package org.whispersystems.textsecuregcm.entities.relationship;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户搜索响应
 */
public class UserSearchResponse {
    /**
     * 用户列表
     */
    @Schema(required = true)
    public UserInfo[] users;

    /**
     * 下一页游标
     */
    @Schema(required = false)
    public String nextCursor;
} 