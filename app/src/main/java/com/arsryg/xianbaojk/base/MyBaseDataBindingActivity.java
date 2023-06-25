package com.arsryg.xianbaojk.base;


import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.dy.fastframework.activity.BaseActivity;
import com.dy.fastframework.application.SuperBaseApp;
import com.dy.fastframework.util.SharedPreferenceUtil;
import com.dy.fastframework.util.bean.NetErroInfo;



/**
 * 复制此Activity到自己的基类中
 * 默认顶到状态栏的
 * @param <T>
 */
public abstract class MyBaseDataBindingActivity<V extends MyBaseViewModel<T>,T extends ViewDataBinding> extends BaseActivity {
    public T binding;
    public V viewModel;



    public boolean isMeBackGround;
    @Override
    public void onStart() {
        isMeBackGround=false;
        super.onStart();
    }

    @Override
    public void onRestart() {
        isMeBackGround=false;
        super.onRestart();
    }

    @Override
    public void onPause() {
        isMeBackGround=true;
        super.onPause();
    }


    @Override
    public void showTs(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLayoutVoid(int setLayout) {
        binding = DataBindingUtil.setContentView(this,setLayout);
        viewModel=createViewModel();
    }

    protected abstract V createViewModel();

    public void dealWithNetErr(NetErroInfo netErroInfo) {
        closeDialog();
        if(viewModel!=null){
            viewModel.onNetErrShowNormal();
        }
    }


    public SharedPreferenceUtil getSpUtil(){
        return SuperBaseApp.getSharedPreferenceUtil();
    }





    @Override
    public void closeDialog() {
        super.closeDialog();
        if(viewModel!=null){
            viewModel.closeDialog();
        }
    }



}
