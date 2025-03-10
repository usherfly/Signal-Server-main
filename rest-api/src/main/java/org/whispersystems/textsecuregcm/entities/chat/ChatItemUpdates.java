package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 聊天项更新内容
 */
public class ChatItemUpdates {
    /**
     * 是否置顶
     */
    @Schema(required = false)
    public Boolean pinned;

    /**
     * 是否隐藏
     */
    @Schema(required = false)
    public Boolean hidden;

    /**
     * 状态(ACTIVE/ARCHIVED)
     */
    @Schema(required = false)
    public String status;
} 