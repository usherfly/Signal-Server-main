package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 单聊信息
 */
public class PrivateInfo {
    /**
     * 目标用户ID
     */
    @Schema(required = true)
    public String targetUserId;

    /**
     * 是否启用加密
     */
    @Schema(required = true)
    public boolean encryptionEnabled;

    /**
     * 关注状态
     */
    @Schema(required = true)
    public String followStatus;
} 