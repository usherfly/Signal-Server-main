package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * EcSignedPreKey
 */
public class EcSignedPreKey {
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

    /**
     * Signature by Identity Key
     */
    @Schema(required = true)
    public byte[] signature;
} 