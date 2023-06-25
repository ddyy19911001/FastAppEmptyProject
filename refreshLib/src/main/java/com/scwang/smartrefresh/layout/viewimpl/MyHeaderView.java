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
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;


/**
 * Created by Administrator on 2019/3/25 0025.
 */
public class MyHeaderView extends LinearLayout implements RefreshHeader {
    private static final String L = "MyTag";
    private TextView tvHeadText;
    public GifView gifView;
    public View view;
    public MyHeaderView(Context context) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        view=View.inflate(context, R.layout.head_animate_view,null);
        gifView=view.findViewById(R.id.gif);
        tvHeadText=view.findViewById(R.id.tv_head_text);
        gifView.pause();//暂停动画
        gifView.setGifResource(R.drawable.load);
        addView(view);
    }

    public MyHeaderView(Context context,View customView,GifView gifView) {
        super(context);
        if(customView==null||gifView==null){
            throw new NullPointerException("view can not be null on create MyHeadView");
        }
        this.view=customView;
        this.gifView=gifView;
        setGravity(Gravity.CENTER_HORIZONTAL);
        gifView.pause();//暂停动画
        addView(customView);
    }

    public MyHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public void setPrimaryColors(@ColorInt int... colors) {

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
        gifView.play();//开始动画
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if (success){
            Log.d(L,"刷新完成");
        } else {
            Log.d(L,"刷新完成");
        }
        tvHeadText.setText("刷新完成");
        gifView.setVisibility(GONE);
        return setRefreshFinishDelayTime();//延迟300毫秒之后再弹回
    }

    public int setRefreshFinishDelayTime() {
        return 300;
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
                gifView.setVisibility(VISIBLE);
                gifView.pause();
                Log.i(L,"下拉刷新");
                tvHeadText.setText("下拉刷新");
                break;
            case Refreshing:
                gifView.play();
                Log.i(L,"正在刷新");
                tvHeadText.setText("正在刷新...");
                break;
            case ReleaseToRefresh:
                Log.i(L,"下拉刷新");
                tvHeadText.setText("下拉刷新");
                break;
        }
    }
}
