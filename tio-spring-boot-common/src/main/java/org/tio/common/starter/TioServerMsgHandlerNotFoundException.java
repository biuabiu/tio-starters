package org.tio.common.starter;

/**
 * @author fyp
 */

public class TioServerMsgHandlerNotFoundException extends RuntimeException {
    public TioServerMsgHandlerNotFoundException() {
        super();
    }

    public TioServerMsgHandlerNotFoundException(String msg) {
        super(msg);
    }
}
