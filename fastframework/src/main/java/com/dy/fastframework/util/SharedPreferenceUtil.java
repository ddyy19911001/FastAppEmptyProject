package com.dy.fastframework.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;


/**
 * 保存用户信息
 */
public class SharedPreferenceUtil {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String Tag="DataSave-DyLog";
    private Gson gson;

    public SharedPreferenceUtil(Context context,String nameKey) {
        this.context = context;
        gson=new Gson();
        sharedPreferences = context.getSharedPreferences(nameKey, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveString(String tag, String s) {
        editor.putString(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

    public void saveBoolean(String tag, boolean s) {
        editor.putBoolean(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

    public void saveInt(String tag, int s) {
        editor.putInt(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

    public void saveLong(String tag, long s) {
        editor.putLong(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

   public void saveObject(String tag,Object object){
        if(object==null){
            saveString(tag,"");
            return;
        }
       try {
           String objStr= gson.toJson(object);
           saveString(tag,objStr);
           showSuccessLog(tag, objStr);
       }catch (Exception e){
           e.printStackTrace();
       }
   }



    public void clearString(String tag) {
        editor.putString(tag, null);
    }

    public void clearLong(String tag) {
        editor.putLong(tag, 0L);
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }



    public String getStr(String key) {
        return sharedPreferences.getString(key, null);
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0L);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public Object getObj(String key,Class clzz){
        String objStr=sharedPreferences.getString(key, null);
        try {
            if(TextUtils.isEmpty(objStr)){
                return null;
            }else{
                return gson.fromJson(objStr, clzz);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }




    private void showSuccessLog(String tag, String s) {
//        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

    private void showSuccessLog(String tag, boolean s) {
//        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

    private void showSuccessLog(String tag, int s) {
//        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

    private void showSuccessLog(String tag, long s) {
//        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

}
