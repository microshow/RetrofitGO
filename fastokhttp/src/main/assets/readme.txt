################################# 版本记录  #######################################
1）DroidOkHttp 1.0
基于okhttp-2.7.0.jar、okio-1.6.0.jar

2）DroidOkHttp 1.3 
基于okhttp-3.3.1.jar、okio-1.8.0.jar深度封装的网络请求框架

- 支持缓存模式（1.只读网络；2.只读缓存；3.先读网络，网络没有则读缓存； 
             4.先读缓存，不管是否有缓存数据，都会再读网络然后响应调用端《推荐》）；
- 同时支持上传、下载进度回调； 上传文件支持File、byte[]
- 各种请求方式（post,get,delete ......） 
- AES数据加解密等

3）DroidOkHttp 2.1.7 (2017-07-01)
-基于okhttp-3.8.1.jar、okio-1.13.0.jar深度封装的网络请求框架
-重构项目结构
-重构异步加载，替换之前的AsyncTask,改为线程池+Handler的请求机制，并回调刷新UI线程

4）DroidOkHttp 2.1.8 (2017-07-04)
- DroidOkHttp 项目名称 重新命名为 fastokhttp

################################# 用法 #######################################
/**
 * 在Application里初始化以下代码:
 *
 * 一）初始化
 * 1.第一种初始化方式【推荐】
 * FastOkHttpManager.init(Context context, File cachedDir, boolean debug);
 * 
 * 2.第二种初始化方式(更多自定义配置项)：
 * private void initOKHttpConfig () {
		Map<String, String> commonParamMap = new HashMap<String, String>();
        Map<String, String> commonHeaderMap = new HashMap<String, String>();

        OkHttpManager okHttpManager = new OkHttpManager.Builder()
                .setCommenParams(commonParamMap)
                .setCommenHeader(commonHeaderMap)
                .setTimeout(Constants.REQ_TIMEOUT)
                .setDebug(false)
                .cachedDir(new File(getCacheDir(), "_data"))//缓存目录 (必须的配置)
                //.setCertificates(...)
                //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())

        .build();
        okHttpManager.init();
	}
	二）调用请求
	第一种方式： FastOkHttpManager.doGet(xxxxxxxxxxxxx);或者 FastOkHttpManager.doRequest(xxxxxxxxxxxxx);
	第二种方式：
	    OkHttpTask okHttpTask = new OkHttpTask();
    	okHttpTask.setUrl(url);
    	okHttpTask.setCachePolicyMode(CachePolicyMode.POLICY_CACHED_AND_NETWORK);
    	okHttpTask.doGet(new INetCallback<String>(){

			@Override
			public void onSuccess(ResponseDataWrapper response, String t) {
				super.onSuccess(response, t);
				dataStr += "\n\n onSuccess isCacheData="+response.isCacheData()+" > datas:"+t ;
				data.setText(dataStr);
			}

			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				dataStr += "\n\n onFailure:"+msg ;
				data.setText(dataStr);
			}
    		
    	});
    	
         三） 取消请求
    okHttpTask.cancel(true);
 */







