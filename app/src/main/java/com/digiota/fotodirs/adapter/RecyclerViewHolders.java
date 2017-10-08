package com.digiota.fotodirs.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.digiota.fotodirs.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by jdiamand on 1/18/17.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{



    //public ImageView

    public SimpleDraweeView photo;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);

        photo = (SimpleDraweeView)itemView.findViewById(R.id.photoResource);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void onClick(View view) {
        int pos = getPosition() ;
    }
}
