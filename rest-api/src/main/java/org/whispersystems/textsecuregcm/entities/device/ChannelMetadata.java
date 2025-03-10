package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 频道元数据
 */
public class ChannelMetadata {
    /**
     * 是否启用加密
     */
    @Schema(required = true)
    public boolean encryptionEnabled;

    /**
     * 设备信息
     */
    @Schema(required = true)
    public TargetDeviceInfo deviceInfo;
} 