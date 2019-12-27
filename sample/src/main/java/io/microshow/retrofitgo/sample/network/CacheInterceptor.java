package io.microshow.retrofitgo.sample.network;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import io.microshow.retrofitgo.cache.CachePolicyMode;
import io.microshow.retrofitgo.cache.CacheSpUtils;
import io.microshow.retrofitgo.cache.MD5Util;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * cache 拦截器
 */
public class CacheInterceptor implements Interceptor {

    private static final String TAG = CacheInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Charset UTF8 = Charset.forName("UTF-8");

        // 打印请求报文
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String reqBody = null;
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            reqBody = buffer.readString(charset);
        }
        Log.d(TAG, String.format("发送请求\nmethod：%s\nurl：%s\nheaders: %s\nbody：%s",
                request.method(), request.url(), request.headers(), reqBody));

        //从请求头读取 缓存模式
        String cacheMode = request.header("CACHE_MODE");

        // 打印返回报文
        // 先执行请求，才能够获取报文
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String respBody = null;
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            respBody = buffer.clone().readString(charset);
        }
        Log.d(TAG, String.format("收到响应\n%s %s\n请求url：%s\n请求body：%s\n响应body：%s",
                response.code(), response.message(), response.request().url(), reqBody, respBody));

        if (!TextUtils.isEmpty(cacheMode) && !cacheMode.equals(CachePolicyMode.POLICY_ONLY_NETWORK.name())) {
            //不是POLICY_ONLY_NETWORK 模式 都要缓存数据
            //缓存数据
            String cacheKey = createCacheKey(request, reqBody);
            CacheSpUtils.saveCacheData(cacheKey, respBody);
            Log.d(TAG, "cacheKey=" + cacheKey + ";cacheData=" + respBody);
        }

        return response;

    }

    //创建缓存key
    public String createCacheKey(Request request, String reqBody) {
        return MD5Util.encrypt(request.method() + request.url() + request.headers() + reqBody);
    }

}
