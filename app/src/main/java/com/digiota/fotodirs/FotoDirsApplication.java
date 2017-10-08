package com.digiota.fotodirs;

import android.app.Application;
import android.content.Context;

import com.digiota.fotodirs.model.LocalMediaDirectory;
import com.digiota.fotodirs.model.MessageEvent;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jdiamand on 1/16/17.
 */

public class FotoDirsApplication extends Application {

    static private Context mContext ;
    static private boolean mSplashScreen ;
    static private LocalMediaDirectory mLocalMediaDirectory  = null ;

    public static LocalMediaDirectory getLocalMediaDirectory() {

        return mLocalMediaDirectory;
    }

    public static void setLocalMediaDirectory(LocalMediaDirectory localMediaDirectory) {
        mLocalMediaDirectory    = localMediaDirectory;
    }


    public static boolean isSplashScreen() {
        return mSplashScreen;
    }
    public static void setSplashScreen(boolean mSplashScreen) {
        FotoDirsApplication.mSplashScreen = mSplashScreen;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //Fresco.initialize(this);

        mContext = getApplicationContext() ;

    }

     public static void initLocalMediaDirectory () {


         if (mLocalMediaDirectory == null )  {
             mLocalMediaDirectory = new LocalMediaDirectory(mContext) ;
             EventBus.getDefault().post(new MessageEvent("InitLocalMediaDir"));
         }

     }


}
