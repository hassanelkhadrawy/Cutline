package com.system.cutline.Services;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.system.cutline.Core.CutlineApplication;
import com.system.cutline.Utils.PrefsConstants;
import com.system.cutline.Utils.PrefsUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteHistoryRunnable implements Runnable {

    private final CharSequence mTextToWrite;
    private boolean isQuestion;
    private OnCompleteWritingListener listener;
    private Context context;
    private File mHistoryFile;

    public WriteHistoryRunnable(CharSequence text, boolean isQuestion) {
        mTextToWrite = text;
        this.isQuestion = isQuestion;
        context = CutlineApplication.getInstance();
        mHistoryFile = new File(PrefsUtil.getString(PrefsConstants.FILE_PATH));
    }

    public void setOnCompleteWritingListener(OnCompleteWritingListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mTextToWrite)) {
            return;
        }

        if (isExternalStorageWritable()) {
            try {
                BufferedWriter writer =
                        new BufferedWriter(new FileWriter(mHistoryFile, true));
                if (isQuestion)
                    writer.newLine();
                writer.write(mTextToWrite.toString());
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                listener.onComplete();
            }
        }
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public interface OnCompleteWritingListener {
        public void onComplete();
    }
}
