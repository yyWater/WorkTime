package com.yy.base.utils;

/**
 * Created by Administrator on 2017/11/5.
 */

public class ColorUtils {
    public static String colorToString(int color) {
        String string = "#000000";

        color &= 0x00FFFFFF; // Format is argb, remove alpha value.

        try {
            string = String.format("#%06X", color);
        } catch (NumberFormatException e) {
           e.printStackTrace();
        }

        return string;
    }
}
