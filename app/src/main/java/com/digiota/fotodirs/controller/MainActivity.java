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

    MainViewMvcImpl mViewMVC;
    private final static int TAG_CODE_READ_EXTERNAL_STORAGE = 1;

    private int mDirectoryIndex  ;

    private FragmentDrawer mDrawerFragment;
    public static final String FOLDER_INDEX   = "folder_index" ;
    public static final String PICTURE_INDEX  = "picture_index" ;
    public static final String PREFS_PACKAGE  = "com.digiota.fotodirs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int pictureOffset = 0 ;
        SharedPreferences prefs = this.getSharedPreferences(
                PREFS_PACKAGE, Context.MODE_PRIVATE);
        if(savedInstanceState!=null) {
            restoreDirectory(savedInstanceState);
            pictureOffset = prefs.getInt(PICTURE_INDEX, 0 ) ;
        } else {
            mDirectoryIndex = 0 ;

            prefs.edit().putInt(PICTURE_INDEX, 0).apply();

        }
        // Instantiate MVC view associated with this activity
        mViewMVC = new MainViewMvcImpl(this, null);

        int permissionCheck = -1 ;
        // Assume thisActivity is the current activity
        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck == 0 ) {
            FotoDirsApplication.initLocalMediaDirectory()  ;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    TAG_CODE_READ_EXTERNAL_STORAGE);
        }





        setSupportActionBar(mViewMVC.getToolbar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerFragment = (FragmentDrawer)
                getSupportFragmentManager().
                        findFragmentById( mViewMVC.getNaviDrawerId()   );

        mDrawerFragment.setUp(mViewMVC.getNaviDrawerId(),
                (DrawerLayout) findViewById(mViewMVC.getDrawerLayout()), mViewMVC.getToolbar());
        mDrawerFragment.setDrawerListener(this);
        displayDirectory(mDirectoryIndex,pictureOffset);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mViewMVC.CreateOptionsMenu(menu) ;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mViewMVC.SelectOptionsMenu(item.getItemId())) {
            return true ;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {

            int pictureOffset = 0 ;
            if (position == mDirectoryIndex) {

                SharedPreferences prefs = this.getSharedPreferences(
                        PREFS_PACKAGE, Context.MODE_PRIVATE);
                pictureOffset = prefs.getInt(PICTURE_INDEX, 0 ) ;
            }
        displayDirectory(position, pictureOffset);
    }

    private void displayDirectory(int directoryIndex, int pictureOffset) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String subTitle = "/directory" ;
        mDirectoryIndex = directoryIndex ;
        switch (directoryIndex) {
            case 0:
            case 1:
            default :
                Bundle bundl = new Bundle();
                bundl.putInt(FOLDER_INDEX, directoryIndex);
                SharedPreferences prefs = this.getSharedPreferences(
                        PREFS_PACKAGE, Context.MODE_PRIVATE);
                prefs.edit().putInt(PICTURE_INDEX, pictureOffset);
                fragment = new FotoGridFragment();
                fragment.setArguments(bundl);
                LocalMediaDirectory localMediaDirectory = FotoDirsApplication.getLocalMediaDirectory();

                if (localMediaDirectory != null) {
                    subTitle = localMediaDirectory.getFolderNameAtIndex(directoryIndex);
                }
                break;

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(mViewMVC.getContainerBodyId() , fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        saveState.putInt(FOLDER_INDEX, mDirectoryIndex);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        int indx = 0 ;
        for ( String p : permissions) {
            if (p.equals(Manifest.permission.READ_EXTERNAL_STORAGE) ) {

               if (grantResults[indx] == 0) {
                   FotoDirsApplication.initLocalMediaDirectory()  ;
                   return ;
               }

            }
            ++indx ;
        }

    }

    private void restoreDirectory(Bundle savedInstanceState) {

        mDirectoryIndex = savedInstanceState.getInt(FOLDER_INDEX,0) ;

    }
}
