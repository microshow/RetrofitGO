package io.microshow.retrofitgo.internal;

import android.support.annotation.StringRes;

import io.microshow.retrofitgo.RetrofitClient;

public final class ErrorHelper {

  public static final int SUCCESS = 200;
  public static final int UNKNOWN = 8001000;        //未知错误 如未知HOST(断网情况)
  public static final int PARSE_ERROR = 8001001;    //解析错误
  public static final int NETWORK_ERROR = 8001002;  //网络错误
  public static final int HTTP_ERROR = 8001003;     //协议出错
  public static final int SSL_ERROR = 8001005;      //证书出错/握手失败
  public static final int TIMEOUT_ERROR = 8001006;  //连接超时
  public static final int INVOKE_ERROR = 8001007;   //调用错误
  public static final int CONVERT_ERROR = 8001008;  //类转换错误
  public static final int SERVER_ERROR = 8001009;
  public static final int EOS_ERROR = 8001010;//eos rpc接口的错误回调

  public static final int TOKEN_EXPIRED = 4;

  public static final String ERROR_MSG = "REQUEST_ERROR";
  public static final String NET_ERROR_MSG = "NETWORK_ERROR";
  public static final String PARSE_ERROR_MSG = "JSON_PARSE_ERROR";
  public static final String UNKNOWN_ERROR_MSG = "UNKNOWN";
  public static final String SSL_ERROR_MSG = "SSL_ERROR";

  public static final String COMMON_ERROR = "系统出现错误，请稍后重试";


  public static Error empty() {
    return new Error(SUCCESS, "");
  }

  public static Error netError() {
    return new Error(NETWORK_ERROR, COMMON_ERROR);
  }

  public static Error httpError() {
    return new Error(HTTP_ERROR, COMMON_ERROR);
  }

  public static Error sslError() {
    return new Error(SSL_ERROR, COMMON_ERROR);
  }

  public static Error parseError() {
    return new Error(PARSE_ERROR, COMMON_ERROR);
  }

  public static Error castError() {
    return new Error(CONVERT_ERROR, COMMON_ERROR);
  }

  public static Error timeout() {
    return new Error(TIMEOUT_ERROR, COMMON_ERROR);
  }

  public static Error unKnowError() {
    return new Error(UNKNOWN, COMMON_ERROR);
  }

  public static Error apiError(int code, String message) {
    return new Error(code, code + ": " + message);
  }

  private static String getString(@StringRes int resId) {
    return RetrofitClient.get().getContext().getString(resId);
  }

}
