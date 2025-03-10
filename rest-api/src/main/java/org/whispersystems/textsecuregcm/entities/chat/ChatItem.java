package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 聊天项
 */
public class ChatItem {
    /**
     * 聊天项ID
     */
    @Schema(required = true)
    public String id;

    /**
     * 聊天类型(GROUP/PRIVATE)
     */
    @Schema(required = true)
    public String chatType;

    /**
     * Sendbird频道URL
     */
    @Schema(required = true)
    public String channelUrl;

    /**
     * 显示名称
     */
    @Schema(required = true)
    public String name;

    /**
     * 头像URL
     */
    @Schema(required = true)
    public String profileUrl;

    /**
     * 背景图URL
     */
    @Schema(required = true)
    public String backdropUrl;

    /**
     * 是否置顶
     */
    @Schema(required = true)
    public boolean pinned;

    /**
     * 最新消息ID
     */
    @Schema(required = true)
    public String latestMessageId;

    /**
     * 最新消息时间
     */
    @Schema(required = true)
    public long latestMessageTime;

    /**
     * 未读消息数
     */
    @Schema(required = true)
    public int unreadCount;

    /**
     * 状态(ACTIVE/ARCHIVED)
     */
    @Schema(required = true)
    public String status;

    /**
     * 元数据
     */
    @Schema(required = true)
    public ChatMetadata metadata;
} 