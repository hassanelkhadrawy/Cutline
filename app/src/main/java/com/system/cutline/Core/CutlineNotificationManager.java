package com.system.cutline.Core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


import com.system.cutline.Activities.MainActivity;
import com.system.cutline.Core.CutlineApplication;
import com.system.cutline.R;
import com.system.cutline.Services.ClipboardService;
import com.system.cutline.Utils.ActionsConstants;
import com.system.cutline.Utils.PrefsUtil;


public class CutlineNotificationManager {

    private static final int NOTIFICATION_ID = 101;
    private static Context context = CutlineApplication.getInstance();

    public static void showRunningNotification() {

        //  clearNotification();
        Intent stopIntent = new Intent(context, ClipboardService.class);
        stopIntent.setAction(ActionsConstants.ACTION_STOP);
        PendingIntent piStop = PendingIntent.getService(context, 0, stopIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.addAction(R.drawable.ic_null, context.getString(R.string.stop), piStop)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.running_text))
                .setTicker(context.getString(R.string.running_text))
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_MAX);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        NotificationManager mNotificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, notification);
        PrefsUtil.saveString("SERVICE", "ON");
    }

    public static void showNotRunningNotification() {
        //  clearNotification();
        Intent startIntent = new Intent(context, ClipboardService.class);
        startIntent.setAction(ActionsConstants.ACTION_START);
        PendingIntent piStart = PendingIntent.getService(context, 0, startIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .addAction(R.drawable.ic_null, context.getString(R.string.start), piStart)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.not_running_text))
                .setTicker(context.getString(R.string.not_running_text))
                .setPriority(Notification.PRIORITY_LOW);

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        Notification notification = mBuilder.build();
        NotificationManager mNotificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, notification);
        PrefsUtil.saveString("SERVICE", "OFF");
    }

    public static void clearNotification() {
        try {
            NotificationManager mNotificationManager = (NotificationManager) context.
                    getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.cancel(NOTIFICATION_ID);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
