package org.whispersystems.textsecuregcm.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.whispersystems.textsecuregcm.entities.chat.ChatCreateRequest;
import org.whispersystems.textsecuregcm.entities.chat.ChatUpdateRequest;
import org.whispersystems.textsecuregcm.entities.chat.ChatMemberAddRequest;
import org.whispersystems.textsecuregcm.entities.chat.ChatMemberRemoveRequest;

/**
 * 聊天管理相关的API接口
 */
@Path("/v1/chats")
public class ChatController {

    /**
     * 获取用户的聊天列表
     * 返回用户参与的所有聊天会话，包括私聊和群聊
     *
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return 聊天列表响应
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatList(
        @QueryParam("userId") String userId,
        @QueryParam("deviceId") String deviceId
    ) {
        return Response.ok().build();
    }

    /**
     * 创建新的聊天会话
     * 用户可以创建私聊或群聊会话
     *
     * @param request 聊天创建请求
     * @return 创建结果响应
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createChat(ChatCreateRequest request) {
        return Response.ok().build();
    }

    /**
     * 获取特定聊天会话的详细信息
     * 返回聊天会话的基本信息、成员列表等
     *
     * @param chatId 聊天ID
     * @param userId 用户ID
     * @return 聊天详情响应
     */
    @GET
    @Path("/{chatId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatDetail(
        @PathParam("chatId") String chatId,
        @QueryParam("userId") String userId
    ) {
        return Response.ok().build();
    }

    /**
     * 更新聊天会话信息
     * 群主或管理员可以更新群聊的名称、头像等信息
     *
     * @param chatId 聊天ID
     * @param request 聊天更新请求
     * @return 更新结果响应
     */
    @PUT
    @Path("/{chatId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateChat(
        @PathParam("chatId") String chatId,
        ChatUpdateRequest request
    ) {
        return Response.ok().build();
    }

    /**
     * 删除聊天会话
     * 用户可以删除自己的聊天会话，群主可以解散群聊
     *
     * @param chatId 聊天ID
     * @param userId 用户ID
     * @return 删除结果响应
     */
    @DELETE
    @Path("/{chatId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteChat(
        @PathParam("chatId") String chatId,
        @QueryParam("userId") String userId
    ) {
        return Response.ok().build();
    }

    /**
     * 添加聊天成员
     * 群主或管理员可以添加新成员到群聊
     *
     * @param chatId 聊天ID
     * @param request 成员添加请求
     * @return 添加结果响应
     */
    @POST
    @Path("/{chatId}/members")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addChatMember(
        @PathParam("chatId") String chatId,
        ChatMemberAddRequest request
    ) {
        return Response.ok().build();
    }

    /**
     * 移除聊天成员
     * 群主可以移除群聊成员，用户可以退出群聊
     *
     * @param chatId 聊天ID
     * @param request 成员移除请求
     * @return 移除结果响应
     */
    @DELETE
    @Path("/{chatId}/members")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeChatMember(
        @PathParam("chatId") String chatId,
        ChatMemberRemoveRequest request
    ) {
        return Response.ok().build();
    }
} 