package com.system.cutline.Core;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.system.cutline.Utils.PrefsUtil;

public abstract class BaseActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int RequestPermissionCode = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initViews();
        initPrefs();
        onViewCreated();

        if (!CheckingPermissions()) {
            RequestPermissions();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PrefsUtil.getInstance(getSharedPreferencesName()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefsUtil.getInstance(getSharedPreferencesName()).unregisterOnSharedPreferenceChangeListener(this);
    }

    protected abstract int initLayout();

    protected abstract String getSharedPreferencesName();

    protected abstract void initPrefs();

    protected abstract void initViews();

    protected abstract void onViewCreated();


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        initPrefs();
    }

    protected boolean CheckingPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                    SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    protected void RequestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean Read = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean Write = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (!(Read && Write))
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
