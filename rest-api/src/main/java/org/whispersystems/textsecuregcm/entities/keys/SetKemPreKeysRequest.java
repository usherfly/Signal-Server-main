package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SetKemPreKeysRequest
 */
public class SetKemPreKeysRequest {
    /**
     * 设备ID
     */
    @Schema(required = true)
    public String deviceId;

    /**
     * Identity Public Key
     */
    @Schema(required = true)
    public byte[] identityKey;

    /**
     * KEM Signed PreKey List
     */
    @Schema(required = true)
    public KemSignedPreKey[] preKeys;
} 