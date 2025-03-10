package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新聊天项请求
 */
public class UpdateChatItemRequest {
    /**
     * 用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 更新内容
     */
    @Schema(required = true)
    public ChatItemUpdates updates;
} 