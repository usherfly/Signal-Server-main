package org.whispersystems.textsecuregcm.entities.keys;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * KeyTransparencyProof
 */
public class KeyTransparencyProof {
    /**
     * 账户ID
     */
    @Schema(required = true)
    public String accountId;

    /**
     * 密钥历史记录
     */
    @Schema(required = true)
    public KeyHistoryEntry[] keyHistory;

    /**
     * 证明
     */
    @Schema(required = true)
    public MerkleProof proof;
} 