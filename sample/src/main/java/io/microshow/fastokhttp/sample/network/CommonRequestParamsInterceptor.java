package io.microshow.fastokhttp.sample.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.microshow.rxretrofit.encrypt.JWTFactory;
import io.microshow.rxretrofit.interceptor.RequestParamsInterceptor;

/**
 *
 */
public class CommonRequestParamsInterceptor extends RequestParamsInterceptor {

    @Override
    public Map<String, String> getCommonParams() {
        Map<String, String> PARAMS = new ConcurrentHashMap<>();
        PARAMS.put("secret", JWTFactory.getSecret(
                GlobalManager.getInstance().getServerTime(),
                GlobalManager.getInstance().getJwtIss(),
                GlobalManager.getInstance().getJwtSecret()));
        return PARAMS;
    }

}
