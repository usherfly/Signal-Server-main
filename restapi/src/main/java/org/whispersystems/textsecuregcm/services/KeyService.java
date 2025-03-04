package org.whispersystems.textsecuregcm.services;

import org.whispersystems.textsecuregcm.models.*;

import java.util.List;

public interface KeyService {
    
    // 获取预密钥包
    PreKeyBundle getPreKeyBundle(String accountId, int deviceId);
    
    // 获取预密钥数量
    int getPreKeyCount(String accountId, int deviceId);
    
    // 设置预密钥
    void setPreKeys(String accountId, int deviceId, List<PreKey> preKeys);
    
    // 设置签名预密钥
    void setSignedPreKey(String accountId, int deviceId, SignedPreKey signedPreKey);
    
    // 设置 KEM 预密钥
    void setKemPreKey(String accountId, int deviceId, KEMSignedPreKey kemPreKey);
    
    // 获取密钥透明度证明
    KeyTransparencyProof getKeyTransparencyProof(String accountId);
    
    // 验证密钥透明度证明
    boolean verifyKeyTransparency(String accountId, KeyTransparencyProof proof);
    
    // 发送加密消息
    void sendEncryptedMessage(String accountId, int deviceId, EncryptedMessage message);
    
    // 接收加密消息
    List<EncryptedMessage> receiveEncryptedMessages(String accountId, int deviceId);
} 