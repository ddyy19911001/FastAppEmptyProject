package com.scwang.smartrefresh.layout.viewimpl;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cunoraz.gifview.library.GifView;
import com.scwang.smartrefresh.layout.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;


/**
 * Created by Administrator on 2019/3/25 0025.
 */
public class MyFooterView extends LinearLayout implements RefreshFooter {
    private static final String L = "MyTag";
    private  View footerRoot;
    public TextView headText;
    public View footerNoMoreLayout;
    public GifView gifView;
    public View view;
    //数据是否已经完全加载
    private boolean isFinished=false;
    public MyFooterView(Context context) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        //自定义下部显示控件
        view=View.inflate(context, R.layout.footer_animate_view,null);
        gifView=view.findViewById(R.id.gif);
        headText=view.findViewById(R.id.tv_head_text);
        footerNoMoreLayout =view.findViewById(R.id.ll_foot_no_more);
        footerRoot=view.findViewById(R.id.ll_foot_load);
        footerNoMoreLayout.setVisibility(GONE);
        Log.d("footer","隐藏footer");
        gifView.pause();//暂停动画
        gifView.setGifResource(R.drawable.load);
        headText.setVisibility(VISIBLE);
        addView(view);
    }

    public MyFooterView(Context context, View customView, GifView gifView, TextView headText, View footerText) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        //自定义下部显示控件
        this.view=customView;
        this.gifView=gifView;
        this.headText=headText;
        this.footerNoMoreLayout =footerText;
        this.footerNoMoreLayout.setVisibility(GONE);
        Log.d("footer","隐藏footer");
        this.gifView.pause();//暂停动画
        this.headText.setVisibility(GONE);
        addView(view);
    }

    @Override@Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    public MyFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }



    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }


    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        if(isFinished){
            gifView.setVisibility(GONE);
        }else {
            gifView.setVisibility(VISIBLE);
            gifView.play();//开始动画
        }
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if(isFinished){
            gifView.setVisibility(GONE);
        }else {
            gifView.setVisibility(VISIBLE);
            gifView.pause();//开始动画
        }
        if (success){
            Log.d(L,"刷新完成");
        } else {
            Log.d(L,"刷新完成");
        }
        return setLoadMoreFinishDelayTime();//延迟100毫秒之后再弹回
    }


    public int setLoadMoreFinishDelayTime() {
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                Log.i(L, "下拉开始刷新");
                break;
            case Refreshing:
                Log.i(L, "正在刷新");
                if (footerNoMoreLayout != null && footerNoMoreLayout.getVisibility() == VISIBLE) {
                    footerNoMoreLayout.setVisibility(GONE);
                    footerRoot.setVisibility(VISIBLE);
                    Log.d("footer","隐藏footer");
                }
                break;
            case ReleaseToRefresh:
                Log.i(L, "释放立即刷新");
                break;
        }
    }


    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        this.isFinished=noMoreData;
        if(noMoreData) {
            Log.d("footer","显示我有底线");
            footerNoMoreLayout.setVisibility(VISIBLE);
            footerRoot.setVisibility(GONE);
        }else{
            footerNoMoreLayout.setVisibility(GONE);
            footerRoot.setVisibility(VISIBLE);
            Log.d("footer","隐藏footer");
        }
        return false;
    }
}
