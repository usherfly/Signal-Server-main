package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 消息发送权限响应
 */
public class MessagePermissionResponse {
    /**
     * 是否可发送
     */
    @Schema(required = true)
    public boolean canSend;

    /**
     * 限制原因
     */
    @Schema(required = false)
    public String reason;

    /**
     * 需要支付的Pulse数量
     */
    @Schema(required = false)
    public String requiredPulseAmount;

    /**
     * 当前状态
     */
    @Schema(required = true)
    public MessagePermissionStatus currentStatus;
} 