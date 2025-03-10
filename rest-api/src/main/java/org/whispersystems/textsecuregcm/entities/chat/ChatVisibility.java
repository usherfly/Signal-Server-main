package org.whispersystems.textsecuregcm.entities.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 聊天可见性
 */
public class ChatVisibility {
    /**
     * 是否可见
     */
    @Schema(required = true)
    public boolean isVisible;

    /**
     * 归档原因
     */
    @Schema(required = false)
    public String archiveReason;
} 