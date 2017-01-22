package com.github.library.util;

import android.content.Context;

/**
 * @ explain:
 * @ author：xujun on 2016/5/30 18:15
 * @ email：gdutxiaoxu@163.com
 */
public class ScreenUtils {

    private ScreenUtils() {

    }
    /**
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
