package com.vivindev.realfirewallpaper;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;


public class AppRater {
    private final static String APP_TITLE = "App";// App Name


    public static void showRateDialog(final Context mContext) {
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.drawable.ic_star);
        builder.setTitle("Rate " + APP_TITLE);    
        builder.setMessage(mContext.getResources().getString(R.string.description_rate));

        //Button One : Yes
        builder.setPositiveButton(mContext.getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*Toast.makeText(mContext, "Yes button Clicked!", Toast.LENGTH_LONG).show();*/
                System.exit(1);

            }
        });


        //Button Two : No
        builder.setNegativeButton(mContext.getResources().getString(R.string.improvement), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                      	
            	Intent emailIntent = new Intent(Intent.ACTION_SEND);
            	emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vivindev.dev@gmail.com"});
            	emailIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getPackageName());
                emailIntent.setType("text/plain");
                //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Messg content");
                final PackageManager pm = mContext.getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                mContext.startActivity(emailIntent);
            	
            }
        });


        //Button Three : Neutral
        builder.setNeutralButton(mContext.getResources().getString(R.string.rate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(mContext, "Neutral button Clicked!", Toast.LENGTH_LONG).show();
                //dialog.cancel();
            	try {
            		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+mContext.getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                	mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+mContext.getPackageName())));
                }
            	
            }
        });


        AlertDialog diag = builder.create();
        diag.show();
              
    }
    
}