package com.digiota.fotodirs.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.R;
import com.digiota.fotodirs.interfaces.FragmentDrawerViewMvc;

/**
 * Created by jdiamand on 1/31/17.
 */

public class FragmentDrawerViewMvcImpl implements FragmentDrawerViewMvc {



    private View mRootView;
    private RecyclerView mRecyclerViewInternal;
    private RecyclerView mRecyclerViewExternal;

    public RecyclerView getRecyclerViewInternal() {
        return mRecyclerViewInternal;
    }

    public RecyclerView getRecyclerViewExternal() {
        return mRecyclerViewExternal;
    }



    public FragmentDrawerViewMvcImpl(Context context, ViewGroup container) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.fragment_navigation_drawer, container);
        initialize();
    }


    void initialize() {
        mRecyclerViewInternal = (RecyclerView) mRootView.findViewById(R.id.drawerListInternal);
        mRecyclerViewExternal = (RecyclerView) mRootView.findViewById(R.id.drawerListExternal);

    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    public int mapFolderToIcon(String folderName) {
        int retVal = R.drawable.ic_collections_black_24dp ;

        while(true) {
            if (folderName.startsWith("Camera")) {
                retVal = R.drawable.ic_photo_camera_black_24dp;
                break ;
            }

            if (folderName.startsWith("Instagram")) {
                retVal = R.drawable.ic_instagram_logo_black;
                break ;
            }

            if (folderName.startsWith("Facebook")) {
                retVal = R.drawable.ic_facebook_logo_black;
                break ;
            }

            if (folderName.startsWith("Photo editor")) {
                retVal = R.drawable.ic_format_paint_black_24dp;
                break ;
            }

            if (folderName.startsWith("Photoshop")) {
                retVal = R.drawable.ic_photoshop_logo_black;
                break ;
            }

            if (folderName.startsWith("Message")) {
                retVal = R.drawable.ic_message_black_24dp;
                break ;
            }

            if (folderName.startsWith("Messenger")) {
                retVal = R.drawable.ic_messenger_logo_black;
                break ;
            }
            if (folderName.startsWith("Screenshots")) {
                retVal = R.drawable.ic_add_to_queue_black_24dp;
                break ;
            }

            if (folderName.startsWith("Download")) {
                retVal = R.drawable.ic_file_download_black_24dp;
                break ;
            }

            if (folderName.startsWith("digiFOTO")) {
                retVal = R.drawable.ic_digifoto_logo_black;
                break ;
            }

            if (folderName.startsWith("Snapchat")) {
                retVal = R.drawable.ic_snapchat_logo_black;
                break ;
            }





            break ;

        }
        return retVal ;

    }
}







