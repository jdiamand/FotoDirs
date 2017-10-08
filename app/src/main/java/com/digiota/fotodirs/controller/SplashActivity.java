package com.digiota.fotodirs.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.digiota.fotodirs.FotoDirsApplication;
import com.digiota.fotodirs.view.SplashViewMvcImpl;

/**
 * Created by jdiamand on 1/28/17.
 */

public class SplashActivity extends AppCompatActivity {

    SplashViewMvcImpl mViewMVC;
    Intent myIntent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewMVC = new SplashViewMvcImpl(this, null);


        int timeWait = 1500 ;
        if (!FotoDirsApplication.isSplashScreen()) {
            setContentView(mViewMVC.getRootView());
            FotoDirsApplication.setSplashScreen(true) ;
        } else {
            timeWait = 0 ;
        }

            startMainActivity( timeWait) ;





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void startMainActivity(int timeWait) {
        myIntent = new Intent(this, MainActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(myIntent);
                finish();
            }
        }, timeWait);

    }


}
