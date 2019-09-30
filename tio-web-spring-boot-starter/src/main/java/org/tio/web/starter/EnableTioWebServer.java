package org.tio.web.starter;

import org.springframework.context.annotation.Import;
import org.tio.web.starter.configuration.TioWebServerMarkerConfiguration;

import java.lang.annotation.*;


/**
 * Annotation to activate Tio WebSocket Server related configuration {@link org.tio.web.starter.configuration.TioWebServerAutoConfiguration}
 *
 * @author fanpan26
 * */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TioWebServerMarkerConfiguration.class)
public @interface EnableTioWebServer {
    String[] scanPackages() default {};
}
