package org.tio.web.starter;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.tio.core.stat.IpStatListener;
import org.tio.http.common.*;
import org.tio.http.common.session.id.ISessionIdGenerator;
import org.tio.http.common.session.id.impl.SnowflakeSessionIdGenerator;
import org.tio.http.common.session.limiter.SessionRateLimiter;
import org.tio.http.common.session.limiter.SessionRateVo;
import org.tio.http.server.HttpServerStarter;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.http.server.intf.ThrowableHandler;
import org.tio.http.server.mvc.Routes;
import org.tio.http.server.session.SessionCookieDecorator;
import org.tio.http.server.stat.ip.path.IpPathAccessStats;
import org.tio.http.server.stat.token.TokenPathAccessStats;
import org.tio.server.ServerTioConfig;
import org.tio.utils.Threads;
import org.tio.utils.cache.ICache;
import org.tio.utils.cache.caffeineredis.CaffeineRedisCache;
import org.tio.utils.json.Json;
import org.tio.utils.resp.Resp;
import org.tio.web.starter.configuration.TioWebServerSslProperties;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author fanpan26
 */
public final class TioWebServerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(TioWebServerBootstrap.class);

    private static volatile TioWebServerBootstrap tioWebServerBootstrap;

    private HttpServerStarter serverStarter;
    private ServerTioConfig serverTioConfig;
    private HttpConfig httpConfig;
    private Routes routes;
    private DefaultHttpRequestHandler httpRequestHandler;
    private ISessionIdGenerator sessionIdGenerator;
    private TioWebServerSslProperties sslProperties;
    private ApplicationContext applicationContext;
    private TioWebServerListener tioWebServerListener;

    public static final TioWebServerBootstrap getInstance(ApplicationContext applicationContext
            ,TioWebServerListener tioWebServerListener
            ,TioWebServerSslProperties sslProperties) {
        if (tioWebServerBootstrap == null) {
            synchronized (TioWebServerBootstrap.class) {
                if (tioWebServerBootstrap == null) {
                    tioWebServerBootstrap = new TioWebServerBootstrap(applicationContext
                            , tioWebServerListener, sslProperties);
                }
            }
        }
        return tioWebServerBootstrap;
    }

    private TioWebServerBootstrap(ApplicationContext applicationContext
            ,TioWebServerListener tioWebServerListener
            ,TioWebServerSslProperties sslProperties) {
        this.applicationContext = applicationContext;
        this.tioWebServerListener = tioWebServerListener;
        this.sslProperties = sslProperties;
    }


    private void initSessionIdGenerator() {
        this.sessionIdGenerator = new SnowflakeSessionIdGenerator(1, 1);
    }

    private void initHttpConfig() throws IOException {
        httpConfig = new HttpConfig(8090, 1800L, "/api", ".do");
        httpConfig.setName("tio-web-spring-boot-starter");
        httpConfig.setSessionIdGenerator(this.sessionIdGenerator);
        httpConfig.setSessionCacheName(HttpConfig.SESSION_CACHE_NAME);
        httpConfig.setMaxLiveTimeOfStaticRes(3600);
        httpConfig.setMaxLengthOfMultiBody(1024 * 1024);
        httpConfig.setMaxLengthOfPostBody(1024 * 1024);
        httpConfig.setAppendRequestHeaderString(true);

        httpConfig.setPage404("/404.html");
        httpConfig.setPage500("/500.html");

        httpConfig.setSessionCookieName("tio-session");
        httpConfig.setPageRoot("/resources/html/");

        byte[] bs = Json.toJson(Resp.fail("Your IP is on the blacklist")).getBytes();
        httpConfig.setRespForBlackIp(new HttpResponse(null, bs));
        httpConfig.setSessionRateLimiter(new SessionRateLimiter() {
            @Override
            public boolean allow(HttpRequest request, SessionRateVo sessionRateVo) {
                return false;
            }

            @Override
            public HttpResponse response(HttpRequest request, SessionRateVo sessionRateVo) {
                return null;
            }
        });

        //分布式session
        //CaffeineRedisCache caffeineRedisCache = CaffeineRedisCache.register(RedisInit.get(), httpConfig.getSessionCacheName(), null, sessionTimeout);
        httpConfig.setSessionStore(null);

        if (tioWebServerListener != null) {
            tioWebServerListener.afterSetHttpConfig(this.httpConfig);
        }
    }


    private String[] getScanPackages(boolean useAnnotation) {
        Map<String, Object> annotationMap = applicationContext.getBeansWithAnnotation(EnableTioWebServer.class);
        if (useAnnotation) {
            Annotation[] annotations = annotationMap.entrySet().iterator().next().getValue().getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof EnableTioWebServer) {
                    EnableTioWebServer enableTioWebServer = (EnableTioWebServer) annotation;
                    return enableTioWebServer.scanPackages();
                }
            }
        } else {
            Class applicationClazz = annotationMap.entrySet().iterator().next().getValue().getClass();
            String packageName = applicationClazz.getPackage().getName();
            return new String[]{packageName};
        }
        return null;
    }

    /**
     *  注解包 》 配置包 》 程序所在包
     * */
    private void initRoutes() {
        String[] scanPackages = getScanPackages(true);
        if (scanPackages == null || scanPackages.length == 0) {
            //TODO 添加 routes.scanPackages
            if (true) {

            } else {
                scanPackages = getScanPackages(false);
            }
        }
        this.routes = new Routes(scanPackages);
    }

    private void initRequestHandler() throws Exception {
        httpRequestHandler = new DefaultHttpRequestHandler(this.httpConfig, this.routes);
        //httpRequestHandler.setHttpServerInterceptor(WebApiHttpServerInterceptor.ME);
        //httpRequestHandler.setHttpSessionListener(WebApiHttpSessionListener.ME);
//            httpRequestHandler.setSessionCookieDecorator(new SessionCookieDecorator() {
//                @Override
//                public void decorate(Cookie sessionCookie, HttpRequest request, String domain) {
//
//                }
//            });
//            httpRequestHandler.setThrowableHandler(new ThrowableHandler() {
//                @Override
//                public HttpResponse handler(HttpRequest request, RequestLine requestLine, Throwable throwable) throws Exception {
//                    return null;
//                }
//            });
        //httpRequestHandler.setIpPathAccessStats(null);


        //IpPathAccessStats ipPathAccessStats = new IpPathAccessStats(TioSiteStatPathFilter.me, serverTioConfig, TioSiteIpPathAccessStatListener.ME_SITE_API,
        //       Const.IpPathAccessStatDuration.IP_PATH_ACCESS_STAT_DURATIONS);
        //httpRequestHandler.setIpPathAccessStats(ipPathAccessStats);

        //TokenPathAccessStats tokenPathAccessStats = new TokenPathAccessStats(TioSiteStatPathFilter.me, TioSiteTokenGetter.me, TioSiteCurrUseridGetter.me, serverTioConfig,
        //        TioSiteTokenPathAccessStatListener.ME_SITE_API, TokenPathAccessStatDuration.TOKEN_PATH_ACCESS_STAT_DURATIONS);
        //httpRequestHandler.setTokenPathAccessStats(tokenPathAccessStats);

        if (tioWebServerListener != null) {
            tioWebServerListener.afterSetHttpRequestHandler(this.httpRequestHandler);
        }

    }

    private void initHttpServerStarter() {
        this.serverStarter = new HttpServerStarter(this.httpConfig, this.httpRequestHandler, Threads.getTioExecutor(), Threads.getGroupExecutor());
    }

    private void initServerTioConfig() throws Exception{
        this.serverTioConfig = serverStarter.getServerTioConfig();

        serverTioConfig.logWhenDecodeError = true;
        serverTioConfig.setName("web site name");
        serverTioConfig.setReadBufferSize(20481);
        //serverTioConfig.setIpStatListener(new IpStatListener() {});
        //serverTioConfig.ipStats.addDurations(Const.IpStatDuration.IPSTAT_DURATIONS);

        //		serverTioConfig.ipStats.addDurations(Const.IpStatDuration.IPSTAT_DURATIONS, TioSiteIpStatListener.web);

        if (sslProperties != null && sslProperties.isEnabled()) {

            String keyStoreFile = sslProperties.getKeyStore();
            String trustStoreFile = sslProperties.getTrustStore();
            String keyStorePwd = sslProperties.getPassword();
            serverTioConfig.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);

        }

    }

    private void start() throws Exception {
        serverStarter.start();
    }

    public void stop() {
        try {
            serverStarter.stop();
        } catch (Exception e) {

        }
    }

    public void contextInitialized() {
        try {
            initHttpConfig();
            initRoutes();
            initRequestHandler();
            initHttpServerStarter();
            initServerTioConfig();

            start();
        } catch (Exception e) {
            logger.error("Error occurred while bootstrap Tio Web Server :", e);
            throw new RuntimeException("Error occurred while bootstrap Tio Web Server ", e);
        }
    }

}
