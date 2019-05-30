package org.tio.websocket.starter.samples;

import org.tio.websocket.starter.TioWebSocketMsgHandler;
import org.tio.websocket.starter.WebSocketMsgHandler;
import org.springframework.beans.factory.annotation.Value;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.common.HttpResponseStatus;
import org.tio.utils.hutool.StrUtil;
import org.tio.websocket.common.WsRequest;

@WebSocketMsgHandler
public class EchoMsgHandler implements TioWebSocketMsgHandler {


    @Value("${tio.websocket.server.port}")
    private Integer port;

    private static final String GROUP_ALL = "TIO-WEBSOCKET-SPRING-BOOT-STARTER-ALL";

    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String userId = httpRequest.getParam("uid");
        if (StrUtil.isBlank(userId)) {
            httpResponse.setStatus(HttpResponseStatus.C401);
        }
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        Tio.bindGroup(channelContext, GROUP_ALL);
        String userId = httpRequest.getParam("uid");
        Tio.bindUser(channelContext, userId);
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Tio.remove(channelContext, "websocket closed");
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String s, ChannelContext channelContext) throws Exception {
        return null;
    }
}
