package com.semonics;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.semonics.tworld.R;


/**
 * Author : Iconflux Technology  * Purpose: Display notification to notification bar
 */
public class MyNotificationManager {
    private static int ID_SMALL_NOTIFICATION = 235;
    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }     /*The method will show a small notification     parameters are title for message title, message for message text and an intent that will open     when you will tap on the notification*/

    public void showSmallNotification(String message, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mCtx.getPackageName(), "TWorld", importance);
            channel.setDescription(message);
           // Register the channel with the notifications manager
            NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);
        } ID_SMALL_NOTIFICATION = ID_SMALL_NOTIFICATION + 1;
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, ID_SMALL_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, mCtx.getPackageName());
        Notification notification;
        notification = mBuilder.setWhen(0).setAutoCancel(true).setContentIntent(resultPendingIntent).setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentTitle("TWorld").setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher)).setSound(defaultSoundUri).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }
}

