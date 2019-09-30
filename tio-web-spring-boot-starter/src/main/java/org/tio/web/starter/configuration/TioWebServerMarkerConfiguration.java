package org.tio.web.starter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fanpan26
 */
@Configuration
public class TioWebServerMarkerConfiguration {

    @Bean
    public Marker tioWebServerMarkBean() {
        return new Marker();
    }

    class Marker {
    }
}
