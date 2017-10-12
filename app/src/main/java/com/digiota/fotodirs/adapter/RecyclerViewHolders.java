package com.digiota.fotodirs.adapter;

import android.content.Intent;

import android.support.v7.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


import com.digiota.fotodirs.R;
import com.digiota.fotodirs.controller.FotoDisplayActivity;
import com.digiota.fotodirs.controller.MainActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

/**
 * Created by jdiamand on 1/18/17.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder  implements View.OnClickListener {


    public static final String SELECTED_PIC_FILEPATH = "selected_file_path" ;
    public static final String SELECTED_PIC_DIRPATH  = "selected_directory_path" ;
    //public ImageView

    public SimpleDraweeView photo;
    private String mCurrentDirectory  ;
    RecyclerViewAdapter mRecyclerViewAdapter ;

    public RecyclerViewHolders(View itemView, RecyclerViewAdapter recyclerViewAdapter) {
        super(itemView);

        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        mRecyclerViewAdapter = recyclerViewAdapter ;
        mCurrentDirectory = ((MainActivity)(mRecyclerViewAdapter).getContext()).getMainViewMvcImpl().getDirectoryName()  ;

        photo = (SimpleDraweeView)itemView.findViewById(R.id.photoResource);
    }

    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public void onClick(View view) {
        int pos = getAdapterPosition()  ;
        Intent myIntent = new Intent(itemView.getContext(),FotoDisplayActivity.class);
        ItemObject item = (ItemObject)mRecyclerViewAdapter.getObectAtIndex(pos) ;
        File photoFile = item.getPhotoFile() ;
        String s = photoFile.getAbsolutePath() ;
        myIntent.putExtra(SELECTED_PIC_FILEPATH, s);
        myIntent.putExtra(SELECTED_PIC_DIRPATH, mCurrentDirectory);
        itemView.getContext().startActivity(myIntent);

    }



}
