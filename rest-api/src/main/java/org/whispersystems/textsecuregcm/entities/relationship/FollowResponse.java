package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 关注响应
 */
public class FollowResponse {
    /**
     * 关注状态(FOLLOWING/BLOCKED)
     */
    @Schema(required = true)
    public String followStatus;

    /**
     * 关注时间
     */
    @Schema(required = true)
    public long timestamp;

    /**
     * 元数据
     */
    @Schema(required = true)
    public FollowMetadata metadata;
} 