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
import androidx.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Intent notification = new Intent(this, Home.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("1", "PatechNotif", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notification_build = new NotificationCompat.Builder(this, "1");
            Notification notification1 = notification_build
                    .setSmallIcon(R.drawable.logo_patech)
                    .setContentTitle("TANAMAN BERPOTENSI ANTRAKNOSA!")
                    .setContentText("Segera cek kondisi tanaman cabai Anda")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//            notificationManager.notify(1, notification_build.build());

            startForeground(1, notification1);



            //notification manager

//            // Add as notification
//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(NotifServices.this);
//            notificationManagerCompat.notify(1, notification_build);


        } catch (Exception e){
            e.printStackTrace();
        }
//        return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }
}
