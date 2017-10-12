package com.digiota.fotodirs.controller;


import android.content.res.Configuration;
import android.support.v4.app.FragmentManager ;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.view.FotoDisplayViewMvcImpl;
import com.digiota.fotodirs.view.MainViewMvcImpl;


/**
 * Created by jdiamand on 10/9/17.
 */

public class FotoDisplayActivity extends AppCompatActivity implements View.OnTouchListener{
    FotoDisplayViewMvcImpl mFotoDisplayViewMVC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate MVC view associated with this activity
        mFotoDisplayViewMVC = new FotoDisplayViewMvcImpl(this, null,savedInstanceState);

        setSupportActionBar(mFotoDisplayViewMVC.getToolbar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        mFotoDisplayViewMVC.getAppName() ;
        getSupportActionBar().setSubtitle(mFotoDisplayViewMVC.getDirectoryName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        int resourceId  ;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            resourceId = R.id.overlay_fragment_container_portrait ;
        } else {
            resourceId = R.id.overlay_fragment_container_portrait ;
        }

        */
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();;
        ft.add(R.id.overlay_fragment_container, new FooFragment()).
        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit() ;





    }

    public String getCurrentFotoDate() {

        if (mFotoDisplayViewMVC.getCurrentFotoDate() != null) {
            return mFotoDisplayViewMVC.getCurrentFotoDate() ;
        }
        return "Unavailable" ;
    }

    public String getCurrentLongitude() {

        if (mFotoDisplayViewMVC.getCurrentLongitude() != null) {
            return mFotoDisplayViewMVC.getCurrentLongitude().toString() ;
        }
        return "Unavailable" ;
    }

    public String getCurrentLatitude() {

        if (mFotoDisplayViewMVC.getCurrentLatitude() != null) {
            return mFotoDisplayViewMVC.getCurrentLatitude().toString() ;
        }
        return "Unavailable" ;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mFotoDisplayViewMVC.scaleImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mFotoDisplayViewMVC.processItemSelected(item.getItemId()) ) {
            return true ;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        ImageView view = (ImageView) v;
        return true ;
    }

}
