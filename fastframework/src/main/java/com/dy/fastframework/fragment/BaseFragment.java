package com.dy.fastframework.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dy.fastframework.R;
import com.dy.fastframework.view.CommonMsgDialog;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends SuperBaseFragment{
    private CommonMsgDialog commonMsgDialog;
    public boolean isFirstGetData=true;
    public Dialog mLoadingDialog;

    public void showMsgDialog(String msg){
        commonMsgDialog=getCommonMsgDialog();
        commonMsgDialog.showMsg(msg);
    }

    private CommonMsgDialog getCommonMsgDialog() {
        if(commonMsgDialog==null) {
            commonMsgDialog = new CommonMsgDialog(getActivity());
        }
        return commonMsgDialog;
    }


    public void onRestart(){

    }



    /**
     * 获取颜色资源
     * @param res
     * @return
     */
    public int getResColor(int res){
        if(getActivity()==null){
            return 0;
        }
        return getResources().getColor(res);
    }


    /**
     * 获取颜色资源
     * @param res
     * @return
     */
    public String getResString(int res){
        if(getActivity()==null){
            return "";
        }
        return getResources().getString(res);
    }





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (mRootView != null) {
            return mRootView;
        }
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        bindViewWithId(mRootView);
        mViewInflateFinished = true;
        init();
        return mRootView;
    }


    @Override
    public void onDestroyView() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mViewInflateFinished&&isVisibleToUser&&isFirstGetData){
            isFirstGetData=false;
            onFirstVisibleToGetData();
        }
    }




    //被隐藏的fragment才能调用这个方法在首次打开时进行获取数据，已执行过bindviewwithid
    public void onFirstVisibleToGetData() {

    }


    /**
     * 显示转圈的Dialog
     *
     * @return
     */
    public Dialog showLoadingDialog(String msg, boolean canDismiss) {
        closeDialog();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(getActivity(), R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(canDismiss); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        this.mLoadingDialog = loadingDialog;
        return loadingDialog;
    }


    /**
     * 关闭dialog
     */
    public void closeDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            this.mLoadingDialog.dismiss();
            this.mLoadingDialog = null;
        }
    }


    @Override
    public void onDestroy() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


}
