package cn.chenanduo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chen on 2017/1/6.
 */

public class SpUtil {

    private static SharedPreferences sp;

    /**
     * 保存boolean
     */
    public static void saveBoolean(Context context,String key,boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("spinfo", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }
    /**
     * 取出boolean
     */
    public static boolean getBoolean(Context context,String key,boolean defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("spinfo", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defaultValue);
    }
    /**
     * 保存String
     */
    public static void saveString(Context context,String key,String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("spinfo", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }
    /**
     * 取出String
     */
    public static String getString(Context context, String key, String defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("spinfo", Context.MODE_PRIVATE);
        }
        return sp.getString(key,defaultValue);
    }
}
