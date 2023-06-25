package com.arsryg.xianbaojk.base;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.dy.fastframework.activity.BaseWebViewActivity;
import com.dy.fastframework.application.SuperBaseApp;
import com.dy.fastframework.util.SharedPreferenceUtil;
import com.dy.fastframework.util.bean.NetErroInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



/**
 * 复制此Activity到自己的基类中
 * @param <T>
 */
public abstract class MyBaseWebDataBindingActivity<V extends MyBaseViewModel<T>,T extends ViewDataBinding> extends BaseWebViewActivity  {
    public T binding;
    public V viewModel;


    @Override
    public void showTs(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealWithNetErr(NetErroInfo netErroInfo) {
        closeDialog();
        if(viewModel!=null){
            viewModel.onNetErrShowNormal();
        }
    }





    @Override
    public void setLayoutVoid(int setLayout) {
        binding = DataBindingUtil.setContentView(this,setLayout);
        viewModel=createViewModel();
    }

    protected abstract V createViewModel();





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

    public void showEmptyLayout(){
        if(viewModel!=null&&viewModel.loadingManager!=null){
            viewModel.loadingManager.showEmptyLayout();
        }
    }


}
