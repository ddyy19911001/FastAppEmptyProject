package com.vise.xsnow.base;

public interface MyCallBackInterface<T> {
     void onRequestSuccess(T data);
     void onRequestFail(int errCode, String errMsg);
}
