package com.digiota.fotodirs.adapter;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by jdiamand on 1/18/17
 *
 * 
 */

public class ItemObject implements Comparable<ItemObject>{



    private File photoFile;


    public ItemObject(  File photoFile ) {

        this.photoFile = photoFile;
    }





    public File getPhotoFile() {
        return photoFile;
    }


    @Override
    public int compareTo(@NonNull ItemObject o) {
        return this.photoFile.toString().compareToIgnoreCase(o.photoFile.toString());
    }
}
