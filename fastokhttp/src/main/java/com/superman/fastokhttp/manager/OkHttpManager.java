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

import android.content.Context;
import android.text.TextUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import com.superman.fastokhttp.core.CachePolicyMode;
import com.superman.fastokhttp.core.Constants;

import okio.Buffer;

/**
 * OkHttp 管理类
 * @author zhichao.huang
 *
 */
public class OkHttpManager {

	private OkHttpClient mOkHttpClient;
	private Builder mBuilder;

	private Map<String, String> mCommonParamsMap;
	private Map<String, String> mCommonHeaderMap;
	private List<InputStream> mCertificateList;
	private HostnameVerifier mHostnameVerifier;
	public CachePolicyMode cacheType;
	private long mTimeout;
	private boolean mDebug;

	private static OkHttpManager mOkHttpFinal;

	private OkHttpManager(Builder builder) {
		this.mCommonParamsMap = builder.mCommonParamsMap;
		this.mCommonHeaderMap = builder.mCommonHeaderMap;
		this.mCertificateList = builder.mCertificateList;
		this.mHostnameVerifier = builder.mHostnameVerifier;
		this.cacheType = builder.cacheType;
		this.mTimeout = builder.mTimeout;
		this.mDebug = builder.mDebug;
		mBuilder = builder;
	}

	public void init() {
		this.mOkHttpClient = OkHttpFactory.getOkHttpClientFactory(mBuilder);
		Constants.DEBUG = mDebug;
		mOkHttpFinal = this;
	}

	public static class Builder {
		private Map<String, String> mCommonParamsMap;
		private Map<String, String> mCommonHeaderMap;
		public List<InputStream> mCertificateList;
		public HostnameVerifier mHostnameVerifier;

		public int maxCachedSize = 5 * 1024 *1024;//5M的缓存空间
		public File cachedDir;//缓存sd目录
		public List<Interceptor> networkInterceptors;
		public List<Interceptor> interceptors;
		public int maxCacheAge = 3600 * 12;//缓存年龄 有效期
		public CachePolicyMode cacheType = CachePolicyMode.POLICY_ONLY_NETWORK;//缓存策略,默认只读取最新的网络数据,不加载缓存数据
		public boolean isGzip = false;//请求数据是否以压缩的方式发送，需要服务端支持这种访问模式

		public long mTimeout = Constants.REQ_TIMEOUT;//默认30秒超时时间
		public boolean mDebug;

		public Builder() {
			this.mCommonParamsMap = new HashMap<String, String>();
			this.mCommonHeaderMap = new HashMap<String, String>();
			this.mCertificateList = new ArrayList<InputStream>();
		}

		/**
		 * 添加公共参数
		 * @param paramsMap
		 * @return
		 */
		public Builder setCommenParams(Map<String, String> paramsMap){
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				if ( !TextUtils.isEmpty(entry.getKey()) ) {
					String value = "";
					if ( !TextUtils.isEmpty(entry.getValue()) ) {
						value = entry.getValue();
					}
					mCommonParamsMap.put(entry.getKey(), value);
				}
			}
			return this;
		}

		/**
		 * 公共header
		 * @param headerMap
		 * @return
		 */
		public Builder setCommenHeader(Map<String, String> headerMap) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				if ( !TextUtils.isEmpty(entry.getKey()) ) {
					String value = "";
					if ( !TextUtils.isEmpty(entry.getValue()) ) {
						value = entry.getValue();
					}
					mCommonHeaderMap.put(entry.getKey(), value);
				}
			}
			return this;
		}

		/**
		 * 指定证书
		 * @param certificates
		 * @return
		 */
		public Builder setCertificates(InputStream... certificates) {
			for(InputStream inputStream:certificates) {
				if ( inputStream != null ) {
					mCertificateList.add(inputStream);
				}
			}
			return this;
		}

		public Builder setCertificates(String... certificates) {
			for(String certificate:certificates) {
				if (!TextUtils.isEmpty(certificate)) {
					mCertificateList.add(new Buffer()
					.writeUtf8(certificate)
					.inputStream());
				}
			}
			return this;
		}

		public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
			this.mHostnameVerifier = hostnameVerifier;
			return this;
		}

		/**
		 * 设置调试开关
		 * @param debug
		 * @return
		 */
		 public Builder setDebug(boolean debug) {
			this.mDebug = debug;
			return this;
		}

		/**
		 * 设置timeout
		 * @param timeout
		 * @return
		 */
		 public Builder setTimeout(long timeout) {
			 this.mTimeout = timeout;
			 return this;
		 }

		 public Builder gzip(boolean openGzip) {
			 this.isGzip = openGzip;
			 return this;
		 }

		 public Builder cacheType(CachePolicyMode cacheType){
			 this.cacheType = cacheType;
			 return this;
		 }

		 public Builder cachedDir(File cachedDir) {
			 this.cachedDir = cachedDir;
			 return this;
		 }

		 /**
		  * 拦截器使用可参考这篇文章  <a href="http://www.tuicool.com/articles/Uf6bAnz">http://www.tuicool.com/articles/Uf6bAnz</a>
		  * @param interceptors
		  */
		 public Builder interceptors(List<Interceptor> interceptors) {
			 this.interceptors = interceptors;
			 return this;
		 }

		 public Builder maxCachedSize(int maxCachedSize) {
			 this.maxCachedSize = maxCachedSize;
			 return this;
		 }

		 /**
		  * 拦截器使用可参考这篇文章  <a href="http://www.tuicool.com/articles/Uf6bAnz">http://www.tuicool.com/articles/Uf6bAnz</a>
		  * @param networkInterceptors
		  */
		 public Builder networkInterceptors(List<Interceptor> networkInterceptors) {
			 this.networkInterceptors = networkInterceptors;
			 return this;
		 }

		 public Builder maxCacheAge(int maxCacheAge){
			 this.maxCacheAge = maxCacheAge;
			 return this;
		 }

		 public OkHttpManager build() {
			 return new OkHttpManager(this);
		 }
	}

	public static OkHttpManager getOkHttpFinal() {
		if (mOkHttpFinal == null) {
			return getDefaultOkHttpFinal();
		}
		return mOkHttpFinal;
	}
	
	/**
	 * 在 Application 初始化 OkHttpManager 
	 * 默认请求超时时间30000ms
	 * 
	 * @param context
	 * @param cachedDir 如果传null过来的话 缓存目录默认设为 new File(context.getFilesDir(), "okhttp_net_cache_datas")
	 * @param debug 是否开启debug 日志打印，发布时需设置为false
	 * @return
	 */
	public static OkHttpManager initDroidOkHttp(Context context, File cachedDir, boolean debug) {
		if (mOkHttpFinal == null) {
			
			Map<String, String> commonParamMap = new HashMap<String, String>();
	        Map<String, String> commonHeaderMap = new HashMap<String, String>();
	        
	        //缓存目录文件
	        File mCachedDir = null;
	        if (cachedDir == null && context != null) {
	        	mCachedDir = new File(context.getFilesDir(), "okhttp_net_cache_datas");
	        }
	        
	        OkHttpManager okHttpManager = new OkHttpManager.Builder()
	                .setCommenParams(commonParamMap)
	                .setCommenHeader(commonHeaderMap)
	                .setTimeout(Constants.REQ_TIMEOUT)
	                .setDebug(debug)
	                .cachedDir(mCachedDir)//缓存目录 (必须的配置)
	                //.setCertificates(...)
	                //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
	                .build();
	        okHttpManager.init();
			return okHttpManager;
		}
		return mOkHttpFinal;
	}

	public static OkHttpManager getDefaultOkHttpFinal() {
		OkHttpManager okHttpFinal = new Builder().setTimeout(Constants.REQ_TIMEOUT).build();
		okHttpFinal.init();
		return okHttpFinal;
	}

	public OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}

	public Map<String, String> getCommonParams() {
		return mCommonParamsMap;
	}

	public List<InputStream> getCertificateList() {
		return mCertificateList;
	}

	public HostnameVerifier getHostnameVerifier() {
		return mHostnameVerifier;
	}

	public long getTimeout() {
		return mTimeout;
	}

	public Map<String, String> getCommonHeaderMap() {
		return mCommonHeaderMap;
	}
	
	public CachePolicyMode getCachePolicy () {
		return cacheType;
	}
	
	public Builder getBuilder () {
		return mBuilder;
	}
	
	/**
	 * 清除缓存
	 */
	public void clearCached(){
        try {
        	mOkHttpClient.cache().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
