package com.dy.fastframework.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.BDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.dy.fastframework.R;

import org.jetbrains.annotations.NotNull;

/**
 * 子类直接继承即可快速创建弹窗
 */
public abstract class DialogFragmentBase extends BDialogFragment {
    private Bundle _data;
    private View view;
    private FragmentActivity context;

    public Bundle getBundleData() {
        return _data;
    }

    public DialogFragmentBase(Bundle data) {
        this._data=data;
    }

    public boolean isNeedSelfStyle(){
        return false;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isNeedSelfStyle()) {
            setStyle(STYLE_NO_TITLE, R.style.ActionSheetDialogStyle);
        }else{
            setSelfStyle();
        }
    }

    public void setSelfStyle() {

    }



    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()!=null&&getDialog().getWindow()!=null) {
            Window dialogWindow = getDialog().getWindow();
            //设置Dialog从窗体底部弹出
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            //设置动画
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
            //获得窗体的属性
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            if(!setWindowLayoutParams(lp,dialogWindow)){
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.horizontalMargin = 0;
                lp.verticalMargin = 0;
                lp.gravity= Gravity.BOTTOM;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                //       将属性设置给窗体
                dialogWindow.setAttributes(lp);
            }

        }
    }




    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), setLayoutId(), null);
        this.context= getActivity();
        return view;
    }




    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViewWithId(view);
        init();
    }



    //设置布局id
    public abstract int setLayoutId();

    public abstract void bindViewWithId(View view);

    /**
     * return true自定义窗口宽高，代码直接写在方法中即可
     * @param lp
     * @param window
     * @return
     */
    public abstract boolean setWindowLayoutParams(WindowManager.LayoutParams lp, Window window);

    //首次加载时
    protected abstract void init();



}