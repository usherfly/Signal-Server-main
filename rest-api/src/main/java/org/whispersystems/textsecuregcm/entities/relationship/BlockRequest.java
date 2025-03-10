package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 封禁请求
 */
public class BlockRequest {
    /**
     * 当前用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 目标用户ID
     */
    @Schema(required = true)
    public String targetUserId;

    /**
     * 封禁原因
     */
    @Schema(required = false)
    public String reason;
} 