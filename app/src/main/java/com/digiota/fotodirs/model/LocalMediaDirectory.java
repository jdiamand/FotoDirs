package com.digiota.fotodirs.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdiamand on 1/28/17.
 */
/*
Provides a store of the local media dirs on the phone that contain
photos
These dirs will be menu item in the local section of the flyout menu
 */
public class LocalMediaDirectory {
    private final static String TAG = "LocalMediaDirectory" ;
    private Context mContext ;
    private ContentResolver mContentResolver ;
    private Map<String, FotoDirInfo > mPictureDirectoriesInt  ;
    private Map<String, FotoDirInfo > mPictureDirectoriesExt ;
    private Map<String, FotoDirInfo > mPictureDirectoriesCloud ; // currently not implemented

    public Map<String, FotoDirInfo> getPictureDirectoriesInt() {
        return mPictureDirectoriesInt;
    }

    public Map<String, FotoDirInfo> getPictureDirectoriesExt() {
        return mPictureDirectoriesExt;
    }

    public LocalMediaDirectory(Context context) {
        mContext = context ;
        mContentResolver = mContext.getContentResolver() ;
        mPictureDirectoriesInt = new HashMap<String, FotoDirInfo>();
        mPictureDirectoriesExt = new HashMap<String, FotoDirInfo>();
        try {
            getImagePaths(mContext);
        } catch (SecurityException e) {
            Log.d(TAG,e.toString()) ;
        }

    }

    public String[] getFolderNames() {


        String[] intfiles = mPictureDirectoriesInt.keySet().toArray(
                new String[mPictureDirectoriesInt.keySet().size()]);
        String[] extfiles = mPictureDirectoriesExt.keySet().toArray(
                new String[mPictureDirectoriesExt.keySet().size()]);

        return ArrayUtils.addAll(intfiles,extfiles) ;


    }

    public String getFolderNameAtIndex(int indx) {

        if (getFolderNames().length > indx) {
            String folderName = getFolderNames()[indx];
            int indxSlash = folderName.lastIndexOf('/');

            return folderName.substring(indxSlash) ;
        }
        return "" ;

    }

    public String getFullFolderNameAtIndex(int indx) {

        if (getFolderNames().length > indx) {
            return  getFolderNames()[indx] ;
        }
        return "" ;

    }


    public void getImagePaths(Context context) {
        // The list of columns we're interested in:
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};
        final String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
        final Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // Specify the provider
                        columns, // The columns we're interested in
                        null, // A WHERE-filter query
                        null, // The arguments for the filter-query
                        orderBy

                );

        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                String path = cursor.getString(image_path_col) ;

                String[] parts = path.split("/");
                int pIndx = path.lastIndexOf( '/' ) ;
                String truncatedPath = path.substring(0,pIndx) ;
                String fileName = path.substring(pIndx + 1) ;
                FotoDirInfo fotoDirInfo = new FotoDirInfo(parts[parts.length - 2],1) ;
                FotoFileInfo fotoFileInfo = new FotoFileInfo(fileName) ;
                if (parts[2].equals("emulated")) {
                    if (mPictureDirectoriesInt.containsKey(truncatedPath)) {
                        fotoDirInfo = mPictureDirectoriesInt.get(truncatedPath) ;
                        fotoDirInfo.incrementCount();
                    }
                    fotoDirInfo.fotoFileInfoList.add(fotoFileInfo) ;
                    mPictureDirectoriesInt.put(truncatedPath,fotoDirInfo ) ;

                } else {
                    if (mPictureDirectoriesExt.containsKey(truncatedPath)) {
                        fotoDirInfo = mPictureDirectoriesExt.get(truncatedPath) ;
                        fotoDirInfo.incrementCount();
                    }
                    fotoDirInfo.fotoFileInfoList.add(fotoFileInfo) ;
                    mPictureDirectoriesExt.put(truncatedPath,fotoDirInfo ) ;
                }

                //Log.d("IMAGEALL",  truncatedPath + ":" +  parts[parts.length - 2] ) ;
                //}
            } while (cursor.moveToNext());
        }
        cursor.close();

        return ;
    }

}
