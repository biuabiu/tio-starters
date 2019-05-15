package com.fanpan26.tio.server.websocket.starter.samples;

import com.fanpan26.tio.server.websocket.TioWebSocketIpStatListener;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.intf.Packet;
import org.tio.core.stat.IpStat;

/**
 * @author fyp
 * @crate 2019/5/15 23:07
 * @project tio-starters
 */
public class MyIpStatListener implements TioWebSocketIpStatListener {
    /**
     * 统计时间段到期后，用户可以在这个方法中实现把相关数据入库或是打日志等
     *
     * @param groupContext
     * @param ipStat
     */
    @Override
    public void onExpired(GroupContext groupContext, IpStat ipStat) {

    }

    /**
     * 建链后触发本方法，注：建链不一定成功，需要关注参数isConnected
     *
     * @param channelContext
     * @param isConnected    是否连接成功,true:表示连接成功，false:表示连接失败
     * @param isReconnect    是否是重连, true: 表示这是重新连接，false: 表示这是第一次连接
     * @param ipStat
     * @throws Exception
     * @author: tanyaowu
     */
    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect, IpStat ipStat) throws Exception {

    }

    /**
     * 解码异常时
     *
     * @param channelContext
     * @param ipStat
     */
    @Override
    public void onDecodeError(ChannelContext channelContext, IpStat ipStat) {

    }

    /**
     * 发送后（注：不一定会发送成功）
     *
     * @param channelContext
     * @param packet
     * @param isSentSuccess
     * @param ipStat
     * @throws Exception
     */
    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess, IpStat ipStat) throws Exception {

    }

    /**
     * 解码成功后
     *
     * @param channelContext
     * @param packet
     * @param packetSize
     * @param ipStat
     * @throws Exception
     */
    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize, IpStat ipStat) throws Exception {

    }

    /**
     * 接收到一些字节数据后
     *
     * @param channelContext
     * @param receivedBytes
     * @param ipStat
     * @throws Exception
     */
    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes, IpStat ipStat) throws Exception {

    }

    /**
     * 处理一个消息包后
     *
     * @param channelContext
     * @param packet
     * @param ipStat
     * @param cost           耗时，单位：毫秒
     * @throws Exception
     */
    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, IpStat ipStat, long cost) throws Exception {

    }
}
