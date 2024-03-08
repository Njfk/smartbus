package com.sky.smartbus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sky.smartbus.R;
import com.sky.smartbus.bean.BusStation;
import com.sky.smartbus.utils.DisplayUtil;
import com.sky.smartbus.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author renshijie
 * @email 1192306691@qq.com
 * @createTime 2024/3/6
 * @describe NaviBusStationRealTimeView
 **/
public class NaviBusStationRealTimeView extends HorizontalScrollView {
    private Context mContext;
    private LinearLayout rootLinearLayout;
    private LinearLayout stationListLinearLayout;

    private BaseAttrProcessor baseAttrProcessor;
    private int progress = 0;
    private Bitmap bitmap = null;
    private NaviBusStationCarView carView;

    private List<TextView> stations = new ArrayList<>();
    private float stationMarginLeft = 30;
    private AtomicBoolean isTouching = new AtomicBoolean(false);


    public NaviBusStationRealTimeView(Context context) {
        this(context, null);
    }

    public NaviBusStationRealTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NaviBusStationRealTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        stationMarginLeft = (int) DisplayUtil.dp2px(this.mContext, 15);
        baseAttrProcessor = new BaseAttrProcessor(context, attrs, 0, this) {
            @Override
            protected void initAttrs() {
                Drawable drawable = this.typedArray.getDrawable(R.styleable.NaviBusStationView_sky_drawable);
                progress = this.typedArray.getInt(R.styleable.NaviBusStationView_sky_progress, 0);
                stationMarginLeft = this.typedArray.getDimension(R.styleable.NaviBusStationRealTimeView_sky_station_margin_left, stationMarginLeft);

                if (drawable != null) {
                    bitmap = FileUtil.drawableToBitmap(drawable);
                } else {
                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_bus2)).getBitmap();
                }
            }

            @Override
            protected int[] getAttrs() {
                return R.styleable.NaviBusStationRealTimeView;
            }
        };
        this.init();
    }


    private void init() {
        //添加根节点布局
        addRootLinearLayout();
    }

    /**
     * 添加根节点
     */
    private void addRootLinearLayout() {
        rootLinearLayout = new LinearLayout(this.mContext);
        LinearLayout.LayoutParams rootLinearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootLinearLayout.setLayoutParams(rootLinearLayoutParams);
        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);
        addView(rootLinearLayout);
        addStationCarView();
        addStationListView();
    }

    /**
     * 添加站台列表布局
     */
    private void addStationListView() {
        if (rootLinearLayout != null) {
            stationListLinearLayout = new LinearLayout(this.mContext);
            LinearLayout.LayoutParams stationListLinearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            stationListLinearLayout.setLayoutParams(stationListLinearLayoutParams);
            stationListLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLinearLayout.addView(stationListLinearLayout);
        }
    }

    /**
     * 添加可移动车标
     */
    private void addStationCarView() {
        if (rootLinearLayout != null) {
            carView = new NaviBusStationCarView(this.mContext);
            carView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (bitmap != null) {
                carView.setBitmap(bitmap);
            }
            rootLinearLayout.addView(carView);
        }
    }

//    /**
//     * 添加站台
//     */
//    @Deprecated
//    public void addStation(BusStation... busStation) {
//        if (stationListLinearLayout != null && busStation != null) {
//            for (BusStation station : busStation) {
//                TextView stationView = new TextView(this.mContext);
//                ViewGroup.LayoutParams stationParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                stationView.setLayoutParams(stationParams);
//
//                stationView.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                stationView.setText(station.getName());
//                stationView.setEms(1);
//                stationView.setSingleLine(false);
//                stationListLinearLayout.addView(stationView);
//                stations.add(stationView);
//            }
//        }
//        if (carView!=null && !carView.isAttached()){
//            carView.attachView(stationListLinearLayout);
//        }
//        //重新测量
//        stationListLinearLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                carView.requestLayout();
//            }
//        });
//    }

    /**
     * 只可添加一次
     *
     * @param busStation
     */
    public void addStation(List<BusStation> busStation) {
        stationListLinearLayout.removeAllViews();
        final int[] targetWidth = {0};
        if (stationListLinearLayout != null && busStation != null) {
            for (BusStation station : busStation) {
                int index = busStation.indexOf(station);
                TextView stationView = new TextView(NaviBusStationRealTimeView.this.mContext);

                LinearLayout.LayoutParams stationParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (index != 0) {
                    stationParams.leftMargin = (int) stationMarginLeft;
                }
                stationView.setLayoutParams(stationParams);

                stationView.setGravity(Gravity.CENTER_HORIZONTAL);
                stationView.setText(station.getName());
                stationView.setEms(1);
                stationView.setSingleLine(false);
                stationView.post(new Runnable() {
                    @Override
                    public void run() {
                        targetWidth[0] += stationView.getWidth() + stationParams.leftMargin;
                        if (index == busStation.size() - 1) {
                            if (carView != null) {
                                carView.setViewWidth(targetWidth[0]);
                            }
                        }
                    }
                });

                stationListLinearLayout.addView(stationView);
                stations.add(stationView);
            }
        }

        if (carView != null && !carView.isAttached()) {
            carView.attachView(stationListLinearLayout, NaviBusStationRealTimeView.this);
        }

        //重新测量
        stationListLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                if (carView != null)
                    carView.requestLayout();
            }
        });

    }

    /**
     * 设置顶部动画小车进度条
     *
     * @param progress
     */
    public void setProgress(int progress) {
        progress = Math.min(progress, 100);
        progress = Math.max(0, progress);
        this.progress = progress;
        if (carView != null)
            carView.setProgress(progress);
    }

    /**
     * 设置站台间进度
     */
    public void setProgress(int progress, int secondProgress) {
        progress = Math.min(progress, 100);
        progress = Math.max(0, progress);
        this.progress = progress;

        secondProgress = Math.min(secondProgress, 100);
        secondProgress = Math.max(0, secondProgress);

        if (carView != null)
            carView.setProgress(progress, secondProgress);

    }


    public void scrollByProgress(int progress, int secondProgress) {
        setProgress(progress, secondProgress);

        int i = progress2Index(progress);
        int carLocation = getCarLocation(i);

        if (!isTouching.get())
            scrollTo(carLocation - getWidth() / 2, 0);
    }

    /**
     * 进度的转坐标
     *
     * @param progress
     * @return
     */
    public int progress2Index(int progress) {
        int size = stations.size();
        return (int) ((float) progress / 100 * size);
    }

    /**
     * 获取车标当前的像素位置
     *
     * @param index
     * @return
     */
    public int getCarLocation(int index) {
        int totoal = 0;
        for (int i = 0; i <= index; i++) {
            TextView textView = stations.get(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            if (i == 0) {
                continue;
            }
            if (i == stations.size() - 1) {
                totoal += textView.getWidth() * 2 + layoutParams.leftMargin;
            } else {
                totoal += textView.getWidth() + layoutParams.leftMargin;
            }
        }
        return totoal;
    }

    /**
     * 获取车标在站台之间的位置
     *
     * @return
     */
    public float getCarSecondLocation(int secondProgress) {
        //获取每个站台间的距离
        int totoal = 0;
        if (stations != null && stations.size() >= 2) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) stations.get(1).getLayoutParams();
            totoal = stations.get(1).getWidth() + layoutParams.leftMargin;
        }
        float percent = (float) secondProgress / 100;
        return totoal * percent;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bitmap != null)
            bitmap.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            isTouching.set(true);
        } else if (action == MotionEvent.ACTION_UP) {
            isTouching.set(false);
        }
        return super.onTouchEvent(ev);
    }
}