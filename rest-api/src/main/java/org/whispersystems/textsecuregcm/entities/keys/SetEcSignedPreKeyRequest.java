package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SetEcSignedPreKeyRequest
 */
public class SetEcSignedPreKeyRequest {
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
     * EC Signed PreKey
     */
    @Schema(required = true)
    public EcSignedPreKey signedPreKey;
} 