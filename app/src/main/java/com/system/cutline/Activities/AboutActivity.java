package com.system.cutline.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.system.cutline.Core.BaseActivity;
import com.system.cutline.R;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private FloatingActionButton facebook, google, youtube;

    private String facebookPageID = "Softizone1";
    private String facebookUrl = "https://www.facebook.com/" + facebookPageID;
    private String facebookUrlScheme = "fb://page/" + facebookPageID;
    private String googlePlusProfile = "11727868402275895030";
    private String googlePlusUrl = "https://plus.google.com/" + googlePlusProfile + "1/posts";
    private String youtubeUrl = "https://www.youtube.com/channel/UC2jlvnSH4uT04ShIM79l4cg";

    @Override
    protected int initLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected String getSharedPreferencesName() {
        return null;
    }

    @Override
    protected void initPrefs() {
    }

    @Override
    protected void initViews() {
        facebook = findViewById(R.id.facebook);
        google = findViewById(R.id.google);
        youtube = findViewById(R.id.youtube);
    }

    @Override
    protected void onViewCreated() {
        facebook.setOnClickListener(this);
        youtube.setOnClickListener(this);
        google.setOnClickListener(this);
    }

    private void onGoogleClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(googlePlusUrl));
        startActivity(intent);
        /*
        try {
            if (getPackageManager().getPackageInfo("com.google.android.apps.plus", 0) != null) {
                intent.setPackage("com.google.android.apps.plus");
                intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
                intent.putExtra("customAppUri", googlePlusProfile);
            }
        } catch (PackageManager.NameNotFoundException e) {
            intent.setData(Uri.parse(googlePlusUrl));
        }finally {
            startActivity(intent);
        }/*
        try {
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", googlePlusProfile);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent.setData(Uri.parse(googlePlusUrl));
            startActivity(intent);
        }*/
    }

    private void onFacebookClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850)
                intent.setData(Uri.parse("fb://facewebmodal/f?href=" + facebookUrl));
            else
                intent.setData(Uri.parse(facebookUrlScheme));

        } catch (PackageManager.NameNotFoundException e) {
            intent.setData(Uri.parse(facebookUrl));
        } finally {
            startActivity(intent);
        }
    }

    private void onYoutubeClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse(youtubeUrl));
        } catch (PackageManager.NameNotFoundException e) {
            intent.setData(Uri.parse(youtubeUrl));
        } finally {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(facebook))
            onFacebookClicked(v);
        else if (v.equals(google))
            onGoogleClicked(v);
        else if (v.equals(youtube))
            onYoutubeClicked(v);
    }
}
