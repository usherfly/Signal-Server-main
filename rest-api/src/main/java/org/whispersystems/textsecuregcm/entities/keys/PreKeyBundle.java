package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PreKeyBundle
 */
public class PreKeyBundle {
    /**
     * EC Signed PreKey
     */
    @Schema(required = true)
    public EcSignedPreKey ecSignedPreKey;

    /**
     * EC One-Time PreKey
     */
    @Schema(required = false)
    public EcPreKey ecOneTimePreKey;

    /**
     * KEM One-Time PreKey
     */
    @Schema(required = true)
    public KemSignedPreKey kemOneTimePreKey;
} 