package org.tio.web.starter;

import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;

/**
 * @author fanpan26
 */
public interface TioWebServerListener {

    void afterSetHttpRequestHandler(HttpRequestHandler httpRequestHandler);

    void afterSetHttpConfig(HttpConfig httpConfig);

}
