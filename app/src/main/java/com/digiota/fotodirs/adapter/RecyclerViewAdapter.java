package com.digiota.fotodirs.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.List;

/**
 * Created by jdiamand on 1/18/17.
 */

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewHolders> {


    private List<ItemObject> itemList;
    private Context mContext;
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
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        mParent = parent ;
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {


        ItemObject item  = itemList.get(position);

        if(item.getPhotoFile() !=null){
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


            if ( (width == 0)  || (height == 0 ) ) {
                return ;
            }

           // Log.d ("ExifInterfaceFF" , "file = " + item.getPhotoFile().toString() + ", width = "  + width + ", height = " + height ) ;

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(300,300))
                    .build();
            holder.photo.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController( holder.photo.getController())
                            .setImageRequest(request)
                            .build());
        }





       // holder.photo.setImageResource(itemList.get(position).getPhotoResource());
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
