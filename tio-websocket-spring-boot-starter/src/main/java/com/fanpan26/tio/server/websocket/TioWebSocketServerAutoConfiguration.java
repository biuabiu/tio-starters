package com.fanpan26.tio.server.websocket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tio.cluster.redisson.RedissonTioClusterTopic;
import org.tio.core.intf.GroupListener;
import org.tio.core.stat.IpStatListener;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.security.acl.Group;


/**
 * @author fanpan26
 * */
@Configuration
@Import(TioWebSocketServerInitializerConfiguration.class)
@ConditionalOnBean(TioWebSocketServerMarkerConfiguration.Marker.class)
@EnableConfigurationProperties({ TioWebSocketServerProperties.class,
        TioWebSocketServerClusterProperties.class,
        TioWebSocketServerClusterProperties.RedisConfig.class })
public class TioWebSocketServerAutoConfiguration {

    /**
     *  cluster topic channel
     * */
    private static final String CLUSTER_TOPIC_CHANNEL = "tio_ws_spring_boot_starter";

    @Autowired(required = false)
    private IWsMsgHandler tioWebSocketMsgHandler;

    @Autowired(required = false)
    private TioWebSocketIpStatListener tioWebSocketIpStatListener;

    @Autowired(required = false)
    private TioWebSocketGroupListener tioWebSocketGroupListener;

    @Autowired
    private TioWebSocketServerClusterProperties clusterProperties;

    @Autowired
    private TioWebSocketServerClusterProperties.RedisConfig redisConfig;

    @Autowired
    private TioWebSocketServerProperties serverProperties;

    @Autowired(required = false)
    private RedissonTioClusterTopic redissonTioClusterTopic;

    @Autowired(required = false)
    private TioWebSocketClassScanner tioWebSocketClassScanner;

    /**
     * Tio WebSocket Server bootstrap
     * */
    @Bean
    public TioWebSocketServerBootstrap webSocketServerBootstrap() {
        return new TioWebSocketServerBootstrap(serverProperties,
                clusterProperties,
                redissonTioClusterTopic,
                tioWebSocketMsgHandler,
                tioWebSocketIpStatListener,
                tioWebSocketGroupListener,
                tioWebSocketClassScanner);
    }

    @Bean
    public ServerGroupContext serverGroupContext(TioWebSocketServerBootstrap bootstrap){
        return bootstrap.getServerGroupContext();
    }

    @Bean
    @ConditionalOnProperty(value = "tio.websocket.cluster.enabled",havingValue = "true",matchIfMissing = true)
    public RedisInitializer redisInitializer(){
        return new RedisInitializer(redisConfig);
    }


    /**
     *  RedissonTioClusterTopic  with  RedisInitializer
     * */
    @Bean
    @ConditionalOnBean(RedisInitializer.class)
    public RedissonTioClusterTopic redissonTioClusterTopic(RedisInitializer redisInitializer) {
        return new RedissonTioClusterTopic(CLUSTER_TOPIC_CHANNEL,redisInitializer.getRedissonClient());
    }

    @Bean(destroyMethod = "destroy")
    public TioWebSocketClassScanner tioWebSocketClassScanner(ApplicationContext applicationContext) {
        return new TioWebSocketClassScanner(applicationContext);
    }
}
