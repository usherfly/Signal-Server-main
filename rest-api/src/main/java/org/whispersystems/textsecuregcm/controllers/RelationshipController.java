package org.whispersystems.textsecuregcm.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.whispersystems.textsecuregcm.entities.relationship.FollowRequest;
import org.whispersystems.textsecuregcm.entities.relationship.BlockRequest;
import org.whispersystems.textsecuregcm.entities.relationship.ReportRequest;
import org.whispersystems.textsecuregcm.entities.relationship.ReportResponse;

/**
 * 用户关系管理相关的API接口
 */
@Path("/v1/relationships")
public class RelationshipController {

    /**
     * 获取关注列表
     * 获取用户关注的其他用户列表
     *
     * @param userId 用户ID
     * @param pageSize 分页大小
     * @param cursor 分页游标
     * @return 关注列表响应
     */
    @GET
    @Path("/following")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowingList(
        @QueryParam("userId") String userId,
        @QueryParam("pageSize") Integer pageSize,
        @QueryParam("cursor") String cursor
    ) {
        return Response.ok().build();
    }

    /**
     * 获取粉丝列表
     * 获取关注当前用户的其他用户列表
     *
     * @param userId 用户ID
     * @param pageSize 分页大小
     * @param cursor 分页游标
     * @return 粉丝列表响应
     */
    @GET
    @Path("/followers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowersList(
        @QueryParam("userId") String userId,
        @QueryParam("pageSize") Integer pageSize,
        @QueryParam("cursor") String cursor
    ) {
        return Response.ok().build();
    }

    /**
     * 关注用户
     * 当前用户关注目标用户
     *
     * @param request 关注请求
     * @return 关注结果响应
     */
    @POST
    @Path("/follow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(FollowRequest request) {
        return Response.ok().build();
    }

    /**
     * 取消关注
     * 当前用户取消关注目标用户
     *
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 取消关注结果响应
     */
    @DELETE
    @Path("/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser(
        @QueryParam("userId") String userId,
        @QueryParam("targetUserId") String targetUserId
    ) {
        return Response.ok().build();
    }

    /**
     * 获取黑名单列表
     * 获取当前用户拉黑的用户列表
     *
     * @param userId 用户ID
     * @param pageSize 分页大小
     * @param cursor 分页游标
     * @return 黑名单列表响应
     */
    @GET
    @Path("/blocks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlockList(
        @QueryParam("userId") String userId,
        @QueryParam("pageSize") Integer pageSize,
        @QueryParam("cursor") String cursor
    ) {
        return Response.ok().build();
    }

    /**
     * 拉黑用户
     * 当前用户拉黑目标用户
     *
     * @param request 拉黑请求
     * @return 拉黑结果响应
     */
    @POST
    @Path("/block")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response blockUser(BlockRequest request) {
        return Response.ok().build();
    }

    /**
     * 取消拉黑
     * 当前用户取消拉黑目标用户
     *
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 取消拉黑结果响应
     */
    @DELETE
    @Path("/block")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unblockUser(
        @QueryParam("userId") String userId,
        @QueryParam("targetUserId") String targetUserId
    ) {
        return Response.ok().build();
    }

    /**
     * 举报用户
     * 当前用户举报目标用户
     *
     * @param request 举报请求
     * @return 举报结果响应
     */
    @POST
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportUser(ReportRequest request) {
        return Response.ok().build();
    }
} 