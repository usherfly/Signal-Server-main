package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * KemSignedPreKey
 */
public class KemSignedPreKey {
    /**
     * Key ID
     */
    @Schema(required = true)
    public int keyId;

    /**
     * KEM Public Key
     */
    @Schema(required = true)
    public byte[] publicKey;

    /**
     * Signature by Identity Key
     */
    @Schema(required = true)
    public byte[] signature;
} 