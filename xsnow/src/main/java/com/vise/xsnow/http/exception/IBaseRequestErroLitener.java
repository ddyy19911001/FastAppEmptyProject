package com.vise.xsnow.http.exception;

/**
 * 请求错误监听类，所有请求出现的错误将在这里执行
 */
public interface IBaseRequestErroLitener {
    void onHttpErro(ApiException e);
}
