package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 举报响应
 */
public class ReportResponse {
    /**
     * 举报状态
     */
    @Schema(required = true)
    public String status;

    /**
     * 操作时间
     */
    @Schema(required = true)
    public long timestamp;
} 