package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 目标用户
 */
public class TargetUser {
    /**
     * 目标用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 目标用户昵称
     */
    @Schema(required = true)
    public String nickname;
} 