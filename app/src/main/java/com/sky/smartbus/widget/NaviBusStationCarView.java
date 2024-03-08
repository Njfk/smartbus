package com.sky.smartbus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sky.smartbus.R;
import com.sky.smartbus.utils.FileUtil;

/**
 * @author renshijie
 * @email 1192306691@qq.com
 * @createTime 2024/3/6
 * @describe NaviBusStationView
 **/
public class NaviBusStationCarView extends BaseCustomView {
    private Paint paint;
    private Bitmap bitmap;
    private Rect mSrcRect, mDestRect;
    private int mTotalWidth;
    private int mTotalHeight;
    private float progressPercent = 0f;
    private int progress = 0;

    private int secondeProgress = 0;

    private int stationCount = 0;
    private float fontSize = 25;
    private float strokeWidth = 10;
    private LinearLayout attachView;
    private NaviBusStationRealTimeView parentView;
    private int currentIndex = 0;
    private int targetWidth = 0;

    public NaviBusStationCarView(Context context) {
        super(context);
    }

    public NaviBusStationCarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NaviBusStationCarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        baseAttrProcessor = new BaseAttrProcessor(context, attrs, 0, this) {
            @Override
            protected void initAttrs() {
                Drawable drawable = this.typedArray.getDrawable(R.styleable.NaviBusStationView_sky_drawable);
                progress = this.typedArray.getInt(R.styleable.NaviBusStationView_sky_progress, 0);
                if (drawable != null) {
                    bitmap = FileUtil.drawableToBitmap(drawable);
                } else {
                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_bus2)).getBitmap();
                }
            }

            @Override
            protected int[] getAttrs() {
                return R.styleable.NaviBusStationView;
            }
        };
        fontSize = 25;
        strokeWidth = 10;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setTextSize(fontSize);
        paint.setColor(Color.RED);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        if (bitmap == null)
            bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_bus2)).getBitmap();

        mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDestRect = new Rect(100, 0, bitmap.getWidth() + 100, bitmap.getHeight());
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDestRect = new Rect(-bitmap.getWidth() / 2, 0, bitmap.getWidth() / 2, bitmap.getHeight());
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int currentWidthLocation = (int) (getWidth() * (progressPercent));
        if (parentView != null) {
            currentWidthLocation = (int) (parentView.getCarLocation(currentIndex) + parentView.getCarSecondLocation(secondeProgress));
        }
        mDestRect.left = currentWidthLocation - bitmap.getWidth() / 2;
        mDestRect.right = bitmap.getWidth() / 2 + currentWidthLocation;

        //画起点
        paint.setColor(Color.BLUE);
//        canvas.drawCircle((float) fontSize / 2, (float) (bitmap.getHeight() / 1.5 - fontSize / 2), fontSize / 1.5f, paint);
//        paint.setColor(Color.WHITE);
        canvas.drawText("始", (float) 0, (float) (bitmap.getHeight() / 1.5), paint);

        //画线段
        //画走过的路线
        paint.setColor(Color.GRAY);
        canvas.drawLine(0, bitmap.getHeight(), currentWidthLocation, bitmap.getHeight(), paint);
        //画未走过的路线
        paint.setColor(Color.BLUE);
        canvas.drawLine(currentWidthLocation, bitmap.getHeight(), getWidth(), bitmap.getHeight(), paint);
        //画终点
        paint.setColor(Color.BLUE);
        canvas.drawText("终", getWidth() - fontSize, (float) (bitmap.getHeight() / 1.5), paint);
        //画图片
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        progress = Math.min(progress, 100);
        progress = Math.max(progress, 0);
        this.progress = progress;
        this.secondeProgress = 0;//初始化secondProgress
        this.progressPercent = (float) ((float) progress / 100);
        this.currentIndex = parentView.progress2Index(progress);
        postInvalidate();
    }

    public void setProgress(int progress, int secondProgress) {
        progress = Math.min(progress, 100);
        progress = Math.max(progress, 0);
        this.progress = progress;

        secondProgress = Math.min(secondProgress, 100);
        secondProgress = Math.max(secondProgress, 0);
        this.secondeProgress = secondProgress;

        this.progressPercent = (float) ((float) progress / 100);
        this.currentIndex = parentView.progress2Index(progress);
        postInvalidate();
    }

    public void setSecondProgress(int secondProgress) {
        secondProgress = Math.min(secondProgress, 100);
        secondProgress = Math.max(secondProgress, 0);
        this.secondeProgress = secondProgress;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.max(bitmap.getWidth(),targetWidth);
        int height = bitmap.getHeight();
        //宽度测量
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMeasureMode == MeasureSpec.EXACTLY) {
            width = widthMeasureSize;
        } else if (widthMeasureMode == MeasureSpec.UNSPECIFIED) {
            if (targetWidth!=0){
                width = targetWidth;
            }else {
                width = attachView == null ? getResources().getDisplayMetrics().widthPixels : attachView.getWidth() + attachView.getPaddingLeft() + attachView.getPaddingRight();
            }
        } else {
            width = Math.min(widthMeasureSize, width);
        }

        //高度测量
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMeasureMode == MeasureSpec.EXACTLY) {
            height = heightMeasureSize;
        } else if (heightMeasureMode == MeasureSpec.UNSPECIFIED) {
            height = attachView == null ? getResources().getDisplayMetrics().heightPixels : attachView.getHeight();
        } else {
            height = (int) Math.min(heightMeasureSize, bitmap.getHeight() + strokeWidth);
        }

        Log.d("=======", "onMeasure: "+width+","+height);
        setMeasuredDimension(width, height);
    }

    //关联View
    public void attachView(LinearLayout stationListLinearLayout, NaviBusStationRealTimeView viewGroup) {
        attachView = stationListLinearLayout;
        parentView = viewGroup;
        requestLayout();
    }

    public void setViewWidth(int width){
        this.targetWidth = width;
        requestLayout();
    }

    public boolean isAttached() {
        return attachView != null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bitmap != null)
            bitmap.recycle();
    }
}

