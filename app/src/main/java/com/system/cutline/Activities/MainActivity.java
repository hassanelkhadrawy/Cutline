package com.system.cutline.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;
import com.system.cutline.Core.BaseActivity;
import com.system.cutline.Core.CutlineApplication;
import com.system.cutline.R;
import com.system.cutline.Services.ClipboardService;
import com.system.cutline.Utils.KeyboardUtil;
import com.system.cutline.Utils.PrefsConstants;
import com.system.cutline.Utils.PrefsUtil;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView background, about;
    private SwitchCompat mService;
    private LinearLayout fileLayout, languageLayout;
    private TextView fileValue, languageValue;

    @Override
    protected void initPrefs() {
        String lan = PrefsUtil.getString(PrefsConstants.LANGUAGE);
        if (TextUtils.isEmpty(lan)) {
            lan = PrefsConstants.ENGLISH;
            PrefsUtil.saveString(PrefsConstants.LANGUAGE, lan);
        }
        if (lan.equals(PrefsConstants.ENGLISH))
            languageValue.setText(R.string.english);
        else if (lan.equals(PrefsConstants.ARABIC))
            languageValue.setText(R.string.arabic);

        String filePath = PrefsUtil.getString(PrefsConstants.FILE_PATH);
        if (!TextUtils.isEmpty(filePath))
            fileValue.setText(filePath);

        applySwitchChanges(CutlineApplication.getInstance().isMyServiceRunning(ClipboardService.class));
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected String getSharedPreferencesName() {
        return null;
    }


    @Override
    protected void initViews() {
        background = findViewById(R.id.background);
        about = findViewById(R.id.about);
        mService = findViewById(R.id.service_switch);
        fileLayout = findViewById(R.id.file_layout);
        fileValue = findViewById(R.id.file_value);
        languageLayout = findViewById(R.id.language_layout);
        languageValue = findViewById(R.id.language_value);
    }

    @Override
    protected void onViewCreated() {
        languageLayout.setOnClickListener(this);
        fileLayout.setOnClickListener(this);
        mService.setOnCheckedChangeListener(this);
        about.setOnClickListener(this);
    }

    private void applySwitchChanges(boolean isChecked) {
        Intent i = new Intent(this, ClipboardService.class);
        if (isChecked) {
            if (TextUtils.isEmpty(PrefsUtil.getString(PrefsConstants.FILE_PATH))) {
                Toast.makeText(this, getString(R.string.choose_file_to_write),
                        Toast.LENGTH_SHORT).show();
                applyColors(false);
                mService.setChecked(false);
                return;
            }
            if (!CutlineApplication.getInstance().isMyServiceRunning(ClipboardService.class))
                startService(i);
            applyColors(true);
            mService.setChecked(true);

        } else {
            if (CutlineApplication.getInstance().isMyServiceRunning(ClipboardService.class))
                stopService(i);
            applyColors(false);
            mService.setChecked(false);
        }

    }

    private void applyColors(boolean isChecked) {
        if (isChecked) {
            background.setImageResource(R.drawable.running_service);
            if (Build.VERSION.SDK_INT >= 21)
                getWindow().setStatusBarColor(getResources().getColor(R.color.bgConnectedTob));
        } else {
            background.setImageResource(R.drawable.not_running_service);
            if (Build.VERSION.SDK_INT >= 21)
                getWindow().setStatusBarColor(getResources().getColor(R.color.bgDisconnectedTob));
        }
    }

    private void openFile() {

        String lastPath = PrefsUtil.getString(PrefsConstants.FILE_PATH);
        if (TextUtils.isEmpty(lastPath))
            lastPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath();
        new ChooserDialog().with(MainActivity.this)
                .withFilter(false, false, "txt")
                .withStartFile(lastPath)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        PrefsUtil.saveString(PrefsConstants.FILE_PATH, path);
                    }
                })
                .build()
                .show();
    }

    private void createFile() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(getString(R.string.file_name));
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(input.getText().toString().trim())) {
                            Toast.makeText(MainActivity.this, getString(R.string.enter_file_name),
                                    Toast.LENGTH_SHORT).show();
                            input.requestFocus();
                            KeyboardUtil.showSoftKeyboard(input);
                            return;
                        }

                        String lastPath = PrefsUtil.getString(PrefsConstants.FILE_PATH);
                        if (TextUtils.isEmpty(lastPath))
                            lastPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .getAbsolutePath();
                        new ChooserDialog().with(MainActivity.this)
                                .withFilter(true, false)
                                .withStartFile(lastPath)
                                .withChosenListener(new ChooserDialog.Result() {
                                    @Override
                                    public void onChoosePath(String path, File pathFile) {
                                        File newFile = new File(path + File.separator + input.getText().toString() + ".txt");
                                        try {
                                            if (newFile.createNewFile())
                                                PrefsUtil.saveString(PrefsConstants.FILE_PATH, newFile.getAbsolutePath());
                                            else
                                                Toast.makeText(MainActivity.this, getString(R.string.choose_folder_to_select), Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .build()
                                .show();
                    }
                });
        alertDialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    @Override
    public void onClick(View view) {
        if (view.equals(languageLayout)) {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this,
                    findViewById(R.id.language_txt));
            popupMenu.getMenuInflater().inflate(R.menu.language_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.en)
                        PrefsUtil.saveString(PrefsConstants.LANGUAGE, PrefsConstants.ENGLISH);
                    else if (item.getItemId() == R.id.ar)
                        PrefsUtil.saveString(PrefsConstants.LANGUAGE, PrefsConstants.ARABIC);
                    return true;
                }
            });
            popupMenu.show();
        } else if (view.equals(fileLayout))
            if (!CheckingPermissions())
                RequestPermissions();
            else {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,
                        findViewById(R.id.file_txt));
                popupMenu.getMenuInflater().inflate(R.menu.file_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.new_file)
                            createFile();
                        else if (item.getItemId() == R.id.open_file)
                            openFile();
                        return true;
                    }
                });
                popupMenu.show();
            }
        else if (view.equals(about))
            startActivity(new Intent(MainActivity.this, AboutActivity.class));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        applySwitchChanges(isChecked);
    }
}
