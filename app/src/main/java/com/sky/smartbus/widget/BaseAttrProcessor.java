package com.sky.smartbus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author renshijie
 * @email 1192306691@qq.com
 * @createTime 2024/1/11
 * @describe BaseAttrProcessor 继承ViewGroup时需要自定义属性时使用
 **/
public abstract class BaseAttrProcessor<T extends View> {

    protected TypedArray typedArray;
    protected T baseView;

    protected Context context;

    public BaseAttrProcessor(Context context, AttributeSet attributeSet, int i, T t) {
        this.baseView = t;
        this.context = context;
        if (attributeSet != null) {
            typedArray = context.obtainStyledAttributes(attributeSet,getAttrs());
            initAttrs();
            typedArray.recycle();
        }
    }

    protected abstract void initAttrs();

    protected abstract int[] getAttrs();
}
