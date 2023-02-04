package com.example.ex7;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPercentage = level / (float)scale;
        if (batteryPercentage < 0.6) {
//            WorkManager workManager = WorkManager.getInstance(context);
//            List<WorkInfo> list = workManager.getWorkInfosByTagLiveData(TimerWorker.TAG).getValue();
            if(TimerWorker.isWorking)
            //list != null && list.get(0).getState()== WorkInfo.State.RUNNING
            {
                Toast.makeText(context, "Timer is runnig but battery is low!", Toast.LENGTH_LONG).show();
            }

        }
    }
}