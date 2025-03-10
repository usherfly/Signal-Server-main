package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * EcPreKey
 */
public class EcPreKey {
    /**
     * Key ID
     */
    @Schema(required = true)
    public int keyId;

    /**
     * EC Public Key
     */
    @Schema(required = true)
    public byte[] publicKey;
} 