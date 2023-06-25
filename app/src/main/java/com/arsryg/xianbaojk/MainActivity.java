package com.arsryg.xianbaojk;

import android.os.Bundle;

import com.arsryg.xianbaojk.base.MyBaseActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import com.arsryg.xianbaojk.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends MyBaseActivity{
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
}