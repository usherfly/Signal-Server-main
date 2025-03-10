package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 关联设备
 */
public class LinkedDevice {
    /**
     * 关联设备ID
     */
    @Schema(required = true)
    public String deviceId;

    /**
     * 设备类型
     */
    @Schema(required = true)
    public String deviceType;

    /**
     * 最后活跃时间
     */
    @Schema(required = true)
    public String lastActiveAt;
} 