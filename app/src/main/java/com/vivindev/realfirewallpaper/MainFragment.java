package com.vivindev.realfirewallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vivindev.realfirewallpaper.config.admob;

import com.vivindev.realfirewallpaper.adapter.WallpaperAdapter;

/**
 * Created by kingdov on 17/01/2017.
 */

public class MainFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static WallpaperAdapter recyclerAdapter;
    public static androidx.appcompat.widget.AppCompatTextView noWallpaper;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = v.findViewById(R.id.wallpaperList);
        noWallpaper = (androidx.appcompat.widget.AppCompatTextView) v.findViewById(R.id.no_wallpaper) ;
        LinearLayout linearlayout = (LinearLayout)v.findViewById(R.id.unitads);
        admob.admobBannerCall(getActivity(), linearlayout);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        setRecyclerView(getActivity());
        return v;
    }

    public static void setRecyclerView(Context activity) {
        SplashActivity splashScreen = new SplashActivity();
        if(splashScreen.data.size()>0) noWallpaper.setVisibility(View.GONE);
        else noWallpaper.setVisibility(View.VISIBLE);
        recyclerAdapter = new WallpaperAdapter(activity, splashScreen.data);
        recyclerView.setAdapter(recyclerAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }

}
