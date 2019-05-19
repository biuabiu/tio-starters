package org.tio.springboot.starter.websocket.samples;

import org.tio.springboot.starter.websocket.TioWebSocketServerAioListener;
import org.tio.springboot.starter.websocket.WebSocketAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @author fyp
 * @crate 2019/5/18 23:08
 * @project tio-starters
 */
@WebSocketAioListener
public class MyServerAioListener extends TioWebSocketServerAioListener {
    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        super.onAfterSent(channelContext, packet, isSentSuccess);

        System.out.println("onAfterSent");
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        super.onAfterConnected(channelContext, isConnected, isReconnect);

        System.out.println("onAfterConnected");
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        super.onBeforeClose(channelContext, throwable, remark, isRemove);
        System.out.println("onBeforeClose");
    }
}

