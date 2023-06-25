package com.vise.xsnow.http.exception;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.vise.xsnow.R;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.mode.ApiCode;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * @Description: API异常统一管理
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-12-30 17:59
 */
public class ApiException extends Exception {

    private final int code;
    private String message;
    private String cause;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
        this.cause=String.valueOf(throwable.getCause());
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ApiException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDisplayMessage() {
        return message + "(code:" + code + ")";
    }

    public static ApiException handleException(Throwable e) {
        Log.e(ViseHttp.CONFIG().getTag(), e.getMessage()+"\ncause:"+e.getCause());
        ViseHttp.CONFIG().onErroRemoveAllTimer();
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ApiCode.Request.HTTP_ERROR);
            switch (httpException.code()) {
                case ApiCode.Http.UNAUTHORIZED:
                case ApiCode.Http.FORBIDDEN:
                case ApiCode.Http.NOT_FOUND:
                case ApiCode.Http.REQUEST_TIMEOUT:
                case ApiCode.Http.GATEWAY_TIMEOUT:
                case ApiCode.Http.INTERNAL_SERVER_ERROR:
                case ApiCode.Http.BAD_GATEWAY:
                case ApiCode.Http.SERVICE_UNAVAILABLE:
                default:
                    ex.message = ViseHttp.getStr(R.string.NETWORK_ERROR);
                    ex.cause=String.valueOf(httpException.getCause());
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ex = new ApiException(e, ApiCode.Request.PARSE_ERROR);
            ex.message =ViseHttp.getStr(R.string.PARSE_ERROR);
            ex.cause=String.valueOf(e.getCause());
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ApiCode.Request.NETWORK_ERROR);
            ex.message =ViseHttp.getStr(R.string.NETWORK_ERROR);
            ex.cause=String.valueOf(e.getCause());
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, ApiCode.Request.SSL_ERROR);
            ex.message = ViseHttp.getStr(R.string.SSL_ERROR);
            ex.cause=String.valueOf(e.getCause());
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ApiCode.Request.TIMEOUT_ERROR);
            ex.message = ViseHttp.getStr(R.string.TIMEOUT_ERROR);
            ex.cause=String.valueOf(e.getCause());
            return ex;
        } else {
            ex = new ApiException(e, ApiCode.Request.UNKNOWN);
            ex.message =  ViseHttp.getStr(R.string.UNKNOWN_ERRO);
            ex.cause=String.valueOf(e.getCause());
            return ex;
        }
    }

}
