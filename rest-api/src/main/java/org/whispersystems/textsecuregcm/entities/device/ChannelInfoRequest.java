package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 频道信息请求
 */
public class ChannelInfoRequest {
    /**
     * 用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 设备ID
     */
    @Schema(required = true)
    public String deviceId;

    /**
     * 目标用户ID
     */
    @Schema(required = true)
    public String targetUserId;
} 