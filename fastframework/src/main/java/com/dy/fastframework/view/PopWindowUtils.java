package com.dy.fastframework.view;

import android.app.Activity;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;


/**
 * Created by Administrator on 2017/10/12.
 * deng yin
 * 先创建，在createLayout，最后show
 */
public abstract class PopWindowUtils {
    public Activity mContext;
    public PopupWindow popupWindow;
    public View contentView;

    public PopWindowUtils(Activity mContext) {
        this.mContext = mContext;
    }


    public View getContentView() {
        return contentView;
    }

    public void createPopupLayout(int layoutRes,int width,int height){
        createPopupLayout(layoutRes,width,height,true);
    }

    public void createPopupLayout(int layoutRes,int height){
        createPopupLayout(layoutRes,ViewGroup.LayoutParams.MATCH_PARENT,height,true);
    }

    public void createPopupLayout(int layoutRes){
        createPopupLayout(layoutRes,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
    }

    public void createPopupLayout(int layoutRes,int width,int height,boolean isHalfBlack) {
        // 一个自定义的布局，作为显示的内容
        contentView = LayoutInflater.from(mContext).inflate(
                layoutRes, null);
        createViewHolder(contentView);
        popupWindow = new PopupWindow(contentView,
                width, height, true);
        popupWindow.setTouchable(true);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(null);

        // 设置好参数之后再show
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                dismissPop();
            }
        });
        if(isHalfBlack) {
            backgroundAlpha(0.5f);
        }else{
            backgroundAlpha(1f);
        }
    }


    public abstract void createViewHolder(View contentView);

    public void showDropDown(View view,int xOffset,int yOffset,int Gravity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&popupWindow!=null) {
            popupWindow.showAsDropDown(view, xOffset, yOffset, Gravity);
        }
    }

    public void showBottom(View rootView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&popupWindow!=null) {
            popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }

    public void showCenter(View rootView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&popupWindow!=null) {
            popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }


    public void dismissPop() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
