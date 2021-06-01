package com.vivindev.realfirewallpaper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vivindev.realfirewallpaper.FavoritesFragment;
import com.vivindev.realfirewallpaper.MainActivity;
import com.vivindev.realfirewallpaper.MainFragment;
import com.vivindev.realfirewallpaper.WallpaperDetail;
import com.vivindev.realfirewallpaper.config.admob;
import com.vivindev.realfirewallpaper.func.DataUrl;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vivindev.realfirewallpaper.GlideApp;

import java.util.Collections;
import java.util.List;

import com.vivindev.realfirewallpaper.R;

/**
 * Created by kingdov on 17/01/2017.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private LayoutInflater inflater;
    List<DataUrl> data = Collections.emptyList();
    DataUrl current;

    public FavoritesAdapter(Context context, List<DataUrl> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view_wallpaper, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.textView.setText(current.wallName);


        GlideApp.with(context).load(current.wallURL)
                .error(R.drawable.ic_placeholder_wallpaper)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder_wallpaper)
                .into(myHolder.imageView);
        myHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WallpaperDetail.sData = data.get(position);
                Intent intent = new Intent(context, WallpaperDetail.class);
                context.startActivity(intent);
                admob.showInterstitial(true);
            }
        });
        try {
            myHolder.imageViewFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delete));
        }catch (Exception ex){

        }

        myHolder.imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.text_dialog, null);
                final TextView mssg = alertLayout.findViewById(R.id.messageDelete);
                mssg.setText("Do you want to remove it from favorites wallpapers");
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Remove from favorites wallpapers");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.listFavorites.remove(data.get(position).wallURL);
                        MainActivity.favoriteData.remove(data.get(position));
                        MainFragment.setRecyclerView(context);
                        FavoritesFragment.setRecyclerView(context);
                        String json = MainActivity.gson.toJson(MainActivity.listFavorites);
                        MainActivity.editor.putString("favorites", json);
                        MainActivity.editor.commit();
                        Toast.makeText(context, "Operation done Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

                admob.showInterstitial(true);

            }
        });

        myHolder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String shareText = context.getString(R.string.share_text) + " "
                        + context.getString(R.string.app_name) + " app by "
                        + context.getString(R.string.dev) + ": \n\n"
                        + data.get(position).wallName + "\n"
                        + data.get(position).wallURL;

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                context.startActivity(Intent.createChooser(sendIntent, "Share wallpaper via:"));


            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageViewFavorite, imageShare;
        TextView textView;
        boolean favorite = false;
        public MyHolder(View itemView) {
            super(itemView);
            imageShare = itemView.findViewById(R.id.wallpaperShare);
            imageView = itemView.findViewById(R.id.wallpaperThumb);
            imageViewFavorite = itemView.findViewById(R.id.wallpaperFavorite);
            textView = itemView.findViewById(R.id.wallpaperTitle);
        }
    }
}
