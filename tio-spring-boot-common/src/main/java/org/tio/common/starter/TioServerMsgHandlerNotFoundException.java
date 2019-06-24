package org.tio.common.starter;

/**
 * @author fyp
 * @crate 2019/6/24 21:52
 * @project tio-starters
 */

public class TioServerMsgHandlerNotFoundException extends RuntimeException {
    public TioServerMsgHandlerNotFoundException() {
        super();
    }

    public TioServerMsgHandlerNotFoundException(String msg) {
        super(msg);
    }
}
