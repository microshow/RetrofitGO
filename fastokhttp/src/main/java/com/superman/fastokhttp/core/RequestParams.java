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

package com.superman.fastokhttp.core;

import android.text.TextUtils;
import android.util.Log;
import okhttp3.FormBody;
//import okhttp3.FormEncodingBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
//import okhttp3.MultipartBuilder;
import okhttp3.RequestBody;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.superman.fastokhttp.manager.OkHttpManager;

/**
 * Http请求参数类
 * @author zhichao.huang
 *
 */
public class RequestParams {

    public ConcurrentHashMap<String, String> headerMap;

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;//待上传的File
    protected ConcurrentHashMap<String, byte[]> byteParams;//待上传的file - byte[]

    private JSONObject jsonBody;
    private RequestBody requestBody;

    public RequestParams() {
    	init();
    }

    private void init() {
        headerMap = new ConcurrentHashMap<String, String>();
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
        byteParams = new ConcurrentHashMap<String, byte[]>();
        
        headerMap.put("charset", "UTF-8");


        //添加公共参数
        Map<String, String> commonParams = OkHttpManager.getOkHttpFinal().getCommonParams();
        if ( commonParams != null && commonParams.size() > 0 ) {
            urlParams.putAll(commonParams);
        }

        //添加公共header
        Map<String, String> commonHeader = OkHttpManager.getOkHttpFinal().getCommonHeaderMap();
        if ( commonHeader != null && commonHeader.size() > 0 ) {
            headerMap.putAll(commonHeader);
        }

    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if ( value == null ) {
            value = "";
        }

        if (!TextUtils.isEmpty(key)) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, int value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, double value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, boolean value) {
        put(key, String.valueOf(value));
    }
    
    public void put(String key, byte[] data) {
        if (!TextUtils.isEmpty(key) && data != null) {
        	byteParams.put(key, data);
        }
    }

    /**
     * @param key
     * @param file
     */
    public void put(String key, File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }

        boolean isPng = file.getName().lastIndexOf("png") > 0 || file.getName().lastIndexOf("PNG") > 0;
        if (isPng) {
            put(key, file, MediaTypeConstant.MEDIA_TYPE_IMAGE_PNG);
        }

        boolean isJpg = file.getName().lastIndexOf("jpg") > 0 || file.getName().lastIndexOf("JPG") > 0;
        if (isJpg) {
            put(key, file, MediaTypeConstant.MEDIA_TYPE_IMAGE_JPEG);
        }

        if (!isPng && !isJpg) {
            put(key, new FileWrapper(file, null));
        }
    }

    public void put(String key, File file, String contentType) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }

        MediaType mediaType = null;
        try {
            mediaType = MediaType.parse(contentType);
        } catch (Exception e){
            Log.e("RequestParams",e.getMessage()+"");
        }

        put(key, new FileWrapper(file, mediaType));
    }

    public void put(String key, File file, MediaType mediaType) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }

        put(key, new FileWrapper(file, mediaType));
    }

    public void put(String key, FileWrapper fileWrapper) {
        if (!TextUtils.isEmpty(key) && fileWrapper != null) {
            fileParams.put(key, fileWrapper);
        }
    }
    
    /**
     * 上传单个byte[]文件流，没有key
     * @param data
     */
    public void put(byte[] data) {
    	if (data != null) {
    		setCustomRequestBody(RequestBody.create(MediaType.parse(MediaTypeConstant.MEDIA_TYPE_FILE_OR_BYTE), data));
    	}
    }
    
    /**
     * 上传单个File文件流，没有key
     * @param data
     */
    public void put(File file) {
    	if (file != null) {
    		setCustomRequestBody(RequestBody.create(MediaType.parse(MediaTypeConstant.MEDIA_TYPE_FILE_OR_BYTE), file));
    	}
    }

    public void putAll(Map<String, String> params) {
        if ( params != null && params.size() > 0 ) {
            urlParams.putAll(params);
        }
    }

    public void putHeader(String key, String value) {
        if ( value == null ) {
            value = "";
        }

        if (!TextUtils.isEmpty(key)) {
            headerMap.put(key, value);
        }
    }

    public void putHeader(String key, int value) {
        putHeader(key, String.valueOf(value));
    }

    public void putHeader(String key, float value) {
        putHeader(key, String.valueOf(value));
    }

    public void putHeader(String key, double value) {
        putHeader(key, String.valueOf(value));
    }

    public void putHeader(String key, boolean value) {
        putHeader(key, String.valueOf(value));
    }

    public void clearMap() {
        urlParams.clear();
        fileParams.clear();
        byteParams.clear();
    }

    public void setJSONObject(JSONObject jsonBody) {
        this.jsonBody = jsonBody;
    }

    public void setCustomRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    public RequestBody getRequestBody() {
        RequestBody body = null;
        if (jsonBody != null) {
            body = RequestBody.create(MediaType.parse(MediaTypeConstant.MEDIA_TYPE_JSON), jsonBody.toString());
        } else if (requestBody != null) {
            body = requestBody;
        } else if (fileParams.size() > 0 || byteParams.size() > 0) {
            boolean hasData = false;
            okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
                hasData = true;
            }

            for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                FileWrapper file = entry.getValue();
                if (file != null) {
                    hasData = true;
                    builder.addFormDataPart(entry.getKey(), file.getFileName(), RequestBody.create(file.getMediaType(), file.getFile()));
                }
            }
            //构造byte[] 上传参数
            for (ConcurrentHashMap.Entry<String, byte[]> entry : byteParams.entrySet()) {
            	byte[] data = entry.getValue();
                if (data != null) {
                    hasData = true;
                    builder.addFormDataPart(entry.getKey(), "bytedata", RequestBody.create(MediaType.parse(MediaTypeConstant.MEDIA_TYPE_FILE_OR_BYTE), data));
                }
            }
            
            if (hasData) {
                body = builder.build();
            }
        } else {
        	okhttp3.FormBody.Builder builder = new FormBody.Builder();
            boolean hasData = false;
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
                hasData = true;
            }
            if (hasData) {
                body = builder.build();
            }
        }

        return body;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }
        
        if (jsonBody != null) {
        	result.append("jsonBody ["+jsonBody.toString()+"]");
        }

        return result.toString();
    }
}
