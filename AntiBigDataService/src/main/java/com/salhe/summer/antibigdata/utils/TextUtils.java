package com.salhe.summer.antibigdata.utils;

public class TextUtils {

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isEmail(String string){
        return string.matches("\\\\w+([-+.]\\\\w+)*@\\\\w+([-.]\\\\w+)*\\\\.\\\\w+([-.]\\\\w+)*");
    }

}
