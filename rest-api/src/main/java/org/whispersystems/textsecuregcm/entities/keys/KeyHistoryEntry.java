package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * KeyHistoryEntry
 */
public class KeyHistoryEntry {
    /**
     * Identity Public Key
     */
    @Schema(required = true)
    public byte[] identityKey;

    /**
     * 使用时间戳
     */
    @Schema(required = true)
    public long timestamp;
} 