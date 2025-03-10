package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备信息
 */
public class DeviceInfo {
    /**
     * 设备唯一标识符
     */
    @Schema(required = true)
    public String deviceId;

    /**
     * 设备类型(iOS/Android/Desktop)
     */
    @Schema(required = true)
    public String deviceType;

    /**
     * 操作系统版本
     */
    @Schema(required = true)
    public String osVersion;

    /**
     * 应用版本号
     */
    @Schema(required = true)
    public String appVersion;

    /**
     * 推送通知令牌
     */
    @Schema(required = true)
    public String pushToken;

    /**
     * Signal协议注册ID
     */
    @Schema(required = true)
    public String registrationId;

    /**
     * Identity Public Key
     */
    @Schema(required = true)
    public byte[] identityKey;
} 