package org.tio.web.starter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.tio.common.starter.configuration.TioSslProperties;

import static org.tio.web.starter.configuration.TioWebServerSslProperties.PREFIX;

/**
 * @author fanpan26
 */
@ConfigurationProperties(PREFIX)
public class TioWebServerSslProperties extends TioSslProperties {
    public static final String PREFIX = "tio.web.ssl";
}
