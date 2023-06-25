package com.dy.fastframework.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Created by Administrator on 2019/3/29 0029.
 */
public class MyUtils {
    private static Dialog loadingDialog;
    public static boolean isEmpty(String text){
        if(TextUtils.isEmpty(text)||TextUtils.isEmpty(text.trim())){
            return true;
        }
        return false;
    }


    public static boolean isEmpty(TextView text){
        if(TextUtils.isEmpty(text.getText().toString())||TextUtils.isEmpty(text.getText().toString().trim())){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Collection<?> lists){
        if(lists==null||lists.isEmpty()){
            return true;
        }
        return false;
    }

    //判断Map是否为空
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    //判断数组是否为空
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    //判断List是否为空
    public static boolean isEmpty(List<Object> list) {
        return list == null || list.size() == 0;
    }




    /**
     * 格式化时间
     * @param strFormat yyyy-MM-dd HH:mm:ss
     * @param time  102252300
     * @return
     */
    public static String formatTime(String strFormat,long time){
        String s="";
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat);
            s=format.format(new Date(time));
        }catch (Exception e){
            e.printStackTrace();
            s="格式化时间错误";
        }
        return s;
    }

    static ToastUtil tUtil;
    public static void showTs(Context context,String msg) {
        if(tUtil==null){
            tUtil=new ToastUtil(context,msg);
        }
        tUtil.setText(msg);
        tUtil.show();
    }

}
