package com.sky.smartbus.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author renshijie
 * @email 1192306691@qq.com
 * @createTime 2024/1/5
 * @describe DisplayUtil
 **/
public class DisplayUtil {

    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
    }
}
