package com.yy.base.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.HashSet;

/**
 * FontCache
 * date:   2017/10/30 17:27 <br/>
 */

public class FontCache {
    private static HashMap<String, Typeface> fontCache = new HashMap<>(2);

    public static Typeface getTypeface(String fontName, Context context){
        Typeface typeface = fontCache.get(fontName);

        if(typeface == null){
            try{
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

            fontCache.put(fontName, typeface);
        }

        return typeface;
    }
}
