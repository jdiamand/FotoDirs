package com.digiota.fotodirs.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.digiota.fotodirs.FotoDirsApplication;
import com.digiota.fotodirs.R;
import com.digiota.fotodirs.adapter.NavigationDrawerAdapter;
import com.digiota.fotodirs.model.FotoDirInfo;
import com.digiota.fotodirs.model.LocalMediaDirectory;
import com.digiota.fotodirs.model.MessageEvent;
import com.digiota.fotodirs.model.NavDrawerItem;
//import FotoGridFragmentViewMvcImpl;
import com.digiota.fotodirs.view.FragmentDrawerViewMvcImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by jdiamand on 1/15/17.
 */

public class FragmentDrawer extends Fragment   {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    FragmentDrawerViewMvcImpl mViewMVC;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter mAdapterInternal;
    private NavigationDrawerAdapter mAdapterExternal;
    private View containerView;
    private static String[] phoneFotoFolders;
    private Map<String, FotoDirInfo> phoneFotoFoldersMap;
    private static String[] cardFotoFolders;
    private Map<String, FotoDirInfo> cardFotoFoldersMap;
    private static String[] cloudFotoFolders;
    private Map<String, FotoDirInfo> cloudFotoFoldersMap;
    private FragmentDrawerListener drawerListener;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewMVC = new FragmentDrawerViewMvcImpl(this.getContext(), container);

        final  RecyclerView recyclerViewInternal = mViewMVC.getRecyclerViewInternal();
        final  RecyclerView recyclerViewExternal = mViewMVC.getRecyclerViewExternal() ;








        mAdapterInternal = new NavigationDrawerAdapter(getActivity(), getDataInternal());


        recyclerViewInternal.setAdapter(mAdapterInternal);
        recyclerViewInternal.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewInternal.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewInternal, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                drawerListener.onDrawerItemSelected(view, position );
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mAdapterExternal = new NavigationDrawerAdapter(getActivity(), getDataExternal());
        recyclerViewExternal.setAdapter(mAdapterExternal);
        recyclerViewExternal.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewExternal.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewExternal, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int internalCount = recyclerViewInternal.getAdapter().getItemCount() ;
                drawerListener.onDrawerItemSelected(view, position + internalCount);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        if (!updateMenu()) {
            EventBus.getDefault().register(this);
        }

        return mViewMVC.getRootView() ;
    }

    @Override
    public void onStart() {

        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FotoDirsApplication.setLocalMediaDirectory(null);
    }

    @Override
    public void onStop() {

        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    public  List<NavDrawerItem> getDataInternal() {
        List<NavDrawerItem> data = new ArrayList<>();

        if (phoneFotoFolders != null) {
            // preparing navigation drawer items
            for (int i = 0; i < phoneFotoFolders.length; i++) {
                NavDrawerItem navItem = new NavDrawerItem();
                navItem.setTitle(phoneFotoFolders[i]);
                navItem.setIcon(mViewMVC.mapFolderToIcon(phoneFotoFolders[i]) ) ;

                data.add(navItem);
            }
        }
        return data;
    }

    public  List<NavDrawerItem> getDataExternal() {
        List<NavDrawerItem> data = new ArrayList<>();

        if (cardFotoFolders != null) {
            // preparing navigation drawer items
            for (int i = 0; i < cardFotoFolders.length; i++) {
                NavDrawerItem navItem = new NavDrawerItem();
                navItem.setTitle(cardFotoFolders[i]);
                navItem.setIcon(mViewMVC.mapFolderToIcon(cardFotoFolders[i]) ) ;
                data.add(navItem);
            }
        }
        return data;
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    boolean updateMenu() {
        LocalMediaDirectory localMediaDirectory = FotoDirsApplication.getLocalMediaDirectory();
        if (localMediaDirectory == null) {
            return false;
        }
        // on-phone photos
        phoneFotoFoldersMap = localMediaDirectory.getPictureDirectoriesInt();
        phoneFotoFolders = new String[phoneFotoFoldersMap.size()];

        Iterator itPhone = phoneFotoFoldersMap.entrySet().iterator();
        int indxPhone = 0;
        while (itPhone.hasNext()) {
            Map.Entry pair = (Map.Entry) itPhone.next();
            FotoDirInfo fotoDirInfo = (FotoDirInfo) pair.getValue();
            phoneFotoFolders[indxPhone++] = fotoDirInfo.dirName + " (" + fotoDirInfo.fotoCount + ")";
            //itPhone.remove(); // avoids a ConcurrentModificationException
        }
        RecyclerView recyclerViewInternal = mViewMVC.getRecyclerViewInternal();
        RecyclerView recyclerViewExternal = mViewMVC.getRecyclerViewExternal() ;

        mAdapterInternal = new NavigationDrawerAdapter(getActivity(), getDataInternal());
        recyclerViewInternal.setAdapter(mAdapterInternal);

        // on-card photos
        cardFotoFoldersMap = localMediaDirectory.getPictureDirectoriesExt();
        cardFotoFolders = new String[cardFotoFoldersMap.size()];

        Iterator itCard = cardFotoFoldersMap.entrySet().iterator();
        int indxCard = 0;
        while (itCard.hasNext()) {
            Map.Entry pair = (Map.Entry) itCard.next();
            FotoDirInfo fotoDirInfo = (FotoDirInfo) pair.getValue();
            cardFotoFolders[indxCard++] = fotoDirInfo.dirName  + " (" + fotoDirInfo.fotoCount + ")";
            //itCard.remove(); // avoids a ConcurrentModificationException
        }

        mAdapterExternal = new NavigationDrawerAdapter(getActivity(), getDataExternal());
        recyclerViewExternal.setAdapter(mAdapterExternal);

        return true;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                //int pos = rv.getChildPosition(child) ;
                //sendMessage()
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            RecyclerView myrv  = rv ;
            MotionEvent  myE   =   e ;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        updateMenu();

    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }


}


