/*
 * Copyright (C) 2017 zhichao.huang, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.superman.fastokhttp.manager;

import com.superman.fastokhttp.https.HttpsCerManager;
import com.superman.fastokhttp.manager.OkHttpManager.Builder;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * 生产OkHttpClient的工厂类
 * 
 * @author zhichao.huang
 *
 */
public class OkHttpFactory {
	
	private static GzipRequestInterceptor gzipRequestInterceptor = new GzipRequestInterceptor();

    public static OkHttpClient getOkHttpClientFactory(final Builder builder) {
    	
    	OkHttpClient client = new OkHttpClient();
    	
    	okhttp3.OkHttpClient.Builder mBuilder = client.newBuilder();
    	//设置请求时间
    	mBuilder.connectTimeout(builder.mTimeout, TimeUnit.MILLISECONDS);
    	mBuilder.readTimeout(builder.mTimeout, TimeUnit.MILLISECONDS);
    	mBuilder.writeTimeout(builder.mTimeout, TimeUnit.MILLISECONDS);
    	//请求失败后尝试重连
    	mBuilder.retryOnConnectionFailure(true);
    	//请求支持重定向
    	mBuilder.followRedirects(true);
    	
        if(builder.cachedDir != null){
        	//设置缓存
        	mBuilder.cache(new Cache(builder.cachedDir, builder.maxCachedSize));
        }
        
        Interceptor cacheInterceptor = new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", String.format("max-age=%d", builder.maxCacheAge))
                        .build();
            }
        };
        
        //配置Gzip拦截
        if(builder.isGzip){
            if(!mBuilder.interceptors().contains(gzipRequestInterceptor)){
                mBuilder.interceptors().add(new GzipRequestInterceptor());
            }
        }
        
        if(builder.interceptors!=null && !builder.interceptors.isEmpty()){
        	mBuilder.interceptors().addAll(builder.interceptors);
        }
        
        //配置缓存拦截
        mBuilder.networkInterceptors().add(cacheInterceptor);
        
        if (builder.networkInterceptors != null && !builder.networkInterceptors.isEmpty()){
        	mBuilder.networkInterceptors().addAll(builder.networkInterceptors);
        }
        
        //主机名校验
        HostnameVerifier hostnameVerifier = builder.mHostnameVerifier;
        if (hostnameVerifier != null) {
        	mBuilder.hostnameVerifier(hostnameVerifier);
        }
        
        //设置证书cer
        setCertificates(mBuilder, builder.mCertificateList); 
        
        return mBuilder.build();
    }
    
    /**
     * 设置证书Certificate
     * @param mBuilder
     * @param builder
     */
    public static void setCertificates (okhttp3.OkHttpClient.Builder mBuilder, List<InputStream> cers) {
    	if (cers != null && cers.size() > 0) {
			HttpsCerManager httpsCerManager = new HttpsCerManager(mBuilder);
			httpsCerManager.setCertificates(cers);
		}
    }
    
    static class GzipRequestInterceptor implements Interceptor {
        @Override public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Accept-Encoding","gzip")
//                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override public MediaType contentType() {
                    return body.contentType();
                }

                @Override public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }
}
