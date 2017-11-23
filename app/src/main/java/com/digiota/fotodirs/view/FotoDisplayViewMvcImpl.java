package com.digiota.fotodirs.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.digiota.fotodirs.R;
import com.digiota.fotodirs.controller.FotoDisplayActivity;
import com.digiota.fotodirs.controller.ImageInfoFragment;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import static com.digiota.fotodirs.adapter.RecyclerViewHolders.SELECTED_PIC_DIRPATH;
import static com.digiota.fotodirs.adapter.RecyclerViewHolders.SELECTED_PIC_FILEPATH;

/**
 * Created by jdiamand on 10/9/17.
 */

public class FotoDisplayViewMvcImpl {

    //@BindView(R.id.foto_display_toolbar)
   // Toolbar mToolbar ;
   public  static final  String INFO_WINDOW_SHOW = "info_window_show" ;

    boolean mInfoShow;
    private Fragment mInfoFragment ;
    private Context mContext;
    static final String TAG = "FotoDisplayViewMvcImpl" ;
    Toolbar mToolbar;
    private View mRootView;
    private String mDirName = "" ;

    int mCurrentImageHeight  ;
    int mCurrentImageWidth ;
    int mCurrentRotate ;

    String mCurrentFotoDate = "" ;
    Float mLatitude ;
    Float mLongitude;

    File mImageFile ;
    int mRotateangle ;


    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    Matrix matrix  ;
    Matrix savedMatrix  ;
    PointF start  ;

    int mode  ;

    PointF mid = new PointF();
    float oldDist ;


    public int getAngle() {

        return mCurrentRotate ;
    }

    public String getFilename() {
        if (mImageFile != null) {
            return mImageFile.getAbsolutePath() ;
        }
        return null ;
    }
    public String getCurrentFotoDate() {

        String retVal = "Unavailable"    ;

        if ( (mCurrentFotoDate != null) && (!mCurrentFotoDate.equals("")) ){

            //String date="Mar 10, 2016 6:30:00 PM";
            String date=mCurrentFotoDate ;
            //SimpleDateFormat spf=new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
            SimpleDateFormat spf=new SimpleDateFormat("yyyy:MM:dd hh:mm");
            Date newDate ;
            try {
                 newDate = spf.parse(date);
            } catch (ParseException e){
                return "" ;
            }
            spf= new SimpleDateFormat("MMM dd yyyy  hh:mm aaa" );
            date = spf.format(newDate);
            return date ;
        }


        return retVal ;
    }

    public Float getCurrentLatitude() {
        return mLatitude ;
    }
    public Boolean getInfoFragmentAdd() {
        return mInfoShow;
    }

    public Float getCurrentLongitude() {
        return mLongitude ;
    }
    public Toolbar getToolbar() {
        return mToolbar;
    }

    public String getDirectoryName() {
        return mDirName;
    }

    public String getResolution() {

        return  Integer.toString(mCurrentImageHeight ) + " x " +
                    Integer.toString(mCurrentImageWidth) ;
    }

    public FotoDisplayViewMvcImpl(Context context, ViewGroup container,Bundle bundle) {
        mContext = context ;
        mImageFile = null ;
        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_foto_display, container);
        addInfoFragment() ;


        ((FotoDisplayActivity)context).setContentView(mRootView);

        mToolbar = (Toolbar) mRootView.findViewById(R.id.foto_display_toolbar);
        //ButterKnife.bind((FotoDisplayActivity)context) ;


        if (bundle == null) {
            mInfoShow = false ;
            setInfoWindowViz(View.GONE) ;
        } else {

            mInfoShow = bundle.getBoolean(INFO_WINDOW_SHOW,false ) ;

            if (mInfoShow) {
                setInfoWindowViz(View.VISIBLE) ;


            } else {
                setInfoWindowViz(View.GONE) ;


            }
        }


        Uri uri = null ;
        Bundle extras = ((FotoDisplayActivity)context).getIntent().getExtras();
        if(extras !=null)
        {
            if (((FotoDisplayActivity)context).getIntent().hasExtra(INFO_WINDOW_SHOW) ) {
                mInfoShow = extras.getBoolean(INFO_WINDOW_SHOW,false ) ;
                if (mInfoShow) {
                    setInfoWindowViz(View.VISIBLE) ;


                } else {
                    setInfoWindowViz(View.GONE) ;


                }

            }
            String fileName = extras.getString(SELECTED_PIC_FILEPATH);
            mDirName = extras.getString(SELECTED_PIC_DIRPATH, "");
            if (fileName != null) {

                mImageFile = new  File(fileName);

                if(mImageFile.exists()){

                    // EXIF

                    String currentLat  ;
                    String currentLatRef ;
                    String currentLong  ;
                    String currentLongRef ;

                    mRotateangle = 0;
                    try {
                        ExifInterface exif = new ExifInterface(fileName);
                        String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                        int orientation = orientstring != null ? Integer.parseInt(orientstring) : ExifInterface.ORIENTATION_NORMAL;
                        mRotateangle = 0;
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
                            mRotateangle = 90;
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
                            mRotateangle = 180;
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
                            mRotateangle = 270;

                        mCurrentFotoDate = exif.getAttribute(ExifInterface.TAG_DATETIME);
                        //
                        currentLat  = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        currentLatRef  = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                        currentLong= exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        currentLongRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);




                        if((currentLat !=null)
                                && (currentLatRef !=null)
                                && (currentLong != null)
                                && (currentLongRef !=null))
                        {

                            if(currentLatRef.equals("N")){
                                mLatitude = convertToDegree(currentLat);
                            }
                            else{
                                mLatitude = 0 - convertToDegree(currentLat);
                            }

                            if(currentLongRef.equals("E")){
                                mLongitude = convertToDegree(currentLong);
                            }
                            else{
                                mLongitude = 0 - convertToDegree(currentLong);
                            }

                        }






                    } catch (IOException e) {

                        Log.d("FotoDisplayViewMvcImpl", "err = " + e ) ;

                    }


                }


            }
        }



    }

   public  void drawImage() {

        Bitmap myBitmap = BitmapFactory.decodeFile(mImageFile.getAbsolutePath());

        mCurrentImageHeight = myBitmap.getHeight() ;
        mCurrentImageWidth = myBitmap.getWidth() ;
        ImageView myImage = (ImageView) mRootView.findViewById(R.id.selectedFoto);

        mCurrentRotate = mRotateangle ;
        Matrix mat = new Matrix();
        if (mRotateangle != 0 ) {

            mat.setRotate(mRotateangle, (float) myBitmap.getWidth() , (float) myBitmap.getHeight() );
            Bitmap myBitmap2 =
                    Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
            myImage.setImageBitmap(myBitmap2);

        } else {
            myImage.setImageBitmap(myBitmap);
        }


        matrix = null ;

        savedMatrix = null ;
        start = new PointF();
        mode = NONE ;

        oldDist = 1f ;
        setupTouchListener(myImage) ;


    }

    private void addInfoFragment() {

        FrameLayout fl =  (FrameLayout)mRootView.findViewById(R.id.overlay_fragment_container) ;
       FragmentTransaction ft = ((FotoDisplayActivity)mContext).getSupportFragmentManager().
               beginTransaction();

        mInfoFragment = new ImageInfoFragment() ;
       ft.add(R.id.overlay_fragment_container, mInfoFragment).
              setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit() ;

    }


    public boolean createMenu(Menu menu) {
        MenuInflater inflater = ((FotoDisplayActivity)mContext).getMenuInflater();
        inflater.inflate(R.menu.display_foto_menu, menu);
        MenuItem mapMenuItem = menu.findItem(R.id.action_map_info ) ;
        if ( (getCurrentLatitude() == null) || (getCurrentLongitude() == null)) {

            mapMenuItem.setVisible(false) ;
        } else {
            mapMenuItem.setVisible(true) ;
        }
        return true;


    }



    private Float convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;


    };







    void  setupTouchListener(ImageView imageView) {





        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ImageView view = (ImageView) v;
                final int action = event.getAction();

                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "ACTION_DOWN");
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        Log.d(TAG, "mode=DRAG");

                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:

                            oldDist = spacing(event);

                        Log.d(TAG, "ACTION_POINTER_DOWN");
                        Log.d(TAG, "oldDist=" + oldDist);
                        if (oldDist > 50f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                            Log.d(TAG, "mode=ZOOM");
                        }

                        break ;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        Log.d(TAG, "mode=NONE");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "ACTION_MOVE"  );
                        if (mode == DRAG) {
                            Log.d(TAG, "mode == DRAG"  );
                            // ...
                          matrix.set(savedMatrix);
                          matrix.postTranslate(event.getX() - start.x, event.getY()
                                 - start.y);
                        } else if (mode == ZOOM) {
                            Log.d(TAG, "mode == ZOOM"  );
                            float newDist = spacing(event);
                            Log.d(TAG, "newDist=" + newDist);
                            if (newDist > 10f) {
                               matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                               matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }

                        break;


                    case MotionEvent.ACTION_CANCEL:
                        break ;

                }

                    Log.d(TAG,matrix.toString()) ;
                    view.setImageMatrix(matrix);


                return true ;
            }
        });

    }

    public void initMatrices(Matrix seed) {

        matrix = new Matrix(seed);
        savedMatrix = new Matrix(seed);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public boolean  processItemSelected(int id) {

        switch (id) {

            case android.R.id.home:
                ((Activity)mContext).onBackPressed();
                return true ;

            case R.id.action_show_info:


                mInfoShow = !mInfoShow;
                Context appContext = mContext.getApplicationContext() ;
                Intent intent = new Intent(appContext, FotoDisplayActivity.class);
                Bundle bundle = new Bundle();// New activity
                bundle.putString(SELECTED_PIC_FILEPATH, mImageFile.toString());
                bundle.putString(SELECTED_PIC_DIRPATH, mDirName);
                bundle.putBoolean(INFO_WINDOW_SHOW,mInfoShow) ;
                intent.putExtras(bundle);
                ((Activity)mContext).finish();
                appContext.startActivity(intent);



                return true ;


            case R.id.action_map_info :

                String url =
                        "https://www.google.com/maps/search/?api=1&query="
                        + getCurrentLatitude() + ","
                        + getCurrentLongitude() ;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                ((Activity)mContext).startActivity(i);


                return true ;

        }
        return false ;
    }

    private void setInfoWindowViz(int viz) {
        FrameLayout fl =  (FrameLayout)mRootView.findViewById(R.id.overlay_fragment_container) ;
        fl.setVisibility(viz);
    }


    public String getAppName() {
        return mContext.getString(R.string.app_name) ;

   }

   public void scaleImage() {
       ImageView imageView = (ImageView) mRootView.findViewById(R.id.selectedFoto);

       Matrix m = imageView.getImageMatrix();
       RectF drawableRect ;
       if ( (mCurrentRotate == 90 )|| (mCurrentRotate == 270) ) {
           drawableRect = new RectF(0, 0,  mCurrentImageHeight,mCurrentImageWidth);
       } else {
           drawableRect = new RectF(0, 0, mCurrentImageWidth, mCurrentImageHeight);
       }
       RectF viewRect = new RectF(0, 0,   imageView.getWidth(), imageView.getHeight());
       m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
       imageView.setImageMatrix(m);
       initMatrices(imageView.getImageMatrix()) ;
   }


}
