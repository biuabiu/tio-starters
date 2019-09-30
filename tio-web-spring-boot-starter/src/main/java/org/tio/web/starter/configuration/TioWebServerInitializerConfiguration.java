package org.tio.web.starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.tio.web.starter.TioWebServerBootstrap;

/**
 * @author fanpan26
 */
@Configuration
public class TioWebServerInitializerConfiguration implements SmartLifecycle, Ordered {


    @Autowired(required = false)
    private TioWebServerBootstrap webServerBootstrap;

    private boolean running = false;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
        runnable.run();
    }

    @Override
    public void start() {
        new Thread(() -> {
            if (!isRunning()) {
                webServerBootstrap.contextInitialized();
                running = true;
            }
        }, "tioWebServer").start();
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
