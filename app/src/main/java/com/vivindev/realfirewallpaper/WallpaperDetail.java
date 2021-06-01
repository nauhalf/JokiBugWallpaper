package com.vivindev.realfirewallpaper;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vivindev.realfirewallpaper.func.DataUrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Random;

import com.vivindev.realfirewallpaper.config.admob;
import com.vivindev.realfirewallpaper.func.Button;
import com.vivindev.realfirewallpaper.func.CustomTextView;

import static com.vivindev.realfirewallpaper.config.admob.initialInterstitial;
import static com.vivindev.realfirewallpaper.config.admob.showInterstitial;

/**
 * Created by kingdov on 17/01/2017.
 */

public class WallpaperDetail extends AppCompatActivity {

    public static DataUrl sData;
    ImageView arrow_back, wallstuffimage;
    CustomTextView copyright, wallstufftitle, wallstuffsite, wallstufflink,
            wallstufflicense, wallstuffsize, wallstufftype;
    Button setwallpaper, download, preview;
    String directory, extension, fileplace, fulldescPerm;
    View apnaview;
    final int writeStorageRequest = 0;
    FloatingActionButton wallstuffshare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);
        initialInterstitial(this);
        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.unitads);
        admob.admobBannerCall(this, linearlayout);
        findStuff();
        setCopyrightText();
        if (sData == null) {
            onBackPressed();
        } else {
            setData();
            downloadWall();
            setArrowBack();
            setPreview();
            setWallButton();
            setShareButton();
        }

    }

    private void findStuff() {
        copyright = findViewById(R.id.copyright);
        wallstufftitle = findViewById(R.id.wallstufftitle);
        wallstuffsize = findViewById(R.id.wallpaperSize);
        wallstufftype = findViewById(R.id.wallpaperType);
        wallstuffsite = findViewById(R.id.wallstuffsite);
        wallstufflink = findViewById(R.id.wallstufflink);
        wallstuffimage = findViewById(R.id.wallstuffimage);
        arrow_back = findViewById(R.id.arrow_back);
        wallstufflicense = findViewById(R.id.wallstufflicense);
        download = findViewById(R.id.download);
        preview = findViewById(R.id.preview);
        setwallpaper = findViewById(R.id.setwallpaper);
        wallstuffshare = findViewById(R.id.wallstuffshare);
    }

    private void setData() {
        wallstufftitle.setText(sData.wallName);
        wallstuffsite.setText(sData.wallSite);
        wallstufflink.setText(sData.wallSiteUrl);
        wallstuffsize.setText("Type");
        switch (sData.wallURL.toLowerCase().substring(sData.wallURL.length() - 3)) {
            case "jpg":
                wallstufftype.setText("Format jpg");
                extension = ".jpg";
                break;
            case "peg":
                wallstufftype.setText("Format jpeg");
                extension = ".jpeg";
                break;
            case "png":
                wallstufftype.setText("Format png");
                extension = ".png";
                break;
            case "gif":
                wallstufftype.setText("Format gif");
                extension = ".gif";
                break;
            default:
                wallstufftype.setText("Unknown");
                extension = ".jpg";
                break;
        }

        if (sData.wallSite.matches("Pinimg") || sData.wallSite.matches("Imgur")) {
            wallstufflicense.setText(R.string.public_domain);
        } else {
            wallstufflicense.setText(R.string.creative_commons);
        }


        GlideApp.with(this)
                .load(sData.wallURL)
                .error(R.drawable.ic_placeholder_wallpaper_head)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder_wallpaper_head)
                .into(wallstuffimage);

    }

    private void setCopyrightText() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.licence_text));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperDetail.this, DiscActivity.class);
                startActivity(intent);
                showInterstitial(true);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        ss.setSpan(clickableSpan, 335, 339, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        copyright.setText(ss);
        copyright.setMovementMethod(LinkMovementMethod.getInstance());
        copyright.setHighlightColor(Color.TRANSPARENT);
    }

    private void setDownload(String uRl) {

//        directory = "/Pictures/" + getResources().getString(R.string.app_name) + "/";
//        fileplace = getExternalFilesDir(null) + directory + sData.wallName + sData.wallIndex + extension;

//
//        File direct = new File(getExternalFilesDir(null)
//                + directory);
//        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
//        if (!direct.exists()) {
//            direct.mkdir();
//        }

        GlideApp.with(this)
                .asBitmap()
                .load(sData.wallURL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, Transition<? super Bitmap> transition) {


//                        new AsyncTask<Void, Void, Long>() {
//
//                            @Override
//                            protected Long doInBackground(Void... voids) {
//                                File file = new File(fileplace);
//                                long contentlength = resource.getByteCount();
//
//                                try {
//                                    OutputStream os = new FileOutputStream(file);
//                                    resource.compress(Bitmap.CompressFormat.PNG, 100, os);
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                                return contentlength;
//                            }
//
//                            @Override
//                            protected void onPostExecute(Long contentlength) {
//                                super.onPostExecute(contentlength);
//
//                                // Add downloaded wallie to completed downloads, show notif and all
//                                DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
//                                manager.addCompletedDownload(
//                                        sData.wallName + sData.wallIndex,
//                                        "By" + sData.wallSite,
//                                        true,
//                                        "image/png",
//                                        fileplace,
//                                        contentlength,
//                                        true
//                                );
//
//                                // Show snackbar too
//                                Snackbar.make(apnaview, "Download Finished", Snackbar.LENGTH_LONG)
//
//                                        .setAction("Open", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                if(Build.VERSION.SDK_INT>=24){
//                                                    try{
//                                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
//                                                        m.invoke(null);
//                                                    }catch(Exception e){
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                                Intent intent = new Intent();
//                                                intent.setAction(Intent.ACTION_VIEW);
//                                                intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory() + directory + "/" + sData.wallName + extension), "image/*");
//                                                startActivity(intent);
//                                            }
//                                        })
//                                        .show();
//                            }
//                        }.execute();
                        try {

                            new DownloadImage(apnaview, getResources().getString(R.string.app_name), sData.wallName + sData.wallIndex + extension, resource).execute();
                        }
                        catch (Exception e){
                            Toast.makeText(WallpaperDetail.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void downloadWall() {
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apnaview = view;
                if (isPermissionGranted()) {
                    setDownload(sData.wallURL);
                }
                showInterstitial(true);
            }
        });
    }

    private static class DownloadImage extends AsyncTask<Void, Void, Long> {

        String appname, filename;
        private final WeakReference<View> appNavView;
        private Bitmap resource;
        private String path;
        private File filePhoto;


        DownloadImage(View view, String appname, String filename, Bitmap resource) {
            this.appNavView = new WeakReference<>(view);
            this.resource = resource;
            this.appname = appname;
            this.filename = filename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(Void... voids) {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "/Pictures/" + appname);
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, (filename));

            long contentlength = 0;
            try {
                contentlength = resource.getByteCount();
                FileOutputStream os = new FileOutputStream(file);
                resource.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
                path = file.getPath();
                filePhoto = file;
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return contentlength;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            // Add downloaded wallie to completed downloads, show notif and all
            final Context context = appNavView.get().getContext().getApplicationContext();


            Uri uri = Uri.parse("file://" + path);
            Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", filePhoto);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(photoURI, "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Show snackbar too
            createNotification(intent);
            Snackbar.make(appNavView.get(), "Download Finished", Snackbar.LENGTH_LONG)

                    .setAction("Open", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= 24) {
                                try {
                                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                    m.invoke(null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            appNavView.get().getContext().startActivity(intent);
                        }
                    })
                    .show();
        }

        void createNotification(Intent intent){
            Context context = appNavView.get().getContext().getApplicationContext();
            Random generator = new Random();
            PendingIntent pendingIntent =  PendingIntent.getActivity(context, generator.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            String channelId = "Download_Channel";

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, "Download_Channel")
                            .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                            .setContentTitle("Wallpaper " + sData.wallName + sData.wallIndex + " successfully downloaded")
                            .setContentText("By" + sData.wallSite)
                            .setContentIntent(pendingIntent); //Required on Gingerbread and below
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Download Wallpaper",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    private boolean isPermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                // Explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    fulldescPerm = getResources().getString(R.string.storage_explanation_1)
                            + " "
                            + getResources().getString(R.string.app_name)
                            + " "
                            + getResources().getString(R.string.storage_explanation_2);

                    AlertDialog.Builder builder = new AlertDialog.Builder(WallpaperDetail.this);
                    builder.setMessage(fulldescPerm)
                            .setTitle(R.string.storage_required_title)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(WallpaperDetail.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            writeStorageRequest);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return false;

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            writeStorageRequest);
                    return false;
                }
            }
        } else {
            // Auto grant if pre-MM device
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case writeStorageRequest: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.received_permission, Toast.LENGTH_SHORT).show();
                    setDownload(sData.wallURL);
                } else {
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setArrowBack() {
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setPreview() {
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperDetail.this, WallpaperPreview.class);
                startActivity(intent);
                showInterstitial(true);
            }
        });
    }


    private void setWallButton() {

        setwallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View ourView) {
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load(sData.wallURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                WallpaperSelectDialog wallSelectDialog = new WallpaperSelectDialog(WallpaperDetail.this, resource, ourView);
                                wallSelectDialog.show(getFragmentManager(), "wallselectdialog");
                            }
                        });
            }
        });

        showInterstitial(true);
    }

    private void setShareButton() {

        final String shareText = getString(R.string.share_text) + " "
                + getString(R.string.app_name) + " app by "
                + getString(R.string.dev) + ": \n\n"
                + sData.wallName + "\n"
                + sData.wallURL;

        wallstuffshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(sendIntent, "Share wallpaper via:"));
            }
        });

    }

}
