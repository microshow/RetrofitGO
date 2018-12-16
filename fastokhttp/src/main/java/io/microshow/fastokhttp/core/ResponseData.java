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

package io.microshow.fastokhttp.core;

import okhttp3.Headers;

/**
 * 
 * http响应数据封装
 * 
 * @author zhichao.huang
 *
 */
public class ResponseData {

	/**
	 * http是否无响应
	 */
    private boolean responseNull;
    
    /**
	 * 是否请求超时
	 */
    private boolean timeout;

    /**
	 * http code
	 */
    private int code;
    
    /**
	 * http响应消息
	 */
    private String message;
    
    /**
	 * http响应结果
	 */
    private String response;
    
    /**
	 * 是否成功
	 */
    private boolean success;
    
    /**
     * http headers
     */
    private Headers headers;
    
    /**
     * true:当前数据是缓存数据；false：当前数据不是缓存数据 
     */
    private boolean isCacheData;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public boolean isResponseNull() {
        return responseNull;
    }

    public void setResponseNull(boolean responseNull) {
        this.responseNull = responseNull;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

	public boolean isCacheData() {
		return isCacheData;
	}

	public void setCacheData(boolean isCacheData) {
		this.isCacheData = isCacheData;
	}
	
}
