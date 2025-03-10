package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SetEcPreKeysRequest
 */
public class SetEcPreKeysRequest {
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
     * EC PreKey List
     */
    @Schema(required = true)
    public EcPreKey[] preKeys;
} 