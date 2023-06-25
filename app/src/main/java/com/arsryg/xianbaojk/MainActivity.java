package com.arsryg.xianbaojk;

import android.os.Bundle;

import com.arsryg.xianbaojk.base.MyBaseDataBindingActivity;
import com.arsryg.xianbaojk.model.MainModel;
import com.arsryg.xianbaojk.databinding.ActivityMainBinding;


public class MainActivity extends MyBaseDataBindingActivity<MainModel,ActivityMainBinding> {
    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initFirst() {
        setActivityTitle(getResString(R.string.xian_bao));
    }

    @Override
    public boolean setIsExitActivity() {
        return true;
    }

    @Override
    protected MainModel createViewModel() {
        return new MainModel(this,binding);
    }
}