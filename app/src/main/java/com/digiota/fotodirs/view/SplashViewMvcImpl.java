package com.digiota.fotodirs.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.interfaces.ViewMvc;

/**
 * Created by jdiamand on 1/29/17.
 */

public class SplashViewMvcImpl implements ViewMvc {


    Context mContext ;
    private View mRootView;



    public SplashViewMvcImpl(Context context, ViewGroup container) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_splash, container);
        mContext = context ;
        initialize();
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

    void initialize() {

        TextView textView = (TextView) mRootView.findViewById(R.id.version_number);
        textView.setText("Version " + mRootView.getResources().getString(R.string.major_version) + "." + mRootView.getResources().getString(R.string.minor_version));

    }
}
