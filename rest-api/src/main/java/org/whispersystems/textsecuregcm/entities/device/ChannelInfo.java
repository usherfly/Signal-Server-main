package org.whispersystems.textsecuregcm.entities.device;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 频道信息响应
 */
public class ChannelInfo {
    /**
     * Sendbird应用ID
     */
    @Schema(required = true)
    public String applicationId;

    /**
     * 频道URL
     */
    @Schema(required = true)
    public String channelUrl;

    /**
     * 设备对应的Sendbird用户ID
     */
    @Schema(required = true)
    public String imUserId;

    /**
     * 访问令牌
     */
    @Schema(required = true)
    public String sessionToken;

    /**
     * 目标设备的Sendbird用户ID
     */
    @Schema(required = true)
    public String targetImUserId;

    /**
     * 频道类型标识
     */
    @Schema(required = true)
    public String customType;

    /**
     * 元数据
     */
    @Schema(required = true)
    public ChannelMetadata metadata;
} 