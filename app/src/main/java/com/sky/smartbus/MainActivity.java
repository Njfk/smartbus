package com.sky.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sky.smartbus.bean.BusStation;
import com.sky.smartbus.widget.NaviBusStationRealTimeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int progress =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NaviBusStationRealTimeView nrt = findViewById(R.id.nrt);

        List<BusStation> busStations = new ArrayList<>();
        int statoinSize = 20;
        for (int i = 0; i<statoinSize; i++){
            BusStation busStation = new BusStation();
            busStation.setName("地铁站"+i);
            busStations.add(busStation);

        }
        nrt.addStation(busStations);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress +=1;
                        if (progress == statoinSize){
                            progress = 0;
                        }
                        int i = (int) (((float) progress / statoinSize) * 100);

//                       nrt.setProgress(10,i);
                       nrt.setProgress(i);
                    }
                });
            }
        },0,1000);
    }
}