# fastokhttp
快速、便捷的okhttp，基于okhttp-3.8.1.jar、okio-1.13.0.jar深度封装的网络请求框架

## 必杀技
* 支持GET、POST、PUT、PATCH、HEAD、DELETE等请求协议
* 支持Json的提交，及自定义请求数据类型
* 支持带进度显示的多文件上传及下载回调
* 完美的Http缓存模式:
  - 只读网络
  - 只读缓存
  - 先读网络，网络没有则读缓存
  - 先读缓存，不管是否有缓存数据，都会再读网络然后响应调用端,共回调两次【推荐】
* 支持Https、自签名网站Https的访问、双向验证
* 支持失败重试机制，支持请求优先级
* 异步请求支持多个请求并发
* 支持异步取消

## 使用方法
### Gradle方式
后续支持Gradle

### jar包导入
[点击下载](/versions)

添加okhttp版本：

api 'com.squareup.okhttp3:okhttp:3.12.0'

api 'com.squareup.okio:okio:2.1.0'

## 初始化 建议在Application里初始化
fastokhttp初始化时分两种情况

### 一般初始化(推荐)
直接初始化后，一切采用默认设置。
```java
FastOkHttpManager.init(Context context, File cachedDir, boolean debug);
```

### 高级初始化
```java
private void initOKHttpConfig () {
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
```

## 需要的权限
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```

## 调用请求 
```java
//第一种方式【推荐】
FastOkHttpManager.doGet(xxxxxxxxxxxxx);或
FastOkHttpManager.doRequest(xxxxxxxxxxxxx);

//第二种方式
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
...
// 取消请求
okHttpTask.cancel(true);
```

## 代码混淆
```text
// fastokhttp
-dontwarn com.superman.fastokhttp.**
-keep class com.superman.fastokhttp.**{*;}

// okhttp
-dontwarn okhttp3.**
-keep class okhttp3.** {*;} 
-dontwarn okio.**
-keep class okio.** {*;} 
```

## License
```text
Copyright 2017 Super

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
