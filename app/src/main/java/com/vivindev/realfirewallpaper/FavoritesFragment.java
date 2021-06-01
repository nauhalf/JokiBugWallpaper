package com.vivindev.realfirewallpaper;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vivindev.realfirewallpaper.adapter.FavoritesAdapter;
import com.vivindev.realfirewallpaper.config.admob;

/**
 * Created by kingdov on 17/01/2017.
 */

public class FavoritesFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static FavoritesAdapter favoritesAdapter;
    public static androidx.appcompat.widget.AppCompatTextView noFavorite;
    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_favorites, container, false);
        LinearLayout linearlayout = (LinearLayout)v.findViewById(R.id.unitads);
        admob.admobBannerCall(getActivity(), linearlayout);
        recyclerView = v.findViewById(R.id.wallpaperList);
        noFavorite = v.findViewById(R.id.no_favorite);
        getAllFavoritesDataUrl();
        setRecyclerView(getActivity());
        return v;
    }

    public void getAllFavoritesDataUrl(){
        MainActivity.favoriteData.clear();
        SplashActivity splashScreen = new SplashActivity();
        for(int i = 0;i<splashScreen.data.size();i++ ){
            if(MainActivity.listFavorites.contains(splashScreen.data.get(i).wallURL))
                MainActivity.favoriteData.add(splashScreen.data.get(i));
        }
    }
    public static void setRecyclerView(Context context) {
        if (MainActivity.favoriteData.size() > 0)
            noFavorite.setVisibility(View.GONE);
        else
            noFavorite.setVisibility(View.VISIBLE);
        favoritesAdapter = new FavoritesAdapter(context, MainActivity.favoriteData);
        recyclerView.setAdapter(favoritesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
    }

}
