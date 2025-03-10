package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 交易详细信息
 */
public class TransactionDetails {
    /**
     * 消息ID（如果是消息付费）
     */
    @Schema(required = false)
    public String messageId;

    /**
     * 目标用户
     */
    @Schema(required = false)
    public TargetUser targetUser;

    /**
     * 详细描述
     */
    @Schema(required = false)
    public String description;
} 