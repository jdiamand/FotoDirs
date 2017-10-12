package com.digiota.fotodirs.controller;

/**
 * Created by jdiamand on 1/16/17.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.digiota.fotodirs.FotoDirsApplication;
import com.digiota.fotodirs.adapter.ItemObject;
import com.digiota.fotodirs.adapter.RecyclerViewAdapter;
import com.digiota.fotodirs.model.LocalMediaDirectory;
import com.digiota.fotodirs.view.FotoGridFragmentViewMvcImpl;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.digiota.fotodirs.view.MainViewMvcImpl.FOLDER_INDEX;


public class FotoGridFragment  extends Fragment implements AdapterView.OnItemClickListener {

    FotoGridFragmentViewMvcImpl mViewMVC;
    private LayoutInflater mLayoutInflater ;
    private LinearLayoutManager lLayout;
    private List<ItemObject> mRowListItem ;

    private String mCurrrentFolderName ;
    private String mCurrentFullFolderName;

    Activity mActivity = null;
    int mCurrentFolder ;

    public FotoGridFragment() {
        // Required empty public constructor
        mCurrentFolder = 0 ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrrentFolderName = null ;
        mCurrentFullFolderName = null ;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle=getArguments();

        //here is your list array
        mCurrentFolder=bundle.getInt(FOLDER_INDEX);

        LocalMediaDirectory localMediaDirectory = FotoDirsApplication.getLocalMediaDirectory();
        if (localMediaDirectory != null ) {
            mCurrrentFolderName = localMediaDirectory.getFolderNameAtIndex(mCurrentFolder);
            mCurrentFullFolderName = localMediaDirectory.getFullFolderNameAtIndex(mCurrentFolder);
        }





        mViewMVC = new FotoGridFragmentViewMvcImpl(this.getContext(), null);


        mLayoutInflater = inflater ;

        if (mRowListItem == null) {
            mRowListItem = getAllItemList();
        }


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mActivity, mRowListItem);


        mViewMVC.setLayoutAdapter(adapter) ;


        return mViewMVC.getRootView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



        if (context instanceof Activity){
            mActivity=(Activity) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
       //Toast.makeText(getActivity(), "Item: " + (position +1) , Toast.LENGTH_SHORT).show();
    }

    private List<ItemObject> getAllItemList(){
        List<ItemObject> allItems = new ArrayList<ItemObject>();
        if (mCurrrentFolderName != null) {


            File dir = new File(mCurrentFullFolderName);
            File[] fileList = dir.listFiles();


            for (int i = 0; i < fileList.length; i++) {
                String[] parts = fileList[i].toString().split("\\.") ;
                if (parts.length > 1) {
                    if (parts[parts.length -1 ].compareToIgnoreCase("jpg") == 0 ) {
                        allItems.add(new ItemObject( fileList[i]));
                    }
                }

            }
        }

        return allItems;
    }

    /*
    private List<ItemObject> getAllBitmapItemList(){
        List<ItemObject> allItems = new ArrayList<ItemObject>();
        if (mCurrrentFolderName != null) {
            File dir = new File(mCurrentFullFolderName);
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(fileList[i].getName(),bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap,50,50,true);
                //imageView.setImageBitmap(bitmap);
                allItems.add( (ItemObject)bitmap);
            }
        }

        return allItems;
    }
    */
}
