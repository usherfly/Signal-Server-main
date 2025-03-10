package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * GetPreKeyCountResponse
 */
public class GetPreKeyCountResponse {
    /**
     * EC PreKey count
     */
    @Schema(required = true)
    public int ecPreKeyCount;

    /**
     * KEM PreKey count
     */
    @Schema(required = true)
    public int kemPreKeyCount;
} 