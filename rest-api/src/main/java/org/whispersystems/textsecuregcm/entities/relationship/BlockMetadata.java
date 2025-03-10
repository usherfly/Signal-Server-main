package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 封禁元数据
 */
public class BlockMetadata {
    /**
     * 聊天室状态
     */
    @Schema(required = true)
    public String chatRoomStatus;

    /**
     * Sendbird频道状态
     */
    @Schema(required = true)
    public String sendbirdStatus;
} 