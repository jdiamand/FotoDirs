package com.digiota.fotodirs.controller;

/**
 * Created by jdiamand on 10/11/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.digiota.fotodirs.R;

public class ImageInfoFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.image_info_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        TextView etDate = (TextView) view.findViewById(R.id.textViewDate);
        etDate.setText("Date : " + ((FotoDisplayActivity)getActivity()).getCurrentFotoDate()) ;
        TextView etLongitude = (TextView) view.findViewById(R.id.textViewLongitude);
        etLongitude.setText("Longitude : " + ((FotoDisplayActivity)getActivity()).getCurrentLongitude()) ;
        TextView etLatitude = (TextView) view.findViewById(R.id.textViewLatitude);
        etLatitude.setText("Latitude : " + ((FotoDisplayActivity)getActivity()).getCurrentLatitude()) ;
        TextView etFilename = (TextView) view.findViewById(R.id.textViewFilename);
        etFilename.setText("Filename : " + ((FotoDisplayActivity)getActivity()).getFilename()) ;
        TextView etPath = (TextView) view.findViewById(R.id.textViewPathname);
        etPath.setText("Pathname : " + ((FotoDisplayActivity)getActivity()).getPathName()) ;
        TextView etRotation = (TextView) view.findViewById(R.id.textViewRotation);
        etRotation.setText("Orientation : " + ((FotoDisplayActivity)getActivity()).getAngle() + " degrees") ;
        TextView etResolution = (TextView) view.findViewById(R.id.textViewResolution );
        etResolution.setText("Resolution : " + ((FotoDisplayActivity)getActivity()).getResolution() ) ;
    }


}