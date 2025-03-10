package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * MerkleProof
 */
public class MerkleProof {
    /**
     * Merkle Tree Root Hash
     */
    @Schema(required = true)
    public String merkleRoot;

    /**
     * Merkle Inclusion Proof
     */
    @Schema(required = true)
    public String merkleProof;

    /**
     * Server Signature
     */
    @Schema(required = true)
    public byte[] signature;
} 