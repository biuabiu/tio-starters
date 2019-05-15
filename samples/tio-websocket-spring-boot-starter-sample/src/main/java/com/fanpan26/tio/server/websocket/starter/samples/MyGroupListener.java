package com.fanpan26.tio.server.websocket.starter.samples;

import com.fanpan26.tio.server.websocket.TioWebSocketGroupListener;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;

/**
 * @author fyp
 * @crate 2019/5/15 23:07
 * @project tio-starters
 */
@Service
public class MyGroupListener implements TioWebSocketGroupListener {
    /**
     * 绑定群组后回调该方法
     *
     * @param channelContext
     * @param group
     * @throws Exception
     * @author tanyaowu
     */
    @Override
    public void onAfterBind(ChannelContext channelContext, String group) throws Exception {

    }

    /**
     * 解绑群组后回调该方法
     *
     * @param channelContext
     * @param group
     * @throws Exception
     * @author tanyaowu
     */
    @Override
    public void onAfterUnbind(ChannelContext channelContext, String group) throws Exception {

    }
}
