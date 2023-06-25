package com.vise.xsnow.http.subscriber;

import android.util.Log;

import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.config.HttpGlobalConfig;
import com.vise.xsnow.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 包含回调的订阅者，如果订阅这个，上层在不使用订阅者的情况下可获得回调
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2017-01-05 09:35
 */
public class ApiCallbackSubscriber<T> extends ApiSubscriber<T> {

    HttpGlobalConfig httpGlobalConfig;
    ACallback<T> callBack;
    T data;
    private String tag;

    public ApiCallbackSubscriber(ACallback<T> callBack) {
        if (callBack == null) {
            throw new NullPointerException("this callback is null!");
        }
        httpGlobalConfig = ViseHttp.CONFIG();
        tag=httpGlobalConfig.getTag();
        this.callBack = callBack;
    }

    @Override
    public void onError(ApiException e) {
        if (e == null) {
            callBack.onFail(-1, "This ApiException is Null.");
            return;
        }
        Log.i(tag, "完成时间："+format.format(new Date()));
        Log.e(tag, "发生错误：错误码"+e.getCode());
        Log.e(tag, "错误原因："+e.getMessage());
        Log.e(tag, e.getCause()+"");
        httpGlobalConfig.onHttpErro(e);
        callBack.onFail(e.getCode(), e.getMessage());
    }

    @Override
    public void onNext(T t) {
        this.data = t;
        callBack.onSuccess(t);
    }


    SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
    @Override
    public void onComplete() {
        Log.i(tag, "完成时间："+format.format(new Date()));
    }

    public T getData() {
        return data;
    }
}
