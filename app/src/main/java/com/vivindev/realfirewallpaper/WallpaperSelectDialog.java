package com.vivindev.realfirewallpaper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by kingdov on 17/01/2017.
 */

@SuppressLint("ValidFragment")
public class WallpaperSelectDialog extends DialogFragment {

    Context context;
    Bitmap resource;
    View view;


    public WallpaperSelectDialog(Context context, Bitmap resource, View view) {
        this.context = context;
        this.resource = resource;
        this.view = view;
    }


    private void setOurWall(int which, int sbMessage) {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                WallpaperManager.getInstance(context)
                        .setBitmap(resource, null, true, which);
                Toast.makeText(context,sbMessage,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            setOurWall(sbMessage);
        }


    }

    private void setOurWall(int sbMessage) {

        try {
            WallpaperManager.getInstance(context)
                    .setBitmap(resource);
            Snackbar.make(view, sbMessage, Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder setWall;
        if (Build.VERSION.SDK_INT >= 24) {
            setWall = new AlertDialog.Builder(context);
            setWall.setTitle(R.string.set_wallpaper)
                    .setItems(R.array.set_wallpaper_options_24plus, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            switch (i) {
                                case 0: {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            setOurWall(WallpaperManager.FLAG_SYSTEM, R.string.home_set);
                                        }
                                    });
                                    break;
                                }
                                case 1: {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            setOurWall(WallpaperManager.FLAG_LOCK, R.string.lock_screen_set);
                                        }
                                    });
                                    break;
                                }
                                case 2: {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            setOurWall(R.string.both_set);
                                        }
                                    });
                                    break;
                                }
                            }

                        }
                    });
        }else{
            setWall = new AlertDialog.Builder(context);
            setWall.setTitle(R.string.set_wallpaper)
                    .setItems(R.array.set_wallpaper_options_24minus, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            switch (i) {
                                case 0: {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            setOurWall(R.string.both_set);
                                        }
                                    });
                                    break;
                                }
                            }

                        }
                    });
        }

        return setWall.create();



    }
}
