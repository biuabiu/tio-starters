package com.fanpan26.tio.server.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
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