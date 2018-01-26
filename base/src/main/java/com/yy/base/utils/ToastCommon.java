package com.yy.base.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastCommon{

    public static void makeText(Context context, @StringRes int strResId) {
        if(context == null){
            return;
        }
        Toast.makeText(context, context.getString(strResId), Toast.LENGTH_SHORT).show();
    }
    public static void makeText(Context context, @StringRes int strResId, Object... formatArgs) {
        if(context == null){
            return;
        }
        Toast.makeText(context, context.getString(strResId,formatArgs), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, CharSequence text) {
        if(context == null){
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
