package com.digiota.fotodirs.adapter;

import java.io.File;

/**
 * Created by jdiamand on 1/18/17.
 */

public class ItemObject {



    private File photoFile;


    public ItemObject(  File photoFile ) {

        this.photoFile = photoFile;
    }





    public File getPhotoFile() {
        return photoFile;
    }



}
