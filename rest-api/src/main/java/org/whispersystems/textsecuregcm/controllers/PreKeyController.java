package org.whispersystems.textsecuregcm.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.whispersystems.textsecuregcm.entities.keys.SetEcPreKeysRequest;
import org.whispersystems.textsecuregcm.entities.keys.SetEcSignedPreKeyRequest;
import org.whispersystems.textsecuregcm.entities.keys.SetKemPreKeysRequest;
import org.whispersystems.textsecuregcm.entities.keys.VerifyKeyTransparencyRequest;

/**
 * PreKey相关的API接口
 */
@Path("/v1/keys")
public class PreKeyController {

    /**
     * 获取PreKey Bundle
     * 当用户A想要与用户B开始加密通信时，需要先获取用户B的 PreKey Bundle，用于建立安全的通信会话
     *
     * @param targetUserId 目标用户ID
     * @param targetDeviceId 目标设备ID
     * @param userId 当前用户ID
     * @param deviceId 当前设备ID
     * @return PreKey Bundle响应
     */
    @GET
    @Path("/prekeys")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreKeys(
        @QueryParam("targetUserId") String targetUserId,
        @QueryParam("targetDeviceId") String targetDeviceId,
        @QueryParam("userId") String userId,
        @QueryParam("deviceId") String deviceId
    ) {
        return Response.ok().build();
    }

    /**
     * 获取One Time PreKey数量
     * 客户端需要定期检查用户的可用 PreKey 数量，当数量低于阈值时生成新的 PreKey
     *
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return PreKey数量响应
     */
    @GET
    @Path("/prekeys/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneTimePreKeyCount(
        @QueryParam("userId") String userId,
        @QueryParam("deviceId") String deviceId
    ) {
        return Response.ok().build();
    }

    /**
     * 设置EC One-Time PreKeys
     * 当用户首次注册设备或 PreKey 数量不足时，客户端会生成一批新的 EC PreKey 并上传到服务器
     *
     * @param request EC PreKey设置请求
     * @return 设置结果响应
     */
    @POST
    @Path("/prekeys/ec")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setOneTimeEcPreKeys(SetEcPreKeysRequest request) {
        return Response.ok().build();
    }

    /**
     * 设置EC Signed PreKey
     * 用户需要定期更新 EC Signed PreKey 以增强安全性
     *
     * @param request EC Signed PreKey设置请求
     * @return 设置结果响应
     */
    @POST
    @Path("/prekeys/ec/signed")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setEcSignedPreKey(SetEcSignedPreKeyRequest request) {
        return Response.ok().build();
    }

    /**
     * 设置KEM One-Time PreKeys
     * 为支持后量子加密，用户需要设置一批 KEM PreKey
     *
     * @param request KEM PreKey设置请求
     * @return 设置结果响应
     */
    @POST
    @Path("/prekeys/kem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setOneTimeKemSignedPreKeys(SetKemPreKeysRequest request) {
        return Response.ok().build();
    }

    /**
     * 获取密钥透明度证明
     * 当用户需要验证其他用户的 Identity Key 是否可信时使用
     *
     * @param accountId 账户ID
     * @return 密钥透明度证明响应
     */
    @GET
    @Path("/transparency/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKeyTransparencyProof(@PathParam("accountId") String accountId) {
        return Response.ok().build();
    }

    /**
     * 验证密钥透明度证明
     * 收到其他用户的 Key Transparency Proof 后，需要验证其有效性
     *
     * @param request 密钥透明度证明验证请求
     * @return 验证结果响应
     */
    @POST
    @Path("/transparency/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyKeyTransparency(VerifyKeyTransparencyRequest request) {
        return Response.ok().build();
    }
} 