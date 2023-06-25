package com.dy.fastframework.util;

import android.text.TextUtils;

/**
 * 控件规则工具
 */
public class ViewRegUtils {
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个
        //"\\d{9}"代表后面是可以是0～9的数字，有9位
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean isMatchPwdReg(String pwd){
        String reg="[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*";
        if (TextUtils.isEmpty(pwd)) return false;
        else return pwd.matches(reg);
    }
} 
