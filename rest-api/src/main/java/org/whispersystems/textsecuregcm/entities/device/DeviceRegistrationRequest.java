package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备注册请求
 */
public class DeviceRegistrationRequest {
    /**
     * 用户ID
     */
    @Schema(required = true)
    public String userId;

    /**
     * 设备信息
     */
    @Schema(required = true)
    public DeviceInfo deviceInfo;
} 