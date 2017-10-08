package com.digiota.fotodirs.adapter;

import java.io.File;

/**
 * Created by jdiamand on 1/18/17.
 */

public class ItemObject {


    //private int photoResource;
    private File photoFile;

    //public ItemObject( int photoResource, File photoFile ) {
    public ItemObject(  File photoFile ) {
        //this.photoResource = photoResource;
        this.photoFile = photoFile;
    }





    public File getPhotoFile() {
        return photoFile;
    }

/*
    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }
    public int getPhotoResource() {
        return photoResource;
    }
    public void setPhotoResource(int photoResource) {
        this.photoResource = photoResource;
    }
*/


}
