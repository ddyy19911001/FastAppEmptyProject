package com.vise.xsnow.base;


import com.vise.xsnow.http.callback.ACallback;

/**
 * 回调接口实现类，用于自定义接口时使用
 * @param <T>
 */
public  class MyCallBackListener<T> extends ACallback<T>{
    MyCallBackInterface<T> callBackInterface;

    public MyCallBackListener(MyCallBackInterface<T> callBackImp) {
        this.callBackInterface = callBackImp;
    }

    @Override
    public void onSuccess(T data) {
        callBackInterface.onRequestSuccess(data);
    }

    @Override
    public void onFail(int errCode, String errMsg) {
        callBackInterface.onRequestFail(errCode, errMsg);
    }

}
