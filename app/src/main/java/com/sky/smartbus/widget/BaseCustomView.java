package com.sky.smartbus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author renshijie
 * @email 1192306691@qq.com
 * @createTime 2024/3/6
 * @describe BaseCustomView
 **/
public abstract class BaseCustomView extends View {
    protected BaseAttrProcessor baseAttrProcessor;

    protected Context mContext;

    public BaseCustomView(Context context) {
        this(context, null);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        init(context, attrs, defStyleAttr);
    }

    protected abstract void init(Context context, AttributeSet attrs, int defStyleAttr);
}
