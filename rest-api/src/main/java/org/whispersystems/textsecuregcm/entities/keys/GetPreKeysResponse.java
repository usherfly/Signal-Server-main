package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * GetPreKeysResponse
 */
public class GetPreKeysResponse {
    /**
     * Identity Public Key
     */
    @Schema(required = true)
    public byte[] identityKey;

    /**
     * 设备ID
     */
    @Schema(required = true)
    public String deviceId;

    /**
     * PreKeys
     */
    @Schema(required = true)
    public PreKeyBundle preKeys;
} 