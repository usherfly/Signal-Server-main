package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 关系信息
 */
public class RelationshipInfo {
    /**
     * 关注状态
     */
    @Schema(required = true)
    public String followStatus;

    /**
     * 聊天状态
     */
    @Schema(required = true)
    public String chatStatus;

    /**
     * 最后互动时间
     */
    @Schema(required = true)
    public long lastInteractionAt;

    /**
     * 拦截状态
     */
    @Schema(required = true)
    public String blockStatus;
} 