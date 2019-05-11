package io.microshow.rxretrofit.interceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头拦截器
 */
public abstract class RequestHeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Map<String, String> headers = getCommonHeaders();

        if (headers == null || headers.size() == 0) {
            return chain.proceed(request);
        }
        // 构建新的请求
        Request.Builder newRequestBuilder = request.newBuilder();
        //newRequestBuilder.addHeader("Connection", "close");
        // 添加公共Header
        for (Entry<String, String> entry : headers.entrySet()) {
            newRequestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        return chain.proceed(newRequestBuilder.build());
    }

    public abstract Map<String, String> getCommonHeaders();

}
