# Web3 DM聊天系统设计方案

## 1. 概述
本文档描述基于Sendbird实现的Web3 DM单聊功能，集成Signal协议实现端到端加密，并支持基于用户AA钱包余额的功能。

## 2. 功能需求
- 单聊通信：通过sendbird传递秘文
- 消息端到端加密： 通过signal协议加密，后端实现公钥/预密钥分发
- 聊天列表： 群聊/单聊混排
- 单聊关系维护：
    - follow关系
    - 已有聊天：要支持是否显示控制
- 聊天发起
    - 目标用户搜索&排序
    - 新发的单聊：相当于建组和消息同时触发，接收端实时通知机制
- 用户封禁：封禁后，直接在对应senbird群组中设置禁言。
- 聊天限制：只有满足条件，才会授予在senbird群组发言的权限。
- 用户钱包余额查询
- 消息历史记录：依然从sendbird拉取，但都是秘文，无法查看，只能用作统计

## 3. 非功能需求
- 消息实时性
- 数据安全性
- 系统可用性
- 扩展性

## 4. 系统架构

### 4.1 整体架构图
```mermaid
graph TB
    %% 定义节点样式
    classDef default fill:#f9f9f9,stroke:#333,stroke-width:1px
    classDef client fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
    classDef server fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
    classDef blockchain fill:#e8f5e9,stroke:#388e3c,stroke-width:2px

    %% 客户端层
    subgraph Client["客户端层"]
        direction LR
        UI[用户界面]
        SignalClient[Signal协议]
        SendbirdClient[Sendbird]
        
        UI --> SignalClient
        UI --> SendbirdClient
    end

    %% 服务层
    subgraph Server["服务层"]
        direction LR
        PulseSocial[Pulse Social]
        SendbirdServer[Sendbird服务]
        
        PulseSocial --> SendbirdServer
    end

    %% 区块链层
    subgraph Blockchain["区块链层"]
        direction LR
        ChainListener[链上监听]
        DataWarehouse[数据仓库]
        
        ChainListener --> DataWarehouse
    end

    %% 跨层连接
    SignalClient --> PulseSocial
    SendbirdClient --> SendbirdServer
    PulseSocial --> ChainListener
    PulseSocial --> DataWarehouse

    %% 应用样式
    class UI,SignalClient,SendbirdClient client
    class PulseSocial,SendbirdServer server
    class ChainListener,DataWarehouse blockchain
```

### 4.2 组件说明
1. 客户端层
   - 用户界面：消息展示与交互
   - Signal协议：端到端加密
   - Sendbird：消息收发

2. 服务层
   - Pulse Social：核心业务服务
   - Sendbird服务：消息基础设施

3. 区块链层
   - 链上监听：实时数据同步
   - 数据仓库：历史数据存储


### 4.3 端加密消息通信流程
```mermaid
sequenceDiagram
    participant A as 用户A端
    participant Pulse as Pulse Social
    participant Sendbird as Sendbird服务
    participant B as 用户B端
    
    %% 初始密钥交换阶段
    A->>A: 生成身份密钥对(IdentityKey)
    A->>A: 生成预密钥包(PreKeyBundle)
    A->>Pulse: 上传身份公钥和预密钥包
    B->>B: 生成身份密钥对(IdentityKey)
    B->>B: 生成预密钥包(PreKeyBundle)
    B->>Pulse: 上传身份公钥和预密钥包
    
    %% 会话建立阶段
    A->>Pulse: 请求B的预密钥包
    Pulse->>A: 返回B的预密钥包
    A->>A: 执行X3DH密钥协商
    Note over A: 1. DH1 = DH(身份私钥A, 身份公钥B)<br/>2. DH2 = DH(临时私钥A, 身份公钥B)<br/>3. DH3 = DH(临时私钥A, 预密钥B)<br/>4. 会话密钥 = KDF(DH1 || DH2 || DH3)
    
    %% 消息发送阶段
    A->>A: 使用会话密钥加密消息
    A->>Sendbird: 发送加密消息
    
    Sendbird->>B: 转发加密消息
    B->>B: 执行X3DH密钥协商
    Note over B: 1. DH1 = DH(身份私钥B, 身份公钥A)<br/>2. DH2 = DH(身份私钥B, 临时公钥A)<br/>3. DH3 = DH(预密钥B, 临时公钥A)<br/>4. 会话密钥 = KDF(DH1 || DH2 || DH3)
    B->>B: 使用会话密钥解密消息
    B->>B: 显示原文
```

### 4.4 钱包余额查询流程
```mermaid
graph TD
    A[定时任务] --> B{是否活跃用户?}
    B -->|是| C[链上实时监听]
    B -->|否| D[数据仓库查询]
    C --> E[更新余额缓存]
    D --> E
```

## 5. 技术方案

### 5.1 消息加密方案
- 使用Signal Protocol实现端到端加密
- 每个会话生成唯一的会话密钥
- 支持消息前向安全性
- 密钥协商使用X3DH协议

### 5.2 钱包余额监控方案
1. 实时监听：
   - 监听活跃用户的钱包地址
   - 使用WebSocket订阅链上事件
   - 实时更新余额缓存

2. 定时查询：
   - 每小时从数据仓库同步非活跃用户余额
   - 使用批量查询优化性能
   - 设置余额更新阈值


## 6. 功能设计

### 6.1 单聊通信
- 通过sendbird传递秘文
- 本次在senbird新增application，和之前的群里区分开来
- 单聊组信息获取接口
    - 入参
        - 目标用户ID
    - 出参
        - channel_url
### 6.2 消息端到端加密
 - 通过signal协议加密，后端实现公钥/预密钥分发
 - 
### 6.3 聊天列表
 群聊/单聊混排
### 6.4 单聊关系维护
    - follow关系
    - 已有聊天：要支持是否显示控制
### 6.5 聊天发起
    - 目标用户搜索&排序
    - 新发的单聊：相当于建组和消息同时触发，接收端实时通知机制
### 6.6 用户封禁
封禁后，直接在对应senbird群组中设置禁言。
### 6.7 聊天限制
只有满足条件，才会授予在senbird群组发言的权限。
### 6.8 用户钱包余额查询
### 6.9 消息历史记录
依然从sendbird拉取，但都是秘文，无法查看，只能用作统计


## 7. 安全考虑
- 消息全程加密
- 私钥本地存储
- 定期轮换会话密钥
- 防重放攻击
- 钱包地址验证

## 8. 性能优化
- 消息缓存策略
- 余额查询批处理
- 链上事件过滤
- 连接池管理
- 数据预加载

## 9. 部署方案
- 使用容器化部署
- 多区域部署
- 负载均衡
- 故障转移
- 监控告警

## 10. 后续规划
- 群聊支持
- 富媒体消息
- 消息回执
- 更多钱包集成
- 性能优化





