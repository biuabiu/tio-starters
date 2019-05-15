package com.fanpan26.tio.server.websocket;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fanpan26
 * */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TioWebSocketServerMarkerConfiguration.class)
public @interface EnableTioWebSocketServer {
}
