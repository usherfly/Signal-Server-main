package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 关注元数据
 */
public class FollowMetadata {
    /**
     * 是否互相关注
     */
    @Schema(required = true)
    public boolean isFollowBack;
} 