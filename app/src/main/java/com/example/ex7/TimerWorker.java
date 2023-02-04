package com.example.ex7;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data; //added in dependencies
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class TimerWorker extends Worker {

    public static final String TAG = "TIMERTAG";
    private static final String NOTIFICATION_CHANNEL_ID = "countdown_channel";
    private static final int NOTIFICATION_ID = 101;
    public static boolean isWorking = false;
    private Context context;

    public TimerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        int countdownTime = getInputData().getInt("countdown_time", 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.cake) //TODO: need to be something different
                .setContentTitle("Countdown Timer")
                .setContentText("Started for " + countdownTime/60 + " minutes")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Countdown Timer", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
        isWorking = true;

        for (int i = countdownTime; i >= 0; i--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        notificationManager.cancel(NOTIFICATION_ID);
        isWorking = false;
        MainActivity.workerThread();


        Data outputData = new Data.Builder()
                .putInt("countdown_completed", 1)
                .build();

        return Result.success(outputData);
    }

//    public boolean checkIfWorking()
//    {
//        return isWorking;
//    }
}