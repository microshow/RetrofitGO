package io.microshow.fastokhttp;

import java.io.File;

import io.microshow.fastokhttp.callback.FileDownloadCallback;
import io.microshow.fastokhttp.callback.INetCallback;
import io.microshow.fastokhttp.core.CachePolicyMode;
import io.microshow.fastokhttp.core.Method;
import io.microshow.fastokhttp.core.RequestParams;
import io.microshow.fastokhttp.handler.FileDownloadTask;
import io.microshow.fastokhttp.handler.OkHttpTask;
import io.microshow.fastokhttp.manager.OkHttpManager;

import android.content.Context;

/**
 * 
 * FastOkhttp 入口
 * 
 * 用法：在Application里初始化以下代码:
 * 
 * 1. 第一种初始化方式(快速便捷) 推荐：
 * FastOkHttpManager.init(Context context, File cachedDir, boolean debug);
 * 
 * 2. FastOkHttpManager.doGet(xxxxxxxxxxxxx);或者 FastOkHttpManager.doRequest(xxxxxxxxxxxxx);
 * 
 * 3. 取消请求 okHttpTask.cancel(true);
 * 
 * @author zhichao.huang
 *
 */
@SuppressWarnings("rawtypes")
public class FastOkHttpManager {

	/**
	 * 在 Application 初始化 OkHttpManager 
	 * 默认请求超时时间30000ms
	 * 
	 * @param context
	 * @param cachedDir 如果传null过来的话 缓存目录默认设为 new File(context.getFilesDir(), "okhttp_net_cache_datas")
	 * @param debug 是否开启debug 日志打印，发布时需设置为false
	 * @return
	 */
	public static OkHttpManager init (Context context, File cachedDir, boolean debug) {
		return OkHttpManager.initDroidOkHttp(context, cachedDir, debug);
	}
	
	/**
	 * 统一的请求
	 * @param method
	 * @param url
	 * @param requestParams
	 * @param cachePolicy
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doRequest (Method method, String url, RequestParams requestParams
			, CachePolicyMode cachePolicy, INetCallback netCallback) {
		OkHttpTask task = new OkHttpTask();
		task.setUrl(url)
		.setRequestParams(requestParams)
		.setCachePolicyMode(cachePolicy)
		.doExecuse(method, netCallback);
		return task;
	}

	/**
	 * GET请求 带缓存配置
	 * @param url
	 * @param requestParams
	 * @param cachePolicy
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doGet (String url, RequestParams requestParams, CachePolicyMode cachePolicy, INetCallback netCallback) {
		return doRequest(Method.GET, url, requestParams, cachePolicy, netCallback);
	}
	
	/**
	 * GET请求 不带缓存配置，默认缓存只读网络
	 * @param url
	 * @param requestParams
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doGet (String url, RequestParams requestParams, INetCallback netCallback) {
		return doRequest(Method.GET, url, requestParams, null, netCallback);
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param requestParams
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doPost (String url, RequestParams requestParams, INetCallback netCallback) {
		return doRequest(Method.POST, url, requestParams, null, netCallback);
	}
	
	/**
	 * PUT请求
	 * @param url
	 * @param requestParams
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doPut (String url, RequestParams requestParams, INetCallback netCallback) {
		return doRequest(Method.PUT, url, requestParams, null, netCallback);
	}
	
	/**
	 * DELETE请求
	 * @param url
	 * @param requestParams
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doDelete (String url, RequestParams requestParams, INetCallback netCallback) {
		return doRequest(Method.DELETE, url, requestParams, null, netCallback);
	}
	
	/**
	 * Head 请求
	 * @param url
	 * @param requestParams
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doHead (String url, RequestParams requestParams, INetCallback netCallback) {
		return doRequest(Method.HEAD, url, requestParams, null, netCallback);
	}
	
	/**
	 * Patch 请求
	 * @param url
	 * @param requestParams
	 * @param netCallback
	 * @return
	 */
	public static OkHttpTask doPatch (String url, RequestParams requestParams, INetCallback netCallback) {
		return doRequest(Method.PATCH, url, requestParams, null, netCallback);
	}
	
	/**
     * 下载文件
     * @param url
     * @param target 保存的文件
     * @param callback
     */
    public static FileDownloadTask download (String url, File target, FileDownloadCallback callback) {
    	return OkHttpTask.download(url, target, callback);
    }

}
