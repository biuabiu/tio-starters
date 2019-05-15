package com.fanpan26.tio.server.websocket;

import org.springframework.context.ApplicationContext;
import org.tio.utils.hutool.ClassUtil;

import java.util.Map;
import java.util.function.Consumer;

/**
 * If there are no {@link org.springframework.stereotype.Service} annotations or {@link WebSocketMsgHandler} annotations,
 * the {@link org.tio.websocket.server.handler} or {@link TioWebSocketMsgHandler} will be found by scanning packages
 *
 * @author fanpan26
 */
public class TioWebSocketClassScanner {

    private ApplicationContext applicationContext;

    public TioWebSocketClassScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void scanInstance(Class<?> beanClazz, Consumer<Object> consumer) {
        Map<String, Object> annotatioMap = applicationContext.getBeansWithAnnotation(EnableTioWebSocketServer.class);
        Class applicationClazz = annotatioMap.entrySet().iterator().next().getValue().getClass();
        String packageName = applicationClazz.getPackage().getName();

        Object target;
        ;
        try {
            ClassUtil.scanPackage(packageName, clazz -> {
                if (!clazz.isInterface()) {
                    if (beanClazz.isAssignableFrom(clazz)) {
                        try {
                            consumer.accept(clazz.newInstance());
                        } catch (Exception e) {
                            throw new RuntimeException("create new instance of "+clazz.getTypeName()+" failed");
                        }
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {

    }
}
