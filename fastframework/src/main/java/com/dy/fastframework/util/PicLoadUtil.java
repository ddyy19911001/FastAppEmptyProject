package com.dy.fastframework.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dy.fastframework.R;


public class PicLoadUtil {
    public static int erroImgRes= R.drawable.pic_loading_holder;
    public static int loadingRes= R.drawable.pic_loading_holder;
    public static int cornerRes= R.drawable.pic_loading_circle_holder;
    public static final float range=0.1f;


    public static void loadThumb(Context context, Object res, ImageView view){
        if(res==null){
            return;
        }
        if(res instanceof String){
            String url= (String) res;
            if(MyUtils.isEmpty(url)){
                return;
            }
        }
        if(context==null){
            return;
        }
        if(context instanceof Activity){
            Activity activity= (Activity) context;
            if(activity.isFinishing()){
                return;
            }
        }
        Glide.with(context).load(res).dontAnimate().placeholder(loadingRes).error(erroImgRes).thumbnail(0.2f).into(view);
    }

    public static void load(Context context, Object res, ImageView view){
        if(res instanceof Integer) {
            int r = (int) res;
            if (r == 0) {
                return;
            }
        }else if(res instanceof String){
            if(MyUtils.isEmpty((String) res)){
                return;
            }
        }else if(res==null){
            return;
        }
        if(res instanceof Bitmap){
            view.setImageBitmap((Bitmap) res);
            return;
        }
        loadDefault(context,res,view);
    }

    public static void loadHead(Context context, Object res, ImageView view){
        if(res instanceof Integer) {
            int r = (int) res;
            if (r == 0) {
                return;
            }
        }else if(res instanceof String){
            if(MyUtils.isEmpty((String) res)){
                return;
            }
        }else if(res==null){
            return;
        }
        loadDefaultHead(context,res,view);
    }



    private static void loadDefault(Context context, Object res, ImageView view){
        if(res==null){
            return;
        }
        if(res instanceof String){
            String url= (String) res;
            if(MyUtils.isEmpty(url)){
                return;
            }
        }
        if(context==null){
            return;
        }
        if(context instanceof Activity){
            Activity activity= (Activity) context;
            if(activity.isFinishing()){
                return;
            }
        }
        Glide.with(context).load(res).dontAnimate().placeholder(loadingRes).error(erroImgRes).into(view);
    }



    private static void loadDefaultHead(Context context, Object res, ImageView view){
        if(res==null){
            return;
        }
        if(res instanceof String){
            String url= (String) res;
            if(MyUtils.isEmpty(url)){
                return;
            }
        }
        if(context==null){
            return;
        }
        if(context instanceof Activity){
            Activity activity= (Activity) context;
            if(activity.isFinishing()){
                return;
            }
        }
        Glide.with(context).load(res).dontAnimate().placeholder(R.drawable.pic_loading_circle_holder).error(R.drawable.pic_loading_circle_holder).into(view);
    }



}
