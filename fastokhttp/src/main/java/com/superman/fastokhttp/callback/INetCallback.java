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

package com.superman.fastokhttp.callback;

import java.lang.reflect.Type;

import com.superman.fastokhttp.core.ClassTypeReflect;
import com.superman.fastokhttp.core.Constants;
import com.superman.fastokhttp.core.ResponseDataWrapper;

import android.os.Handler;
import android.os.Message;

/**
 * 网络请求回调类
 * @author zhichao.huang
 *
 * @param <T>
 */
public class INetCallback <T> {

	public static final int ERROR_RESPONSE_NULL = 1001;//响应为空
	public static final int ERROR_RESPONSE_JSON_EXCEPTION = 1002;//json解析异常
	public static final int ERROR_RESPONSE_UNKNOWN = 1003;//请求错误未知
	public static final int ERROR_RESPONSE_TIMEOUT = 1004;//请求超时

	public static final String ERROR_RESPONSE_NULL_INFO = "error_response_null";
	public static final String ERROR_RESPONSE_JSON_EXCEPTION_INFO = "error_response_json_exception";
	public static final String ERROR_RESPONSE_UNKNOWN_INFO = "error_response_unknown";
	public static final String ERROR_RESPONSE_TIMEOUT_INFO = "error_response_timeout";
	
	/**
	 * 请求数据成功
	 */
	public static final int TRANSFER_RESPONSE_SUCCESS_CODE = -200;
	/**
	 * 请求数据失败
	 */
	public static final int TRANSFER_RESPONSE_FAILURE_CODE = -201;
	/**
	 * 请求数据进度
	 */
	public static final int TRANSFER_RESPONSE_PROGRESS_CODE = -202;

	/**
	 * 网络响应数据包裹
	 */
	ResponseDataWrapper response;
	
	public Type mType;

	public INetCallback() {
		mType = ClassTypeReflect.getModelClazz(getClass());
	}
	
	/**
	 * handler 调度
	 */
	static Handler handler = new Handler(){
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void handleMessage(Message msg) {
			
			INetCallback netCallback = (INetCallback)msg.obj;
			ResponseDataWrapper response = netCallback.response;
			
			switch (msg.what) {
			case TRANSFER_RESPONSE_SUCCESS_CODE://成功
				try {
					netCallback.onSuccess(response, response.resultObj);
				} catch (Exception e) {
					if (!Constants.net_error_try) {
						throw new RuntimeException(e);
					}
				}
				break;
				
			case TRANSFER_RESPONSE_FAILURE_CODE://失败
				try {
					netCallback.onFailure(response.errorCode, response.getMsg());
				} catch (Exception e) {
					if (!Constants.net_error_try) {
						throw new RuntimeException(e);
					}
				}
				break;
				
			case TRANSFER_RESPONSE_PROGRESS_CODE://刷新进度
				try {
					netCallback.onProgress(response.getProgress(), response.getNetworkSpeed(), response.isDone());
				} catch (Exception e) {
					if (!Constants.net_error_try) {
						throw new RuntimeException(e);
					}
				}
				break;

			default:
				try {
					netCallback.onSuccess(response, response.resultObj);
				} catch (Exception e) {
					if (!Constants.net_error_try) {
						throw new RuntimeException(e);
					}
				}
				break;
			}
			
		}
	};
	
	
	/**
	 * 线程传递 将数据由后台线程传到前台 <br/>
	 * 自己调用时不可 what 必须>0 防止和系统自定的冲突
	 */
	public void transfer(ResponseDataWrapper response, Integer what) {
		Message msg = handler.obtainMessage();
		this.response = response;
		msg.what = what;
		msg.obj = this;
		handler.sendMessage(msg);
	}
	
	public Handler getHandler () {
		return handler;
	}
	
	/**
	 * 开始请求网络
	 */
	public void onStart() {
	}

	/**
	 * 数据请求成功
	 * @param response 原始数据
	 * @param t json转的object
	 */
	public void onSuccess(ResponseDataWrapper response, T t) {
	}

	/**
	 * 上传文件进度
	 * @param progress
	 * @param networkSpeed 网速
	 * @param done
	 */
	public void onProgress(int progress, long networkSpeed, boolean done) {
	}

	/**
	 * 数据请求失败
	 * @param errorCode
	 * @param msg
	 */
	public void onFailure(int errorCode, String msg) {
	}
	
	/**
	 * 任务取消时运行
	 */
	public void onCancelled() {
	}

}
