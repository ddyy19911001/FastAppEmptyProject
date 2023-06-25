package com.dy.fastframework.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/4/24.
 * 双击
 */

public class OnDoubleClickListener implements View.OnTouchListener{

    public int count = 0;//点击次数
    public long firstClick = 0;//第一次点击时间
    public long secondClick = 0;//第二次点击时间
    private boolean isSingleTouchListener;
    /**
     * 两次点击时间间隔，单位毫秒
     */
    private final int totalTime = 1000;
    /**
     * 自定义回调接口
     */
    private DoubleClickCallback mCallback;

    public void clear(){
        firstClick=0;
        secondClick=0;
        count=0;
    }

    public interface DoubleClickCallback {
        void onDoubleClick();
        void onSingleClick();
    }
    public OnDoubleClickListener(boolean isSingleTouchListener,DoubleClickCallback callback) {
        super();
        this.isSingleTouchListener=isSingleTouchListener;
        this.mCallback = callback;
    }

    /**
     * 触摸事件处理
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {//按下
            count++;
            if (1 == count) {
                firstClick = System.currentTimeMillis();//记录第一次点击时间
                if (mCallback != null&&isSingleTouchListener) {
                    mCallback.onSingleClick();
                    clear();
                }
            } else if (2 == count) {
                secondClick = System.currentTimeMillis();//记录第二次点击时间
                if (secondClick - firstClick < totalTime) {//判断二次点击时间间隔是否在设定的间隔时间之内
                    if (mCallback != null) {
                        mCallback.onDoubleClick();
                    }
                    count = 0;
                    firstClick = 0;
                } else {
                    firstClick = secondClick;
                    count = 1;
                }
                secondClick = 0;
            }
        }
        return true;
    }
}
