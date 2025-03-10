package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备注册响应
 */
public class DeviceRegistrationResponse {
    /**
     * 已注册的设备ID
     */
    @Schema(required = true)
    public String deviceId;

    /**
     * 注册状态
     */
    @Schema(required = true)
    public String registrationStatus;

    /**
     * 密钥包注册状态
     */
    @Schema(required = true)
    public String keyBundleStatus;

    /**
     * 关联设备列表
     */
    @Schema(required = true)
    public LinkedDevice[] linkedDevices;
} 