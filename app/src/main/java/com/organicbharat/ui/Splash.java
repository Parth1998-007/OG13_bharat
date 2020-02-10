package com.organicbharat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.organicbharat.R;
import com.organicbharat.ui.home.Home;
import com.organicbharat.ui.login.Login;
import com.organicbharat.utils.AppLog;


public class Splash extends Base {
    private static final String TAG = "Splash";
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoNext();
            }
        }, 2000);

    }

    private void gotoNext() {
        gotoActivity(getNextIntent(),true);
        finish();
    }

    private Intent getNextIntent() {

        if(appPref.isLogin()){
            return new Intent(this, Home.class);
        }
        else {
            return new Intent(this, Login.class);
        }
    }

    private void screenSize()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        AppLog.e(TAG,"width : "+width+", height : "+height);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = displayMetrics.heightPixels / density;
        float dpWidth  = displayMetrics.widthPixels / density;
        AppLog.e(TAG,"dpWidth : "+dpWidth+", dpHeight : "+dpHeight);
    }
}
