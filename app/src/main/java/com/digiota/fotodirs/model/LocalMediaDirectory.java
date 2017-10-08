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
    Context mContext ;
    ContentResolver mContentResolver ;
    private final static String TAG = "LocalMediaDirectory" ;


    public Map<String, FotoDirInfo> getPictureDirectoriesInt() {
        return mPictureDirectoriesInt;
    }

    public Map<String, FotoDirInfo> getPictureDirectoriesExt() {
        return mPictureDirectoriesExt;  // follow to    <=============
    }
    Map<String, FotoDirInfo > mPictureDirectoriesInt  ;
    Map<String, FotoDirInfo > mPictureDirectoriesExt ;
    Map<String, FotoDirInfo > mPictureDirectoriesCloud ; // currently not implemented

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
        String[] extfiles = mPictureDirectoriesExt.keySet().toArray(     //here    <=============
                new String[mPictureDirectoriesExt.keySet().size()]);

        //ArrayUtils.addAll(intfiles,extfiles);
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
                        //MediaStore.Images.Media.DATE_ADDED + " DESC" // Order the results, newest first
                );
        //Log.d("IMAGEALL", "COUNT = " + cursor.getCount()) ;
        //List<String> result = new ArrayList<String>(cursor.getCount());

        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                String path = cursor.getString(image_path_col) ;
                //result.add(path);
                //Log.d("IMAGEALL", path ) ;
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




    /*
        void getImageFolders () {

            final String[] columns = {
                    //MediaStore.Images.Media.DATA,
                    //MediaStore.Images.Media._ID,
                    //MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            };
            final String orderBy = MediaStore.Images.Media.DISPLAY_NAME;

            Cursor cursor = mContentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);

            mExternalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();

            mCount = cursor.getCount();
    // mAbsolutePath is the absolute path (e.g. //emulated/0/folder/picture.png)
            mAbsolutePath = new String[mCount];
            mDisplayName = new String[mCount];
    // mFolderIntentPath is what will eventually become the folder path (e.g. //emulated/0/folder/)
            mFolderIntentPath = new String[mCount];
            mBucket = new String[mCount];
            mBucketId = new String[mCount];

            for (int i = 0; i < this.mCount; i++) {
                cursor.moveToPosition(i);
                //int absolutePathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                //int displayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int bucketColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

                //mAbsolutePath[i]= cursor.getString(absolutePathColumnIndex);
                //mDisplayName[i] = cursor.getString(displayNameColumnIndex);
                mBucket[i] = cursor.getString(bucketColumnIndex);
                mBucketId[i] = cursor.getString(bucketIdColumnIndex);
                //mFolderIntentPath[i] = mAbsolutePath[i].substring(0, mAbsolutePath[i].length() - mDisplayName[i].length());
                //mFolderIntentPath[i] = mAbsolutePath[i].substring(0, mAbsolutePath[i].lastIndexOf('/'));
            }
            String directoryPath = "/storage/emulated/0/DCIM" ;
            //String directoryPath = "/storage/sdcard/DCIM/" ;
            File[] files = new File(directoryPath).listFiles(new ImageFileFilter());
            ArrayList<String> items = new ArrayList<String>();
            for (File file : files) {

                // Add the directories containing images or sub-directories
                if (file.isDirectory()
                        && file.listFiles(new ImageFileFilter()).length > 0) {

                    items.add(file.getAbsolutePath());
                }
                // Add the images
                else {

                    //Bitmap image = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath(),
                    //        50,
                    //        50);
                    //items.add(new GridViewItem(file.getAbsolutePath(), false, image));

                }
            }



    }
    */




    /*
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            else if (isImageFile(file.getAbsolutePath())) {
                Log.d("IMAGEFILE",file.getAbsolutePath() ) ;
                return true;
            }
            return false;
        }
    }
    */
    /**
     * Checks the file to see if it has a compatible extension.
     */
    /*
    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }
    */
    /*
    void getImageFolders2 () {

        final String[] columns = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        final String orderBy = MediaStore.Images.Media.DISPLAY_NAME;

        Cursor cursor = mContentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);

        mExternalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        mCount = cursor.getCount();
// mAbsolutePath is the absolute path (e.g. //emulated/0/folder/picture.png)
        mAbsolutePath = new String[mCount];
        mDisplayName = new String[mCount];
// mFolderIntentPath is what will eventually become the folder path (e.g. //emulated/0/folder/)
        mFolderIntentPath = new String[mCount];
        mBucket = new String[mCount];

        for (int i = 0; i < this.mCount; i++) {
            cursor.moveToPosition(i);
            int absolutePathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int displayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int bucketColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            mAbsolutePath[i]= cursor.getString(absolutePathColumnIndex);
            mDisplayName[i] = cursor.getString(displayNameColumnIndex);
            mBucket[i] = cursor.getString(bucketColumnIndex);
            //mFolderIntentPath[i] = mAbsolutePath[i].substring(0, mAbsolutePath[i].length() - mDisplayName[i].length());
            mFolderIntentPath[i] = mAbsolutePath[i].substring(0, mAbsolutePath[i].lastIndexOf('/'));
        }



    }
    */


}
