package com.digiota.fotodirs.view;

/**
 * Created by jdiamand on 1/28/17.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.interfaces.ViewMvc;
import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.ButterKnife;

/**
 * Very simple MVC view containing just single FrameLayout
 */
public class MainViewMvcImpl implements ViewMvc {


    private Context mContext;

    private View mRootView;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    private Toolbar mToolbar;

    public MainViewMvcImpl(Context context, ViewGroup container) {
        mContext = context ;
        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_main, container);
        ButterKnife.bind((Activity)context) ;
        Fresco.initialize(context);
        ((Activity)context).setContentView(mRootView);
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
        // This MVC view has no state that could be retrieved
        return null;
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


    }
}
