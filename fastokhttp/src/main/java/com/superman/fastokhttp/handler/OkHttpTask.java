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

package com.superman.fastokhttp.handler;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.superman.fastokhttp.callback.FileDownloadCallback;
import com.superman.fastokhttp.callback.INetCallback;
import com.superman.fastokhttp.core.CachePolicyMode;
import com.superman.fastokhttp.core.Constants;
import com.superman.fastokhttp.core.Method;
import com.superman.fastokhttp.core.ProgressRequestBody;
import com.superman.fastokhttp.core.RequestParams;
import com.superman.fastokhttp.core.ResponseData;
import com.superman.fastokhttp.core.ResponseDataWrapper;
import com.superman.fastokhttp.manager.OkHttpManager;
import com.superman.fastokhttp.utils.Utils;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * okhttp task 
 * @author zhichao.huang
 *
 */
@SuppressWarnings("rawtypes")
public class OkHttpTask {

	public static final String TAG = OkHttpTask.class.getSimpleName();
	
	static ExecutorService executorService;
	
	Future<?> feture;

	private String url;
	/**
	 * 源url,主要用于再次重新请求
	 */
	private String souceUrl;
	private RequestParams params = new RequestParams();
	private INetCallback callback;
	private Headers headers;
	private Method method;
	private CachePolicyMode cachePolicy;
	private OkHttpClient okHttpClient;
	
	/**
	 * 记录是否中断请求
	 */
	boolean isCanceled = false;
	
	/**
	 * 请求的响应数据
	 */
	ResponseData responseData = null;
	
	public OkHttpTask () {
		OkHttpManager okHttpManager = OkHttpManager.getOkHttpFinal();
		this.cachePolicy = okHttpManager.getCachePolicy();//使用全局默认的缓存策略，只加载网络数据
		this.okHttpClient = okHttpManager.getOkHttpClient();
	}

	public OkHttpTask(Method method, String url, RequestParams params) {
		this();
		this.method = method;
		this.url = url;
		if (params != null) {
			this.params = params;
		}
	}

	public OkHttpTask(Method method, String url, RequestParams params, CachePolicyMode cachePolicy) {
		this (method, url, params);
		if (cachePolicy != null) {
			this.cachePolicy = cachePolicy;
		}
	}
	
	/**
	 * 设置请求参数
	 * @param params
	 * @return
	 */
	public OkHttpTask setRequestParams (RequestParams params) {
		if (params != null) {
			this.params = params;
		}
		return this;
	}
	
	public OkHttpTask setUrl (String url) {
		this.url = url;
		this.souceUrl = url;
		return this;
	}
	
	public OkHttpTask setCachePolicyMode (CachePolicyMode cachePolicy) {
		if (cachePolicy != null) {
			this.cachePolicy = cachePolicy;
		}
		return this;
	}
	
	/**
	 * GET请求
	 * @param callback
	 * @return
	 */
	public OkHttpTask doGet (INetCallback callback) {
		return doExecuse (Method.GET, callback);
	}
	
	/**
	 * POST请求
	 * @param callback
	 * @return
	 */
	public OkHttpTask doPost (INetCallback callback) {
		return doExecuse (Method.POST, callback);
	}
	
	/**
	 * PUT请求
	 * @param callback
	 * @return
	 */
	public OkHttpTask doPut (INetCallback callback) {
		return doExecuse (Method.PUT, callback);
	}
	
	/**
	 * DELETE请求
	 * @param callback
	 * @return
	 */
	public OkHttpTask doDelete (INetCallback callback) {
		return doExecuse (Method.DELETE, callback);
	}
	
	/**
	 * HEAD请求
	 * @param callback
	 * @return
	 */
	public OkHttpTask doHead (INetCallback callback) {
		return doExecuse (Method.HEAD, callback);
	}
	
	/**
	 * PATCH请求
	 * @param callback
	 * @return
	 */
	public OkHttpTask doPatch (INetCallback callback) {
		return doExecuse (Method.PATCH, callback);
	}
	
	public OkHttpTask doExecuse (Method method, INetCallback callback) {
		this.method = method;
		execuse(callback);
		return this;
	}
	
	/**
     * 下载文件
     * @param url
     * @param target 保存的文件
     * @param callback
     */
    public static FileDownloadTask download (String url, File target, FileDownloadCallback callback) {
        if (!TextUtils.isEmpty(url) && target != null) {
            FileDownloadTask task = new FileDownloadTask(url, target, callback);
            task.execute();
            return task;
        }
        return null;
    }
	
	/**
	 * 线程池里跑runnable
	 * 
	 * @param runnable
	 * @return
	 */
	private static Future<?> executeRunalle (Runnable runnable) {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(Constants.net_pool_size);
		}
		return executorService.submit(runnable);
	}
	
	
	private void execuse (INetCallback callback) {
		this.callback = callback;
		if (params.headerMap != null) {//构造请求头
			headers = Headers.of(params.headerMap);
		}
		if (callback != null) {
			callback.onStart();
		}
		this.feture = executeRunalle(new Runnable() {
			
			@Override
			public void run() {
				
				switch (cachePolicy) {
				case POLICY_ONLY_NETWORK:
					responseData = requestFromNetwork ();
					break;
				case POLICY_ONLY_CACHED:
					responseData = requestFromCached ();
					break;
				case POLICY_NETWORK_ELSE_CACHED:
					responseData = requestFromNetwork ();
					break;
				case POLICY_CACHED_AND_NETWORK:
					responseData = requestFromCached ();
					break;
				}
				if (!isCanceled) {//当没有取消网络请求时执行
					//执行解析动作
					parseResponse (responseData);
				}
			}
		});
	}
	
	/**
	 * 取消请求
	 * @param isInterrupt
	 * @return
	 */
	public Boolean cancel(Boolean isInterrupt) {
		this.isCanceled = true;
		if (feture != null) {
			feture.cancel(isInterrupt);
		}
		if (this.callback != null) {
			this.callback.onCancelled();
		}
		return true;
	}

	/**
	 * 请求来自网络
	 * @return
	 */
	private ResponseData requestFromNetwork () {
		return request (CacheControl.FORCE_NETWORK);
	}

	/**
	 * 请求来自缓存
	 * @return
	 */
	private ResponseData requestFromCached () {
		return request (CacheControl.FORCE_CACHE);
	}

	/**
	 * 构建请求Request
	 * @return
	 */
	private ResponseData request (CacheControl cacheControl) {
		Response response = null;
		ResponseData responseData = new ResponseData();
		try {
			String srcUrl = url;
			//构建请求Request实例
			Request.Builder builder = new Request.Builder();

			switch (method) {
			case GET:
				url = Utils.getFullUrl(url, params.getUrlParams());
				builder.get();
				break;
			case DELETE:
				url = Utils.getFullUrl(url, params.getUrlParams());
				builder.delete();
				break;
			case HEAD:
				url = Utils.getFullUrl(url, params.getUrlParams());
				builder.head();
				break;
			case POST:
				RequestBody body = params.getRequestBody();
				if (body != null) {
					builder.post(new ProgressRequestBody(body, callback));
				}
				break;
			case PUT:
				RequestBody bodyPut = params.getRequestBody();
				if (bodyPut != null) {
					builder.put(new ProgressRequestBody(bodyPut, callback));
				}
				break;

			case PATCH:
				RequestBody bodyPatch = params.getRequestBody();
				if (bodyPatch != null) {
					builder.put(new ProgressRequestBody(bodyPatch, callback));
				}
				break;
			}

			builder.url(url).tag(srcUrl).headers(headers).cacheControl(cacheControl);
			Request request = builder.build();
			response = okHttpClient.newCall(request).execute();//执行请求
		} catch (Exception e) {
			if (Constants.DEBUG) {
				Log.e(TAG,"Exception=" + e.getMessage());
			}
			if (e instanceof SocketTimeoutException) {
				responseData.setTimeout(true);
			} else if (e instanceof InterruptedIOException && TextUtils.equals(e.getMessage(),
					"timeout")) {
				responseData.setTimeout(true);
			}
		}

		//获取请求结果
		if (response != null) {
			responseData.setResponseNull(false);
			responseData.setCode(response.code());
			responseData.setMessage(response.message());
			responseData.setSuccess(response.isSuccessful());
			String respBody = "";
			try {
				respBody = response.body().string();
			} catch (IOException e) {
				e.printStackTrace();
			}
			responseData.setResponse(respBody);
			responseData.setHeaders(response.headers());
			//设置缓存标识
			if (cacheControl == CacheControl.FORCE_CACHE) {//来自缓存
				responseData.setCacheData(true);
			} else {//来自网络
				responseData.setCacheData(false);
			}
		} else {
			responseData.setResponseNull(true);
		}
		return responseData;
	}
	
	/**
	 * 构造请求结果Log
	 * @param respBody
	 * @return
	 */
	private String getRequestResultLogString (String respBody) {
		String str = "\n reqMethod=" + method.name() + ";\n reqUrl=" + url + ";\n reqParams:" + params.toString() +";\n respResult:"+respBody;
		return str;
	}

	/**
	 * 解析响应体，及判断 缓存请求和网络请求逻辑
	 * @param responseData
	 */
	private void parseResponse (ResponseData responseData) {
		//判断请求是否在这个集合中
		if (!responseData.isResponseNull()) {//请求得到响应
			if (responseData.isSuccess()) {//成功的请求
				String respBody = responseData.getResponse();
				if (Constants.DEBUG) {
					Log.d(TAG,"request success ==> " + getRequestResultLogString(respBody));
				}
				parseResponseBody(respBody, callback);
			} else {//请求失败
				int code = responseData.getCode();
				String msg = responseData.getMessage();
				if (Constants.DEBUG) {
					Log.d(TAG, "request fail ==> " + getRequestResultLogString("response failure code=" + code + " msg=" + msg));
				}
				if (code == 504) {
					if (callback != null) {
						checkCacheMode(null, INetCallback.ERROR_RESPONSE_TIMEOUT,
								INetCallback.ERROR_RESPONSE_TIMEOUT_INFO);
					}
				} else {
					if (callback != null) {
						checkCacheMode(null, code, msg);
					}
				}
			}
		} else {//请求无响应
			if (responseData.isTimeout()) {
				if (callback != null) {
					checkCacheMode(null, INetCallback.ERROR_RESPONSE_TIMEOUT,
							INetCallback.ERROR_RESPONSE_TIMEOUT_INFO);
				}
			} else {
				if (Constants.DEBUG) {
					Log.d(TAG, "request fail ==> " + getRequestResultLogString("response empty"));
				}
				if (callback != null) {
					checkCacheMode(null, INetCallback.ERROR_RESPONSE_UNKNOWN, INetCallback.ERROR_RESPONSE_UNKNOWN_INFO);
				}
			}
		}
	}

	/**
	 * 解析响应数据
	 *
	 * @param result 请求的response 内容
	 * @param callback 请求回调
	 */
	private void parseResponseBody(String result, INetCallback callback) {
		//回调为空，不向下执行
		if (callback == null) {
			return;
		}

		if (TextUtils.isEmpty(result)) {
			checkCacheMode (null, INetCallback.ERROR_RESPONSE_NULL, INetCallback.ERROR_RESPONSE_NULL_INFO);
			return;
		}

		if (callback.mType == String.class) {
			checkCacheMode (result, 0, null);
			return;
		} else {
			try {
				Gson gson = new Gson();
				Object obj = gson.fromJson(result, callback.mType);
				if (obj != null) {
					checkCacheMode (obj, 0, null);
					return;
				}
			} catch (Exception e) {
				if (Constants.DEBUG) {
					Log.e(TAG, e!=null ? e.getMessage() : "gson parse exception");
				}
				checkCacheMode (null, INetCallback.ERROR_RESPONSE_JSON_EXCEPTION, INetCallback.ERROR_RESPONSE_JSON_EXCEPTION_INFO);
			}
		}
		checkCacheMode (null, INetCallback.ERROR_RESPONSE_JSON_EXCEPTION, INetCallback.ERROR_RESPONSE_JSON_EXCEPTION_INFO);
	}
	
	/**
	 * 检查缓存模式
	 * 如果resultObj不为空，说明请求成功，否则请求失败
	 */
	private void checkCacheMode (Object resultObj, int errorCode, String msg) {
		switch (cachePolicy) {
		case POLICY_ONLY_NETWORK:
			if (resultObj != null) {//请求成功
				callback.transfer(getResponseDataWrapper(resultObj), INetCallback.TRANSFER_RESPONSE_SUCCESS_CODE);
			} else {
				callback.transfer(getResponseDataWrapper(resultObj, errorCode, msg), INetCallback.TRANSFER_RESPONSE_FAILURE_CODE);
			}
			break;
		case POLICY_ONLY_CACHED:
			if (resultObj != null) {//请求成功
				callback.transfer(getResponseDataWrapper(resultObj), INetCallback.TRANSFER_RESPONSE_SUCCESS_CODE);
			} else {
				callback.transfer(getResponseDataWrapper(resultObj, errorCode, msg), INetCallback.TRANSFER_RESPONSE_FAILURE_CODE);
			}
			break;
		case POLICY_NETWORK_ELSE_CACHED:
			if (resultObj != null) {//请求成功
				callback.transfer(getResponseDataWrapper(resultObj), INetCallback.TRANSFER_RESPONSE_SUCCESS_CODE);
			} else {
				againRequest(CachePolicyMode.POLICY_ONLY_CACHED);
			}
			break;
		case POLICY_CACHED_AND_NETWORK:
			if (resultObj != null) {//请求成功
				callback.transfer(getResponseDataWrapper(resultObj), INetCallback.TRANSFER_RESPONSE_SUCCESS_CODE);
				//延迟执行，防止关掉网络。加载过快导致回调失败的问题
				callback.getHandler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						againRequest(CachePolicyMode.POLICY_ONLY_NETWORK);
					}
				}, 200);
			} else {
				againRequest(CachePolicyMode.POLICY_ONLY_NETWORK);
			}
			break;
		}
	}
	
	/**
	 * 构造成功的ResponseDataWrapper
	 * @param resultObj
	 * @return
	 */
	public ResponseDataWrapper getResponseDataWrapper (Object resultObj) {
		ResponseDataWrapper responseDataWrapper = new ResponseDataWrapper ();
		responseDataWrapper.setResponseData(responseData);
		responseDataWrapper.setResultObj(resultObj);
		return responseDataWrapper;
	}
	
	/**
	 * 构造失败的ResponseDataWrapper
	 * @param resultObj
	 * @param errorCode
	 * @param msg
	 * @return
	 */
	public ResponseDataWrapper getResponseDataWrapper (Object resultObj, int errorCode, String msg) {
		ResponseDataWrapper responseDataWrapper = getResponseDataWrapper(resultObj);
		responseDataWrapper.setErrorCode(errorCode);
		responseDataWrapper.setMsg(msg);
		return responseDataWrapper;
	}
	
	/**
	 * 再次发起请求
	 * 主要是用于比如当读取缓存失败后，再次发起读取网络请求
	 * 或者首次读取网络失败后，然后再次发起读取缓存请求
	 */
	private void againRequest (CachePolicyMode cachePolicy) {
		this.url = this.souceUrl;
		setCachePolicyMode(cachePolicy);
        if (feture != null) {
			feture.cancel(true);
		}
        this.execuse(this.callback);
	}
	
}
