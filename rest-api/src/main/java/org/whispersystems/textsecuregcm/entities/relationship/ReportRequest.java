package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 举报请求
 */
public class ReportRequest {
    /**
     * 举报者用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 被举报用户ID
     */
    @Schema(required = true)
    public String targetUserId;

    /**
     * 聊天室ID
     */
    @Schema(required = true)
    public String chatId;

    /**
     * 举报原因
     */
    @Schema(required = true)
    public String reason;
} 