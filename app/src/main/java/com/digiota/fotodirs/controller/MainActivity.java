package com.digiota.fotodirs.controller;

import android.Manifest;
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
import android.view.ViewParent;
import android.widget.Toast;

import com.digiota.fotodirs.FotoDirsApplication;
import com.digiota.fotodirs.R;
import com.digiota.fotodirs.model.LocalMediaDirectory;
import com.digiota.fotodirs.view.MainViewMvcImpl;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    MainViewMvcImpl mViewMVC;
    private final static int TAG_CODE_READ_EXTERNAL_STORAGE = 1;

    private FragmentDrawer mDrawerFragment;
    boolean waiting = false ;
    public static final String FOLDER_INDEX = "folder_index" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null) {
            restoreDirectory(savedInstanceState);

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
        displayView(0);

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

            displayView(position) ;
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String subTitle = "/directory" ;
        switch (position) {
            case 0:
            case 1:
            default :
                Bundle bundl = new Bundle();
                bundl.putInt(FOLDER_INDEX, position);

                fragment = new FotoGridFragment();
                fragment.setArguments(bundl);
                LocalMediaDirectory localMediaDirectory = FotoDirsApplication.getLocalMediaDirectory();

                if (localMediaDirectory != null) {
                    subTitle = localMediaDirectory.getFolderNameAtIndex(position);
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
        waiting = true ;
        saveState.putBoolean("waiting",waiting);
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
        waiting=savedInstanceState.getBoolean("waiting");
        if (waiting) {
            /*
            AppClass app=(AppClass) getApplication();
            ProgressDialog refresher=(ProgressDialog) app.Dialog.get();
            refresher.dismiss();
            String logingon=getString(R.string.signon);
            app.Dialog=new WeakReference<ProgressDialog>(ProgressDialog.show(AddAccount.this, "", logingon, true));
            */
            Toast.makeText(this, "dir restored", Toast.LENGTH_LONG).show();

        }

    }
}
