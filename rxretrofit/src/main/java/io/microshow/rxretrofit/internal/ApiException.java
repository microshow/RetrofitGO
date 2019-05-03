package io.microshow.rxretrofit.internal;

/**
 */
public class ApiException extends RuntimeException {

    private int error;
    private String msg;

    public ApiException(int error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
