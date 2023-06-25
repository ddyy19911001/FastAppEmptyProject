package com.dy.fastframework.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.dy.fastframework.R;
import com.dy.fastframework.util.AppActivityListManager;
import com.dy.fastframework.util.AppExit2Back;
import com.dy.fastframework.util.MyUtils;
import com.dy.fastframework.util.NetStateReceiver;
import com.dy.fastframework.util.NoDoubleClickListener;
import com.dy.fastframework.util.ScreenUtils;
import com.dy.fastframework.util.StatuBarUtils;
import com.dy.fastframework.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;



/**
 * Created by Administrator on 2018/4/12.
 * deng yin
 */
public abstract class SuperBaseActivity extends AppCompatActivity implements NetStateReceiver.OnNetStateChangeListener {
    public Dialog loadingDialog;
    public boolean isMainActivity;
    public NetStateReceiver netChangeReceiver;
    public ToastUtil toast;
    public SuperBaseActivity mActivity;
    public static final int DEFAULT_STATUS_BAR_H=-1;

    /**
     * 退出app
     */
    public void exitAppNow(){
        AppActivityListManager.getScreenManager().removeAllActivity();
        //创建ACTION_MAIN
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Context content = this;
        //启动ACTION_MAIN
        content.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        onNotcreate();
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mActivity=this;
        onNotSetContentView();
        setLayoutVoid(setLayout());
        onNotAddActivity();
        //将所有activity列入activity管理器中方便退出时清理
        AppActivityListManager.getScreenManager().addActivity(this);
        onNotBindView();
        bindViewWithId();
        onViewBindOver(savedInstanceState);
        onNotSetStatusBar();
        //去除小米手机白色状态栏
        StatuBarUtils.setStatusBarTranslucent(this,true);
        onNotSetNetWorkListener();
        initNetWork();
        //设置沉浸式状态栏文字颜色为黑色
        setStatusBarTextColorBlack(true);
        isMainActivity = setIsExitActivity();
        onNotInitFirst();
        initFirst();
        onInitFirstOver();
    }



    public void changeThemMode(){
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        AppCompatDelegate.setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_NO ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        // 同样需要调用recreate方法使之生效
    }

    public void onViewBindOver(Bundle savedInstanceState) {

    }

    public void setLayoutVoid(int setLayout) {
        setContentView(setLayout);
    }

    /**
     * 其他逻辑全部走完
     */
    public void onInitFirstOver() {

    }

    /**
     * 其他逻辑还没有开始
     */
    public void onNotInitFirst() {
       setStatusBarHeight(DEFAULT_STATUS_BAR_H);
       setBackClickListener();
       setActivityTitle(null);
    }


    /**
     * 重写此方法设置标题
     * @param title
     */
    public void setActivityTitle(String title) {
        TextView titleView=findViewById(R.id.tv_activity_title);
        if(titleView!=null&& !MyUtils.isEmpty(title)){
            titleView.setText(title);
        }
    }

    private void setBackClickListener() {
        View view=findViewById(R.id.iv_title_back);
        if(view!=null){
            view.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    finish();
                }
            });
        }
    }


    /**
     *  主动设置状态栏进行占位，可重写不设置此状态栏即可全屏
     */
    public void setStatusBarHeight(int h) {
        View view=findViewById(R.id.tv_system_status_bar);
        if(view!=null){
            if(h>=0){
                view.getLayoutParams().height=h;
            }else{
                int statusH = (int) getResDimens(R.dimen.default_status_h);
                view.getLayoutParams().height=statusH;
            }
        }
    }


    /**
     * 设置状态栏背景颜色，要设置状态栏文字颜色调用
     * @param colorRes
     */
    public void setStatusBarBackGroundColor(int colorRes){
        View view=findViewById(R.id.tv_system_status_bar);
        if (view != null) {
            if (colorRes != 0) {
                view.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(new ColorDrawable(colorRes));
                } else {
                    view.setBackgroundColor(colorRes);
                }
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获取颜色资源
     * @param res
     * @return
     */
    public int getResColor(int res){
        return getResources().getColor(res);
    }


    /**
     * 获取尺寸资源
     * @param res
     * @return
     */
    public float getResDimens(int res){
        return getResources().getDimension(res);
    }


    /**
     * 获取颜色资源
     * @param res
     * @return
     */
    public String getResString(int res){
        return getResources().getString(res);
    }

    /**
     *
     * @param statubarBackGroundColor  状态栏背景色
     * @param isStatusTextWhite  状态栏文字是否为白色
     */
    public void changeStatustBarColor(int statubarBackGroundColor,boolean isStatusTextWhite){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(statubarBackGroundColor);
            // 设置状态栏字体颜色
            if(isStatusTextWhite){
                //白色
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }else{
                //黑色
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

        }
    }

    /**
     * 还没有设置网络状态监听
     */
    public void onNotSetNetWorkListener() {

    }

    /**
     * 还没有设置状态栏
     */
    public void onNotSetStatusBar() {

    }

    /**
     * 还没有开始添加activity到管理器里面
     */
    public void onNotAddActivity() {

    }

    /**
     * 还没有绑定ViewId
     */
    public void onNotBindView() {

    }

    /**
     * 还没有开始设置布局
     */
    public void onNotSetContentView(){

    }

    /**
     * 还没有开始Create
     */
    public void onNotcreate() {

    }

    /**
     * 绑定ViewId
     */
    public void bindViewWithId() {

    }




    //参数传入是否黑色状态栏文字：false-白色文字  true-黑色文字
    public void setStatusBarTextColorBlack(boolean isBlackStatusTextColor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            // 设置状态栏字体颜色黑色
            View decor = getWindow().getDecorView();
            if (isBlackStatusTextColor) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // 设置状态栏底色
                // 设置状态栏字体颜色白色
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }



    public void initNetWork() {
        IntentFilter intentFilter = new IntentFilter();
        //当前网络发生变化后，系统会发出一条值为android.net.conn.CONNECTIVITY_CHANGE的广播，所以要监听它
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChangeReceiver = new NetStateReceiver();
        netChangeReceiver.setOnNetStateChangeListener(this);
        //进行注册
        registerReceiver(netChangeReceiver, intentFilter);
    }

    /**
     * 处理断网
     */
    @Override
    public void onNetChange(int state) {

    }





    public void showTs(String msg) {
        if (toast == null) {
            toast = new ToastUtil(this,msg);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public String getPhoneType() {
        return Build.BRAND + "-" + Build.MODEL;
    }


    public boolean setIsExitActivity() {
        return false;
    }




    @Override
    public void onResume() {
        super.onResume();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        unregisterReceiver(netChangeReceiver);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }


    @Override
    public void onStart() {
        super.onStart();
    }



    public abstract int setLayout();

    public abstract void initFirst();





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (isMainActivity) {
                        appExit();
                        return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }




    public void appExit() {
        AppExit2Back.exitApp(this);
    }


    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersionNum() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 获取版本名称
     *
     * @return
     */
    public String getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }


    /**
     * 获取应用名称
     *
     * @return
     */
    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }


    /**
     * 获取唯一设备Id
     *
     * @return
     */
    public String getPhoneId() {
        //权限校验
        return getUniquePsuedoID();
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 显示转圈的Dialog
     *
     * @return
     */
    public Dialog showLoadingDialog(String msg, boolean canDismiss) {
        closeDialog();
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(this, R.style.MyDialogStyle);// 创建自定义样式dialog
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
        this.loadingDialog = loadingDialog;
        return loadingDialog;
    }


    /**
     * 关闭dialog
     */
    public void closeDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            this.loadingDialog.dismiss();
            this.loadingDialog = null;
        }
    }

}
