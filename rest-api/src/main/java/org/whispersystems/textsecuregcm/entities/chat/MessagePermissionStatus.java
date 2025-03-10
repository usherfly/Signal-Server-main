package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 消息发送权限状态
 */
public class MessagePermissionStatus {
    /**
     * 是否有钱包
     */
    @Schema(required = true)
    public boolean hasWallet;

    /**
     * 是否为接收方
     */
    @Schema(required = true)
    public boolean isReceiver;

    /**
     * 是否双向关注
     */
    @Schema(required = true)
    public boolean isMutualFollow;

    /**
     * 是否有回复
     */
    @Schema(required = true)
    public boolean hasReply;

    /**
     * 已支付Pulse数量
     */
    @Schema(required = true)
    public String pulsePaid;
} 