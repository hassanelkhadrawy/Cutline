package com.system.cutline.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.system.cutline.Core.CutlineApplication;
import com.system.cutline.Core.CutlineNotificationManager;
import com.system.cutline.Dialogs.QuestionsDialog;
import com.system.cutline.Utils.ActionsConstants;

public class ClipboardService extends Service {

    private ClipboardManager mClipboardManager;

    public ClipboardService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

    }

    @Override
    public void onDestroy() {

        CutlineNotificationManager.showNotRunningNotification();

        if (mClipboardManager != null) {
            mClipboardManager.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);
        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = CutlineApplication.getInstance().getApplicationContext();
        Intent serviceIntent = new Intent(context, ClipboardService.class);
        if (intent == null) context.startService(serviceIntent);
        else if (TextUtils.isEmpty(intent.getAction())) {
            CutlineNotificationManager.showRunningNotification();
            mClipboardManager.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);
            return START_STICKY;
        } else if (intent.getAction().equals(ActionsConstants.ACTION_START))
            context.startService(serviceIntent);
        else if (intent.getAction().equals(ActionsConstants.ACTION_STOP))
            context.stopService(serviceIntent);
        return START_STICKY;
    }

    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {

                    ClipData clip = mClipboardManager.getPrimaryClip();
                    showDialog(clip.getItemAt(0).getText().toString());
                }
            };

    private void showDialog(String text) {
        Intent i = new Intent(CutlineApplication.getInstance(), QuestionsDialog.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("txt", text);
        startActivity(i);
    }
}
