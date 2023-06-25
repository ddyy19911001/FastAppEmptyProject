package com.dy.fastframework.util;

import android.os.Build;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class DeviceIDUtil {
    public static String getDeviceId() {
        String serial = null;
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        //13 位
        try { serial = Build.class.getField("SERIAL").get(null).toString();
            // API>=9 使用serial号
            return Md5(new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString());
        } catch (Exception exception) {
            // rial需要一个初始化
            serial = "serial";
            // 随便一个初始化
        } return Md5(new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString());
    }

    //md5加密
    public static String Md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length;
                 offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().substring(8, 24);
            System.out.println("result: " + result);
            Log.d("ss",result+"");
            // 32位的加密
            //   System.out.println("result: " + buf.toString().substring(8,24));
            // 16位的加密
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        } return result;

    }
}
