package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 聊天元数据
 */
public class ChatMetadata {
    /**
     * 群聊特有字段
     */
    @Schema(required = false)
    public GroupInfo groupInfo;

    /**
     * 单聊特有字段
     */
    @Schema(required = false)
    public PrivateInfo privateInfo;
} 