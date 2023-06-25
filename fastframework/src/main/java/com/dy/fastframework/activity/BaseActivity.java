package com.dy.fastframework.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.dy.fastframework.R;
import com.dy.fastframework.util.AppActivityListManager;
import com.dy.fastframework.util.KeyboardsUtils;
import com.dy.fastframework.util.LogUtils;
import com.dy.fastframework.util.MyUtils;
import com.dy.fastframework.util.bean.NetErroInfo;
import com.dy.fastframework.util.bean.ServerErrInfo;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;


public abstract class BaseActivity extends SuperBaseActivity {
    public long lastDoTime;
    //服务器异常
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerErr(ServerErrInfo serverErrInfo){
        if(System.currentTimeMillis()-lastDoTime>500){
            closeDialog();
            dealWithServerErr(serverErrInfo);
            lastDoTime=System.currentTimeMillis();
        }

    }



    public void set(String lauType) {
        // 本地语言设置
        Locale myLocale = new Locale(lauType);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (KeyboardsUtils.isShouldHideKeyBord(v, me)) { //判断用户点击的是否是输入框以外的区域
                KeyboardsUtils.hintKeyBoards(v);   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 按需处理服务器异常
     * @param serverErrInfo
     */
    public void dealWithServerErr(ServerErrInfo serverErrInfo) {

    }


    //网络异常情况
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetErr(NetErroInfo netErroInfo){
        closeDialog();
        Activity currentActivity = AppActivityListManager.getScreenManager().currentActivity();
        if(this!=currentActivity){
            return;
        }
        if(!MyUtils.isEmpty(netErroInfo.errMsg)){
            showTs(netErroInfo.getErrMsg());
        }else{
            LogUtils.e("网络异常！！！！！");
        }
    }


    /**
     * 请求存储相关权限
     * @param onPermissionCallback
     */
    public void requestStoragePermission(OnPermissionCallback onPermissionCallback){
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        onPermissionCallback.onGranted(permissions,all);
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                         if(never){
                             showTs(getResString(R.string.no_read_write_permission_and_open));
                             XXPermissions.startPermissionActivity(BaseActivity.this, permissions);
                         }else{
                             showTs(getResString(R.string.no_read_write_permission));
                         }
                    }
                });
    }


    /**
     * 请求相机相关权限
     * @param onPermissionCallback
     */
    public void requestCameraPermission(OnPermissionCallback onPermissionCallback){
        XXPermissions.with(this)
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        onPermissionCallback.onGranted(permissions,all);
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if(never){
                            showTs(getResString(R.string.no_camera_and_open));
                            XXPermissions.startPermissionActivity(BaseActivity.this, permissions);
                        }else{
                            showTs(getResString(R.string.no_camera));
                        }
                    }
                });
    }


    /**
     * 请求定位相关权限
     * 首次传0开始
     * @param onPermissionCallback
     */
    public void requestLocationPermission(int step,OnPermissionCallback onPermissionCallback){
        String currentPermission=Permission.ACCESS_COARSE_LOCATION;
        if(step!=0){
            currentPermission=Permission.ACCESS_FINE_LOCATION;
        }
        XXPermissions.with(this)
                .permission(currentPermission)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if(step!=0) {
                            onPermissionCallback.onGranted(permissions, all);
                        }else{
                            requestLocationPermission(1,onPermissionCallback);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if(never){
                            showTs(getResString(R.string.no_location_permission_and_open));
                            XXPermissions.startPermissionActivity(BaseActivity.this, permissions);
                        }else{
                            showTs(getResString(R.string.no_location_permission));
                        }
                    }
                });
    }


}
