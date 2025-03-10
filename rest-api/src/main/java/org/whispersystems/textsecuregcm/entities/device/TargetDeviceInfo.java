package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 目标设备信息
 */
public class TargetDeviceInfo {
    /**
     * 目标设备ID（当前激活设备）
     */
    @Schema(required = true)
    public String deviceId;

    /**
     * 设备类型
     */
    @Schema(required = true)
    public String deviceType;
} 