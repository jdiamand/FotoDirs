package com.digiota.fotodirs.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.adapter.RecyclerViewAdapter;
import com.digiota.fotodirs.controller.MainActivity;
import com.digiota.fotodirs.interfaces.LocalFotosViewMvc;


/**
 * Created by jdiamand on 1/28/17.
 */

public class FotoGridFragmentViewMvcImpl implements LocalFotosViewMvc {
    private View mRootView;
    private RecyclerView mRecyclerView ;
    private RecyclerViewAdapter mvRecyclerViewAdapter ;
    private Context   mContext ;


    public FotoGridFragmentViewMvcImpl(Context context, ViewGroup container) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.fragment_photo_grid, container);
        mContext= context ;
        initialize();
    }



    void initialize() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);




    }

    public void setLayoutAdapter(RecyclerView.Adapter adapter) {
        RecyclerView rv = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

       Configuration config  = mContext.getResources().getConfiguration()     ;

        int rows = 2 ;
        //Log.d("FotoGridFragment", "width=" +  c.screenWidthDp + ",height=" +  c.screenHeightDp ) ;

        int width = config.screenWidthDp ;
        int height = config.screenHeightDp ;

        if (height > width) {
            // portrait mode
            if (width >= 360 ) {
                rows = 4 ;
            } else {
                rows = 2 ;
            }
        } else {

            // in landscape mode
            if (width >= 360) {
                rows = 6  ;
            } else {
                rows = 4 ;
            }
        }

        LinearLayoutManager llm = new GridLayoutManager(mContext, rows);

        rv.setLayoutManager(llm);

        SharedPreferences prefs = mContext.getSharedPreferences(
                 MainViewMvcImpl.PREFS_PACKAGE, Context.MODE_PRIVATE);

        int pictureOffset = prefs.getInt(MainViewMvcImpl.PICTURE_INDEX, 0 ) ;
        llm.scrollToPositionWithOffset(pictureOffset, 0);

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
