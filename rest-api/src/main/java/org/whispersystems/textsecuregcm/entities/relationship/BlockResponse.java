package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 封禁响应
 */
public class BlockResponse {
    /**
     * 封禁状态(SUCCESS/FAILED)
     */
    @Schema(required = true)
    public String status;

    /**
     * 操作时间
     */
    @Schema(required = true)
    public long timestamp;

    /**
     * 元数据
     */
    @Schema(required = true)
    public BlockMetadata metadata;
} 