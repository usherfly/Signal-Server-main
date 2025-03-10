package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Pulse交易记录
 */
public class PulseTransaction {
    /**
     * 交易ID
     */
    @Schema(required = true)
    public String id;

    /**
     * 交易时间
     */
    @Schema(required = true)
    public long timestamp;

    /**
     * 交易类别
     */
    @Schema(required = true)
    public String category;

    /**
     * 数量
     */
    @Schema(required = true)
    public String amount;

    /**
     * 收支方向
     */
    @Schema(required = true)
    public String direction;

    /**
     * 详细信息
     */
    @Schema(required = true)
    public TransactionDetails details;
} 