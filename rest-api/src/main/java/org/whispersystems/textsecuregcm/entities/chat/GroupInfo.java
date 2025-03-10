package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 群聊信息
 */
public class GroupInfo {
    /**
     * 管理员用户ID
     */
    @Schema(required = true)
    public String managerUserId;

    /**
     * 成员数量
     */
    @Schema(required = true)
    public int memberCount;

    /**
     * 日限额
     */
    @Schema(required = true)
    public String dayAllowance;

    /**
     * 资产金额
     */
    @Schema(required = true)
    public String assetAmount;

    /**
     * 群组类别
     */
    @Schema(required = true)
    public String groupCategory;

    /**
     * 链名称
     */
    @Schema(required = true)
    public String chainName;

    /**
     * 代币地址
     */
    @Schema(required = true)
    public String tokenAddress;

    /**
     * 加入条件
     */
    @Schema(required = true)
    public String joinConditions;

    /**
     * 供应商
     */
    @Schema(required = true)
    public String supplier;
} 