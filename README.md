# smart-bus-station

#### 介绍
公交站台自定义View
效果如图所示
1.路线移动
![输入图片说明](%E6%9C%AA%E5%91%BD%E5%90%8D-%E5%89%AF%E6%9C%AC.gif)
2.站台间移动
![输入图片说明](%E6%9C%AA%E5%91%BD%E5%90%8D-%E5%89%AF%E6%9C%AC(2).gif)


#### 使用说明

1.  在布局文件中添加

```
    <com.sky.smartbus.widget.NaviBusStationRealTimeView
        android:id="@+id/nrt"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:scrollbars="none"
        android:overScrollMode="never"
        app:sky_drawable="@drawable/icon_bus2"
        app:sky_station_margin_left="50dp"
        />
```
app:sky_station_margin_left：可以设置站台间隔
app:sky_drawable：设置车标
app:sky_progress: 设置默认进度
2.  通过方法：
void setProgress(int progress)；//设置站台进度
void setProgress(int progress,int secondProgress)；//设置站台进度和站台间进度


