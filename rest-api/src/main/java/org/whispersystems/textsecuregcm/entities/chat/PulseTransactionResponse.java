package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Pulse消耗记录响应
 */
public class PulseTransactionResponse {
    /**
     * 交易记录
     */
    @Schema(required = true)
    public PulseTransaction[] transactions;

    /**
     * 下一页游标
     */
    @Schema(required = false)
    public String nextCursor;
} 