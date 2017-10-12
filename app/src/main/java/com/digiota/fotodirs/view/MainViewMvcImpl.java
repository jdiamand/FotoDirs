package com.digiota.fotodirs.view;

/**
 * Created by jdiamand on 1/28/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.FotoDirsApplication;
import com.digiota.fotodirs.R;
import com.digiota.fotodirs.controller.FotoGridFragment;
import com.digiota.fotodirs.interfaces.MainViewMvc;
import com.digiota.fotodirs.model.LocalMediaDirectory;
import com.facebook.drawee.backends.pipeline.Fresco;




/**
 * Very simple MVC view containing just single FrameLayout
 */
public class MainViewMvcImpl implements MainViewMvc {


    Toolbar mToolbar;

    public static final String FOLDER_INDEX = "folder_index";
    public static final String PICTURE_INDEX = "picture_index";
    public static final String PREFS_PACKAGE = "com.digiota.fotodirs";


    private Context mContext;
    private Bundle  mBundle  ;
    private View mRootView;

    private int mFolderIndex;
    private int mPictureIndex  ;

    private String mDirectoryName  ;
    public Toolbar getToolbar() {
        return mToolbar;
    }

    public String getDirectoryName() {
        return mDirectoryName;
    }

    public MainViewMvcImpl(Context context, ViewGroup container, Bundle bundle) {
        mContext = context ;
        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_main, container);
        mBundle  = bundle ;
        ((Activity)context).setContentView(mRootView);

        Fresco.initialize(context);

        initialize();
    }

    public int getNaviDrawerId() {
         return R.id.fragment_navigation_drawer;
    }

    public int getDrawerLayout() {
        return R.id.drawer_layout ;
    }



    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return mBundle;
    }

    public void setViewState(Bundle bundle) {
        mBundle = bundle ;
    }

    public void CreateOptionsMenu(Menu menu) {

        MenuInflater mInflater = new MenuInflater(mContext);
        mInflater.inflate(R.menu.menu_main, menu);

    }

    public boolean SelectOptionsMenu(int itemId) {



        if (itemId == R.id.action_settings) {
            return true;
        }

        return false ;
    }

    public int getContainerBodyId() {
        return R.id.container_body ;

    }

    void initialize() {
        mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);

        SharedPreferences prefs = mContext.getSharedPreferences(
                PREFS_PACKAGE, Context.MODE_PRIVATE);
        if(mBundle!=null) {
            restoreDirectory(mBundle);
        } else {
            mFolderIndex = 0 ;

            prefs.edit().putInt(PICTURE_INDEX, 0).apply();

        }

    }

    private void restoreDirectory(Bundle savedInstanceState) {

        mFolderIndex = savedInstanceState.getInt(FOLDER_INDEX,0) ;

    }

    public int getFolderIndex() {


        return mFolderIndex  ;

    }

    public int setFolderIndex(int folderIndex) {

        if (mFolderIndex != folderIndex) {
            mPictureIndex = 0 ;
        }
        mFolderIndex = folderIndex ;
        return 0 ;

    }




    public int getPictureIndex() {


        return mPictureIndex ;


    }
    public void saveState(Bundle saveState) {
        saveState.putInt(FOLDER_INDEX, mFolderIndex);
        setViewState(saveState) ;
    }

    public void  displayDirectory(FragmentManager fragmentManager) {
        Fragment fragment = null;
        String title = mContext.getString(R.string.app_name);


        int directoryIndex =  getFolderIndex();




        Bundle bundl = new Bundle();
        bundl.putInt(FOLDER_INDEX, directoryIndex);
        SharedPreferences prefs = mContext.getSharedPreferences(
                PREFS_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putInt(PICTURE_INDEX,  getPictureIndex());

        fragment = new FotoGridFragment();
        fragment.setArguments(bundl);
        LocalMediaDirectory localMediaDirectory = FotoDirsApplication.getLocalMediaDirectory();

        if (localMediaDirectory != null) {
            mDirectoryName = localMediaDirectory.getFolderNameAtIndex(directoryIndex);
        }


        if (fragment != null) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(getContainerBodyId(), fragment);
            fragmentTransaction.commit();

        }


    }


}
