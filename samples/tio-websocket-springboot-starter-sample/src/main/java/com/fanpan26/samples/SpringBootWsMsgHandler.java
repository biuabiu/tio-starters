package com.fanpan26.samples;

import org.springframework.stereotype.Service;
import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

/**
 * @author fyp
 * @crate 2019/6/13 21:23
 * @project tio-starters
 *
 * 和 Tio WebSocket 用法一致，需要实现 IWsMsgHandler 接口，可以添加 @Service 注解，不加的话会自动扫描该类(需要配置 tio.websocket.server.use-scanner: true)
 */
@TioServerMsgHandler
public class SpringBootWsMsgHandler implements IWsMsgHandler {

    /**
     * 握手
     * */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        return httpResponse;
    }

    /**
     * 握手完毕
     * */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {

    }

    /**
     * binaryType = arraybuffer
     * */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }


    /**
     * 关闭
     * */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * binaryType=text
     * */
    @Override
    public Object onText(WsRequest wsRequest, String s, ChannelContext channelContext) throws Exception {
        Tio.sendToAll(channelContext.getGroupContext(), WsResponse.fromText("服务端收到了消息："+s,"utf-8"));
        return null;
    }
}
