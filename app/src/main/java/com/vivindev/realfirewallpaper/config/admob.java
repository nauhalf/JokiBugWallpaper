package com.vivindev.realfirewallpaper.config;

import com.vivindev.realfirewallpaper.SettingsClasse;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kingdov on 17/01/2017.
 */

public class admob {

    public static int mCount = 0;
    public static InterstitialAd mInterstitialAd;

    public static void admobBannerCall(Activity activity , final LinearLayout linerlayout){
        Bundle extras = new Bundle();

        ConsentInformation consentInformation = ConsentInformation.getInstance(activity.getApplicationContext());

        AdView adView = new AdView(activity);
        adView.setAdUnitId(SettingsClasse.admBanner);
        adView.setAdSize(AdSize.SMART_BANNER);
        if(consentInformation.isRequestLocationInEeaOrUnknown()){
            if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
                extras.putString("npa", "1");
            }

            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class,extras)
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            adView.loadAd(adRequest);
        }else {
            AdRequest.Builder builder = new AdRequest.Builder();
            adView.loadAd(builder.build());
        }
        linerlayout.setVisibility(View.GONE);
        linerlayout.setPadding(5,20,5,20);
        linerlayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                linerlayout.setVisibility(View.VISIBLE);
            }
        });

    }

    public static void initialInterstitial(final Activity activity){
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(SettingsClasse.Interstitial);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial(activity);
            }
        });
        requestNewInterstitial(activity);
    }

    public static void requestNewInterstitial(Activity activity) {
        Bundle extras = new Bundle();

        ConsentInformation consentInformation = ConsentInformation.getInstance(activity.getApplicationContext());
        if(consentInformation.isRequestLocationInEeaOrUnknown()){
            if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
                extras.putString("npa", "1");
            }

            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class,extras)
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mInterstitialAd.loadAd(adRequest);
        }else {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    public static void showInterstitial(boolean count){
        if(count){
            mCount++;
            if(SettingsClasse.interstitialFrequence == mCount) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mCount=0;
                }else mCount--;
            }
        } else if (mInterstitialAd.isLoaded()) mInterstitialAd.show();
    }
}
