package io.microshow.rxretrofit.internal;

/**
 * 响应状态码
 */
public class ResponseState {

    public static final int SUCCESS = 200;       // 成功
    public static final int TOKEN_EXPIRE = 2;  // token过期
    public static final int SERVER_EXCEPTION = 3;  // 服务器异常

}
