package com.digiota.fotodirs.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.view.FotoDisplayViewMvcImpl;

import static com.digiota.fotodirs.view.FotoDisplayViewMvcImpl.INFO_WINDOW_SHOW;


/**
 * Created by jdiamand on 10/9/17.
 */

public class FotoDisplayActivity extends AppCompatActivity  /*implements View.OnTouchListener*/{
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

        mFotoDisplayViewMVC.drawImage();



    }



    @Override
    protected void onResume() {

        super.onResume();
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

    public String getFilename() {

        String retVal = "Unavailable" ;
        if (mFotoDisplayViewMVC.getFilename() != null) {

            int indx = mFotoDisplayViewMVC.getFilename().lastIndexOf("/") ;

            if (indx == -1 ) {
                retVal = mFotoDisplayViewMVC.getFilename() ;
            } else {
                retVal = mFotoDisplayViewMVC.getFilename().substring(indx) ;
            }

        }
        return retVal ;
    }

    public String getPathName() {

        String retVal = "Unavailable" ;
        if (mFotoDisplayViewMVC.getFilename() != null) {

            int indx = mFotoDisplayViewMVC.getFilename().lastIndexOf('/') ;

            if (indx < 0) {
                retVal = mFotoDisplayViewMVC.getFilename() ;
            } else {
                retVal = mFotoDisplayViewMVC.getFilename().substring(0,indx) ;
            }

            return retVal  ;
        }
        return retVal ;
    }

    public int getAngle() {


        return  mFotoDisplayViewMVC.getAngle();
    }

    public String getResolution() {

        return  mFotoDisplayViewMVC.getResolution();
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
    public boolean onCreateOptionsMenu(Menu menu) {

        return mFotoDisplayViewMVC.createMenu(menu) ;
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INFO_WINDOW_SHOW, mFotoDisplayViewMVC.getInfoFragmentAdd());
    }



}
