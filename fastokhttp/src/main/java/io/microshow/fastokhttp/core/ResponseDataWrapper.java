package io.microshow.fastokhttp.core;

/**
 * 响应数据包裹
 * 
 * @author zhichao.huang
 *
 */
public class ResponseDataWrapper {
	
	/**
	 * 原始网络结果数据封装
	 */
	public ResponseData responseData;
	
	/**
	 * json映射的object
	 */
	public Object resultObj;
	
	/**
	 * 自定义的errorCode
	 */
	public int errorCode;
	
	/**
	 * 自定义的msg
	 */
	public String msg;
	
	//上传相关
	/**
	 * 进度
	 */
	int progress;
	/**
	 * 网速
	 */
	long networkSpeed;
	/**
	 * 是否传输成功
	 */
	boolean done;
	
	/**
	 * 是否是缓存数据；true:是；false:不是
	 * @return
	 */
	public boolean isCacheData () {
		return responseData.isCacheData();
	}

	public ResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(ResponseData responseData) {
		this.responseData = responseData;
	}

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public long getNetworkSpeed() {
		return networkSpeed;
	}

	public void setNetworkSpeed(long networkSpeed) {
		this.networkSpeed = networkSpeed;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
}
