package com.digiota.fotodirs.controller;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.digiota.fotodirs.FotoDirsApplication;
import com.digiota.fotodirs.R;
import com.digiota.fotodirs.model.LocalMediaDirectory;
import com.digiota.fotodirs.view.MainViewMvcImpl;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private MainViewMvcImpl mViewMVC;
    private final static int TAG_CODE_READ_EXTERNAL_STORAGE = 1;


    public MainViewMvcImpl getMainViewMvcImpl() { return mViewMVC;} ;

    private FragmentDrawer mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Instantiate MVC view associated with this activity
        mViewMVC = new MainViewMvcImpl(this, null, savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == 0) {

            FotoDirsApplication.initLocalMediaDirectory();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    TAG_CODE_READ_EXTERNAL_STORAGE);
        }


        setSupportActionBar(mViewMVC.getToolbar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerFragment = (FragmentDrawer)
                getSupportFragmentManager().
                        findFragmentById(mViewMVC.getNaviDrawerId());

        mDrawerFragment.setUp(mViewMVC.getNaviDrawerId(),
                (DrawerLayout) findViewById(mViewMVC.getDrawerLayout()), mViewMVC.getToolbar());
        mDrawerFragment.setDrawerListener(this);

        displayDirectory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  mViewMVC.CreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mViewMVC.SelectOptionsMenu(item.getItemId())) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        mViewMVC.setFolderIndex(position);

        displayDirectory();
    }


    private void displayDirectory() {

        String title = getString(R.string.app_name);
        String subTitle = "/directory";

        mViewMVC.displayDirectory(getSupportFragmentManager());


        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(mViewMVC.getDirectoryName());
    }

    @Override
    protected void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);

        mViewMVC.saveState(saveState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        int indx = 0;
        for (String p : permissions) {
            if (p.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                if (grantResults[indx] == 0) {
                    FotoDirsApplication.initLocalMediaDirectory();
                    return;
                }

            }
            ++indx;
        }

    }
}
