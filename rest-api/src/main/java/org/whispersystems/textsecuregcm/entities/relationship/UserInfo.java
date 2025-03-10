package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户信息
 */
public class UserInfo {
    /**
     * 用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 用户名
     */
    @Schema(required = true)
    public String username;

    /**
     * 昵称
     */
    @Schema(required = true)
    public String nickname;

    /**
     * 头像URL
     */
    @Schema(required = true)
    public String profileUrl;

    /**
     * 关注状态
     */
    @Schema(required = true)
    public String followStatus;

    /**
     * 资产金额
     */
    @Schema(required = true)
    public String assetAmount;

    /**
     * 是否已激活设备
     */
    @Schema(required = true)
    public boolean hasActiveDevice;
} 