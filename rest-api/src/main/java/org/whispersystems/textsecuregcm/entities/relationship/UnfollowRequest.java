package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 取消关注请求
 */
public class UnfollowRequest {
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
} 