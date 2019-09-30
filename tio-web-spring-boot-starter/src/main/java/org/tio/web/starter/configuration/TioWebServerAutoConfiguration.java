package org.tio.web.starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tio.web.starter.TioWebServerBootstrap;
import org.tio.web.starter.TioWebServerListener;

/**
 * @author fanpan26
 */
@Configuration
@Import(TioWebServerInitializerConfiguration.class)
@ConditionalOnBean(TioWebServerMarkerConfiguration.Marker.class)
@EnableConfigurationProperties({TioWebServerSslProperties.class})
public class TioWebServerAutoConfiguration {


    @Autowired(required = false)
    private TioWebServerListener webServerListener;

    @Autowired
    private TioWebServerSslProperties sslProperties;


    @Bean(destroyMethod = "stop")
    public TioWebServerBootstrap getTioWebServerBootstrap(ApplicationContext applicationContext) {
        return TioWebServerBootstrap.getInstance(applicationContext
                , webServerListener
                , sslProperties);
    }
}
