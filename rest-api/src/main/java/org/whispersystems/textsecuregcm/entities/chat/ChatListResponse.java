package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 聊天列表响应
 */
public class ChatListResponse {
    /**
     * 聊天项列表
     */
    @Schema(required = true)
    public ChatItem[] items;
} 