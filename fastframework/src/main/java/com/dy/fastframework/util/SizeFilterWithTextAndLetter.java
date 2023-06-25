package com.dy.fastframework.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeFilterWithTextAndLetter implements InputFilter {
    public int filterType;
    public static final int typeMobile = 0;
    public static final int typeEmail = 1;
    public static final int typeNickName = 2;
    public static final int typePwd = 3;
    private int maxLength = 12;

    public SizeFilterWithTextAndLetter(int maxLength, int filterType) {
        this.maxLength = maxLength;
        this.filterType = filterType;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        //只可输入数字0-9，下划线和中英文,@符号，长度是12
        String reg = "[a-zA-Z|_|@|\u4e00-\u9fa5|0-9]+";
        switch (filterType) {
            case typeMobile:
                reg = "[0-9]+";
                break;
            case typeEmail:
                reg = "[a-zA-Z|_|@|.|0-9]+";
                break;
            case typeNickName:
                reg = "[a-zA-Z|_|@|\u4e00-\u9fa5|0-9]+";
                break;
            case typePwd:
                reg = "[a-zA-Z|_|@|0-9]+";
                break;
        }
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(source.toString());
        if (!m.matches()) return "";
        LogUtils.e("source.length=" + source.length() + ",dest.length=" + dest.length());
        int originLength = 0;
        int nowLength = 0;
        if (dest != null) {
            originLength = dest.length();
        }
        if (source != null) {
            nowLength = source.length();
        }
        int allLength = originLength + nowLength;
        if (allLength > maxLength) {
            int maxIndex = maxLength - originLength;
            return source.subSequence(0, maxIndex);
        } else {
            return null;
        }
    }
}
