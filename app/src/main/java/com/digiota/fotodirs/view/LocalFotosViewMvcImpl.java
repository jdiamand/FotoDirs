package com.digiota.fotodirs.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.interfaces.LocalFotosViewMvc;

/**
 * Created by jdiamand on 1/28/17.
 */

public class LocalFotosViewMvcImpl implements LocalFotosViewMvc {


    private View mRootView;



    public LocalFotosViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.activity_main, container, false);


    }



    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }


}
