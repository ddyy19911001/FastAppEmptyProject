package com.dy.fastframework.util;

import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/8/30.
 * deng yin
 */
//代码2
public abstract class NoDoubleClickListener implements View.OnClickListener {
    public NoDoubleClickListener(int MIN_CLICK_DELAY_TIME) {
        this.MIN_CLICK_DELAY_TIME = MIN_CLICK_DELAY_TIME;
    }

    public NoDoubleClickListener() {
    }

    public  int MIN_CLICK_DELAY_TIME = 200;
    private long lastClickTime = 0;


    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    protected abstract void onNoDoubleClick(View v);
}