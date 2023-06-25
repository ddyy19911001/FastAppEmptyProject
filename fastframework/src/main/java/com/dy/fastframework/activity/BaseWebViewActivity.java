package com.dy.fastframework.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.ValueCallback;

import androidx.annotation.NonNull;

import com.dy.fastframework.R;
import com.dy.fastframework.web.MyWebView;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.config.HttpGlobalConfig;

/**
 * 请设置hardwareAccelerated=true---->可播放视频
 */
public abstract class BaseWebViewActivity extends BaseActivity {
    private static final int INTENT_PHONE = 9562;
    public MyWebView base_webView;
    public ValueCallback valueCallback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //设置硬件加速
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        base_webView = getBase_webView();
        base_webView.enableFullScrenVideo(this);//设置支持全屏播放
        base_webView.setWebFileChoseListener(new MyWebView.WebFileChoseListener() {
            @Override
            public void getFile(ValueCallback valueCallback) {
                BaseWebViewActivity.this.valueCallback = valueCallback;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, INTENT_PHONE);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_PHONE){
            if (resultCode == Activity.RESULT_OK) {
                seleteH5File(data, valueCallback);
            }else {
                valueCallback.onReceiveValue(null);
                valueCallback = null;
            }
        }
    }



    public static void seleteH5File(Intent data, ValueCallback valueCallback){
        if (valueCallback == null){
            // todo valueCallback 为空的逻辑
            return;
        }
        try {
            Uri[] results = null;
            String dataString = data.getDataString();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
            }

            if (dataString != null) {
                results = new Uri[]{Uri.parse(dataString)};
                valueCallback.onReceiveValue(results);
            }
        }catch (Exception e){
            e.printStackTrace();
            valueCallback.onReceiveValue(null);
        }
        valueCallback = null;
    }


    @Override
    public void onNotInitFirst() {
        ViseHttp.CONFIG().setOnRequestWatingDialogListener(new HttpGlobalConfig.OnRequestWatingDialogListener() {
            @Override
            public void onTimeOverToShowLoading() {
                showLoadingDialog(getResources().getString(R.string.loading), true);
            }

            @Override
            public void onRequestOverLoadingNeedClose() {
                closeDialog();
            }
        });
        super.onNotInitFirst();
    }


    public abstract MyWebView getBase_webView();

    @Override
    public void onResume() {
        super.onResume();
        if(base_webView !=null){
            base_webView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(base_webView !=null){
            base_webView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(base_webView !=null){
            base_webView.destroy();
            base_webView =null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (base_webView !=null&&keyCode == KeyEvent.KEYCODE_BACK && base_webView.canGoBack()) {
            base_webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




    @Override
    public void onConfigurationChanged(@NonNull Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }

    }
}
