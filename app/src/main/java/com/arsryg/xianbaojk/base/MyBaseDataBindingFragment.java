package com.arsryg.xianbaojk.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.dy.fastframework.application.SuperBaseApp;
import com.dy.fastframework.fragment.BaseFragment;
import com.dy.fastframework.util.SharedPreferenceUtil;
import com.dy.fastframework.util.bean.NetErroInfo;
import com.dy.fastframework.util.bean.ServerErrInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



/**
 * 复制此Fragment到自己的基类中
 * @param <T>
 */
public abstract class MyBaseDataBindingFragment<V extends MyBaseViewModel<T>,T extends ViewDataBinding> extends BaseFragment{
    public T binding;
    public V viewModel;


    @Override
    public void showTs(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    protected abstract V createViewModel();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (mRootView != null) {
            return mRootView;
        }
        binding = DataBindingUtil.inflate(inflater, setContentView(), container, false);
        viewModel=createViewModel();
        mRootView = binding.getRoot();
        bindViewWithId(mRootView);
        mViewInflateFinished = true;
        init();
        return mRootView;
    }

    @Override
    public void closeDialog() {
        super.closeDialog();
        if(viewModel!=null){
            viewModel.closeDialog();
        }
    }





    public SharedPreferenceUtil getSpUtil(){
        return SuperBaseApp.getSharedPreferenceUtil();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealWithNetErr(NetErroInfo netErroInfo) {
        closeDialog();
        if(viewModel!=null){
            viewModel.onNetErrShowNormal();
        }
    }
}
