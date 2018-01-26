package com.yy.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;


import com.yy.base.ActivityController;

import java.util.Locale;

/**
 * 多语言存储类及切换方法
 */

public class LanguageSpUtils {

    private static final String lang_sp = "language";
    private static final String key_lang = "key_lang";

    public static final String EN = "en";
    public static final String ZH_TW = "zh_TW";
    public static final String ZH_CN = "zh_CN";
    public static final String JA = "ja";
    public static final String RU = "ru";
    public static final String ES = "es";

    /**
     * 获取应用采用的语言的索引
     *
     * @param context Context
     * @return String
     */
    public static String getLocalLanguage(Context context) {
        String defaultValue;
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE)) {
            defaultValue = String.format("%s_%s",
                    Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        } else {
            defaultValue = String.format("%s", Locale.getDefault().getLanguage());
        }
        return getSharedPreferences(context).getString(key_lang, defaultValue);
    }

    /**
     * 保存 切换的语言
     *
     * @param context  Context
     * @param language String
     */
    private static void saveLocalLanguage(Context context, String language) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_lang, language);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(lang_sp, Context.MODE_PRIVATE);
    }

    /**
     * 保存设置选项 并 重新跳转到主页面
     *
     * @param context  Context
     * @param language String
     */
    public static void saveSetting(Context context, String language, Class<?> cls) {
        saveLocalLanguage(context, language);
        restart(context, cls);
    }

    private static void restart(Context context, Class<?> cls) {
        ActivityController.getInstance().popAllActivity();
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * 根据 设置选项，显示相应的语言
     */
    public static void changeLanguage(Context context) {
        String language = LanguageSpUtils.getLocalLanguage(context);
        Locale locale;

        //modify 2017年6月1日19:27:17 因为在mate8升级到最新系统后，发现当前语言为中文时，Locale.getDefault()
        //方法返回的是en，导致客户端默认显示成英文，先删除跟随系统，默认是中文显示
        if (TextUtils.equals(EN, language)) {
            locale = Locale.ENGLISH;
        } else if (TextUtils.equals(ZH_TW, language)) {
            locale = Locale.TRADITIONAL_CHINESE;
        } else if (TextUtils.equals(ZH_CN, language)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (TextUtils.equals(JA, language)) {
            locale = Locale.JAPANESE;
        } else if (TextUtils.equals(RU, language)) {
            locale = new Locale(RU, "RU");
        } else if (TextUtils.equals(ES, language)) {
            locale = new Locale(ES, "ES");
        } else {
            locale = Locale.getDefault();
        }

        setLanguage(context, locale);
    }

    /**
     * 设置语言
     *
     * @param locale Locale
     */
    private static void setLanguage(Context cxt, Locale locale) {
        Resources resources = cxt.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    /**
     * 判断当前应用语言是否为中文
     *
     * @return boolean
     */
    public static boolean isAppInZh(Context cxt) {
        Locale local = cxt.getResources().getConfiguration().locale;
        return local.equals(Locale.CHINESE);
    }

}
