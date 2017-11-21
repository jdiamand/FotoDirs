package com.digiota.fotodirs.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.controller.MainActivity;
import com.digiota.fotodirs.view.MainViewMvcImpl;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.io.IOException;
import java.util.List;

import static com.digiota.fotodirs.view.MainViewMvcImpl.PREFS_PACKAGE;

/**
 * Created by jdiamand on 1/18/17.
 */

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewHolders> {


    private List<ItemObject> itemList;
    private Context mContext;
    public  Context getContext () { return mContext;}  ;
    ViewGroup mParent ;

    public RecyclerViewAdapter(Context mContext, List<ItemObject> itemList) {
        this.itemList = itemList;
        this.mContext = mContext;

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolders holder) {
        super.onViewAttachedToWindow(holder);



        RecyclerView recyclerView = (RecyclerView)mParent ;
        GridLayoutManager layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
        int pos =  layoutManager.findFirstCompletelyVisibleItemPosition()  ;

        SharedPreferences prefs = mContext.getSharedPreferences(
                PREFS_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putInt(MainViewMvcImpl.PICTURE_INDEX,pos).apply();


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null);


        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView, this);
        mParent = parent ;
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {


        ItemObject item  = itemList.get(position);

        if(item.getPhotoFile() !=null){
            int dotIndx = item.getPhotoFile().toString().lastIndexOf(".") ;
            String photoType = item.getPhotoFile().toString().substring(++dotIndx) ;
            Uri uri = Uri.fromFile( item.getPhotoFile()) ;
            ExifInterface exif = null ;
            try {
               exif = new ExifInterface(item.getPhotoFile().toString());
            } catch(IOException e) {
                return ;
            }

            if (exif == null) {
                return ;
            }


            int   width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH , 0) ;
            int   height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH , 0) ;


            if ( (photoType.equals("jpg") && ((width == 0)  || (height == 0 ) ) ) ){
                return ;
            }
            ImageRequest request = null ;

            if (  (photoType.equals("png"))
                 ||  photoType.equals("gif") )
                {

                      request = ImageRequestBuilder.newBuilderWithSource(uri)

                            .build();
                } else {

                      request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(300, 300))
                        .build();
            }
            holder.photo.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController( holder.photo.getController())
                            .setImageRequest(request)
                            .build());
        }






    }
    public Object getObectAtIndex(int index ) {

        return  itemList.get(index) ;
    }

    /*
    public Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=300;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap =  BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

            //return  Bitmap.createScaledBitmap(bitmap,300,300,true);
            return bitmap ;
        } catch (FileNotFoundException e) {}
        return null;
    }
    */
}
