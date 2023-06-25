package com.dy.fastframework.util.bean;

import java.io.Serializable;

public class ServerErrInfo implements Serializable {
    public int errCode;
    public String errMsg;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
