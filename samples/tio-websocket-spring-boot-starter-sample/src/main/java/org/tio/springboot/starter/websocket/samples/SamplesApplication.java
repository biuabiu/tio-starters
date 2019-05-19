package org.tio.springboot.starter.websocket.samples;


import org.tio.springboot.starter.websocket.EnableTioWebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTioWebSocketServer
public class SamplesApplication {
    public static void main(String[] args) {
        SpringApplication.run(SamplesApplication.class,args);
    }
}
