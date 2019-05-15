package com.fanpan26.tio.server.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.cluster.TioClusterConfig;
import org.tio.cluster.redisson.RedissonTioClusterTopic;
import org.tio.core.intf.GroupListener;
import org.tio.core.stat.IpStatListener;
import org.tio.server.ServerGroupContext;
import org.tio.utils.Threads;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.IOException;
import java.util.Set;

/**
 * @author fanpan26
 * */
public final class TioWebSocketServerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(TioWebSocketServerBootstrap.class);

    private static final String GROUP_CONTEXT_NAME = "tio-websocket-spring-boot-starter";

    private TioWebSocketServerProperties serverProperties;
    private TioWebSocketServerClusterProperties clusterProperties;
    private RedissonTioClusterTopic redissonTioClusterTopic;
    private WsServerConfig wsServerConfig;
    private TioClusterConfig clusterConfig;
    private WsServerStarter wsServerStarter;
    private ServerGroupContext serverGroupContext;
    private IWsMsgHandler tioWebSocketMsgHandler;
    private IpStatListener ipStatListener;
    private GroupListener groupListener;

    public TioWebSocketServerBootstrap(TioWebSocketServerProperties serverProperties,
                                       TioWebSocketServerClusterProperties clusterProperties,
                                       RedissonTioClusterTopic redissonTioClusterTopic,
                                       IWsMsgHandler tioWebSocketMsgHandler,
                                       IpStatListener ipStatListener,
                                       GroupListener groupListener,
                                       TioWebSocketClassScanner tioWebSocketClassScanner) {
        this.serverProperties = serverProperties;
        this.clusterProperties = clusterProperties;
        if (redissonTioClusterTopic == null) {
            logger.info("cluster mod closed");
        }
        this.redissonTioClusterTopic = redissonTioClusterTopic;

        // IWsMsgHandler bean not found
        if (tioWebSocketClassScanner != null) {
            if (tioWebSocketMsgHandler == null) {
                tioWebSocketClassScanner.scanInstance(IWsMsgHandler.class, instance -> {
                    this.tioWebSocketMsgHandler = (IWsMsgHandler) instance;
                });
            }else{
                this.tioWebSocketMsgHandler = tioWebSocketMsgHandler;
            }

            if (ipStatListener == null) {
                tioWebSocketClassScanner.scanInstance(IpStatListener.class, instance -> {
                    this.ipStatListener = (IpStatListener) instance;
                });
            } else {
                this.ipStatListener = ipStatListener;
            }
            if (groupListener == null) {
                tioWebSocketClassScanner.scanInstance(GroupListener.class, instance -> {
                    this.groupListener = (GroupListener) instance;
                });
            } else {
                this.groupListener = groupListener;
            }
        } else {
            this.tioWebSocketMsgHandler = tioWebSocketMsgHandler;
            this.ipStatListener = ipStatListener;
            this.groupListener = groupListener;
        }
        if (this.tioWebSocketMsgHandler == null) {
            throw new TioWebSocketMsgHandlerNotFoundException();
        }
        if (tioWebSocketClassScanner!=null){
            tioWebSocketClassScanner.destroy();
        }
    }


    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public void contextInitialized() {
        logger.info("initialize tio websocket server");
        try {
            initTioWebSocketConfig();
            initTioWebSocketServer();
            initTioWebSocketServerGroupContext();

            start();
        }
        catch (Throwable e) {
            logger.error("Cannot bootstrap tio websocket server :", e);
            throw new RuntimeException("Cannot bootstrap tio websocket server :", e);
        }
    }

    private void initTioWebSocketConfig() {
        this.wsServerConfig = new WsServerConfig(serverProperties.getPort());
        if (redissonTioClusterTopic != null && clusterProperties.isEnabled()) {
            this.clusterConfig = new TioClusterConfig(redissonTioClusterTopic);
            this.clusterConfig.setCluster4all(clusterProperties.isAll());
            this.clusterConfig.setCluster4bsId(true);
            this.clusterConfig.setCluster4channelId(clusterProperties.isChannel());
            this.clusterConfig.setCluster4group(clusterProperties.isGroup());
            this.clusterConfig.setCluster4ip(clusterProperties.isIp());
            this.clusterConfig.setCluster4user(clusterProperties.isUser());
        }
    }

    private void initTioWebSocketServer() throws Exception {
        wsServerStarter = new WsServerStarter(wsServerConfig,
                tioWebSocketMsgHandler,
                new TioWebSocketServerDefaultUuid(1L,1L),
                Threads.getTioExecutor(),
                Threads.getGroupExecutor());
    }

    private void initTioWebSocketServerGroupContext() {
        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setName(GROUP_CONTEXT_NAME);
        if (ipStatListener != null) {
            serverGroupContext.setIpStatListener(ipStatListener);
        }
        //serverGroupContext.setServerAioListener();
        if (groupListener != null) {
            serverGroupContext.setGroupListener(groupListener);
        }
        if (serverProperties.getHeartbeatTimeout() > 0) {
            serverGroupContext.setHeartbeatTimeout(serverProperties.getHeartbeatTimeout());
        }
        if (clusterConfig != null) {
            serverGroupContext.setTioClusterConfig(clusterConfig);
        }
    }

    private void start() throws IOException {
        wsServerStarter.start();
    }
}
