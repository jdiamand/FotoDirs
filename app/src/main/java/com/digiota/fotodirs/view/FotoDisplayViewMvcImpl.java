package com.digiota.fotodirs.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.digiota.fotodirs.R;
import com.digiota.fotodirs.controller.FotoDisplayActivity;


import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;


import static com.digiota.fotodirs.adapter.RecyclerViewHolders.SELECTED_PIC_DIRPATH;
import static com.digiota.fotodirs.adapter.RecyclerViewHolders.SELECTED_PIC_FILEPATH;

/**
 * Created by jdiamand on 10/9/17.
 */

public class FotoDisplayViewMvcImpl {
    private Context mContext;
    static final String TAG = "FotoDisplayViewMvcImpl" ;
    Toolbar mToolbar;
    private View mRootView;
    private String mDirName = "" ;

    int mCurrentImageHeight  ;
    int mCurrentImageWidth ;
    int mCurrentRotate ;

    String mCurrentFotoDate ;
    Float mLatitude ;
    Float mLongitude;

    public String getCurrentFotoDate() {
        return mCurrentFotoDate ;
    }

    public Float getCurrentLatitude() {
        return mLatitude ;
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

    public FotoDisplayViewMvcImpl(Context context, ViewGroup container,Bundle bundle) {
        mContext = context ;
        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_foto_display, container);
        ButterKnife.bind((Activity)context) ;

        ((Activity)context).setContentView(mRootView);

        mToolbar = (Toolbar) mRootView.findViewById(R.id.foto_display_toolbar);


        Uri uri = null ;
        Bundle extras = ((FotoDisplayActivity)context).getIntent().getExtras();
        if(extras !=null)
        {
            String fileName = extras.getString(SELECTED_PIC_FILEPATH);
            mDirName = extras.getString(SELECTED_PIC_DIRPATH, "");
            if (fileName != null) {

                File imgFile = new  File(fileName);

                if(imgFile.exists()){

                    // EXIF

                    String currentLat  ;
                    String currentLatRef ;
                    String currentLong  ;
                    String currentLongRef ;

                    int rotateangle = 0;
                    try {
                        ExifInterface exif = new ExifInterface(fileName);
                        String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                        int orientation = orientstring != null ? Integer.parseInt(orientstring) : ExifInterface.ORIENTATION_NORMAL;
                        rotateangle = 0;
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
                            rotateangle = 90;
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
                            rotateangle = 180;
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
                            rotateangle = 270;

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



                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    mCurrentImageHeight = myBitmap.getHeight() ;
                    mCurrentImageWidth = myBitmap.getWidth() ;
                    ImageView myImage = (ImageView) mRootView.findViewById(R.id.selectedFoto);

                    mCurrentRotate = rotateangle ;
                    Matrix mat = new Matrix();
                    if (rotateangle != 0 ) {

                        mat.setRotate(rotateangle, (float) myBitmap.getWidth() , (float) myBitmap.getHeight() );
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


            }
        }


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





    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    Matrix matrix  ;
    Matrix savedMatrix  ;
    PointF start  ;

    int mode  ;

    PointF mid = new PointF();
    float oldDist ;


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
        }
        return false ;
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
