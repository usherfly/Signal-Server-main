package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新聊天可见性请求
 */
public class UpdateChatVisibilityRequest {
    /**
     * 用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 可见性设置
     */
    @Schema(required = true)
    public ChatVisibility visibility;
} 