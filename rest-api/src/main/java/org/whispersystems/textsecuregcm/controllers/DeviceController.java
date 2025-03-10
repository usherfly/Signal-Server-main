package org.whispersystems.textsecuregcm.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.whispersystems.textsecuregcm.entities.device.DeviceRegistrationRequest;
import org.whispersystems.textsecuregcm.entities.device.ChannelInfoRequest;

/**
 * 设备管理相关的API接口
 */
@Path("/v1/devices")
public class DeviceController {

    /**
     * 设备注册接口
     * 用户在新设备上首次使用应用时，需要注册设备信息和密钥信息
     *
     * @param request 设备注册请求
     * @return 设备注册响应
     */
    @POST
    @Path("/registration")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deviceRegistration(DeviceRegistrationRequest request) {
        return Response.ok().build();
    }

    /**
     * 获取频道信息接口
     * 当用户需要与其他用户通信时，查询对应设备的Sendbird频道信息，用于建立WebSocket连接
     *
     * @param request 频道信息请求
     * @return 频道信息响应
     */
    @POST
    @Path("/channel-info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChannelInfo(ChannelInfoRequest request) {
        return Response.ok().build();
    }
} 