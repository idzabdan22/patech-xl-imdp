package com.example.xl_imdp_patech.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.xl_imdp_patech.R;
import com.example.xl_imdp_patech.main.Home;

public class NotifServices extends android.app.Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            Intent notification = new Intent(this, Home.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, 0);
            notification.putExtra("code", 1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("1", "PatechNotif", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification_build = new NotificationCompat.Builder(NotifServices.this, "notification")
                    .setContentTitle("TANAMAN BERPOTENSI ANTRAKNOSA!")
                    .setSmallIcon(R.drawable.logo_patech)
                    .setContentText("Segera cek kondisi tanaman cabai Anda")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .build();


            startForeground(1, notification_build);


            //notification manager

//            // Add as notification
//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(NotifServices.this);
//            notificationManagerCompat.notify(1, notification_build);


        } catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
//        return START_STICKY;
    }
}
