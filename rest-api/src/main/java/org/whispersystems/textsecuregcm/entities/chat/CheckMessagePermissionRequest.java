package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 检查消息发送权限请求
 */
public class CheckMessagePermissionRequest {
    /**
     * 发送方用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 接收方用户ID
     */
    @Schema(required = true)
    public String targetUserId;
} 