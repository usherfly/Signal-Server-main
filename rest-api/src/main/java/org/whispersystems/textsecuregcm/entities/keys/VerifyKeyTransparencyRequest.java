package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * VerifyKeyTransparencyRequest
 */
public class VerifyKeyTransparencyRequest {
    /**
     * 账户ID
     */
    @Schema(required = true)
    public String accountId;

    /**
     * Key Transparency Proof
     */
    @Schema(required = true)
    public KeyTransparencyProof proof;
} 