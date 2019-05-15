package com.fanpan26.tio.server.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Responsible for adding in a marker bean to activate
 * {@link TioWebSocketServerAutoConfiguration}
 * @author fanpan26
 * */
@Configuration
public class TioWebSocketServerMarkerConfiguration {

    @Bean
    public Marker tioWebSocketServerMarkBean() {
        return new Marker();
    }

    class Marker {
    }
}