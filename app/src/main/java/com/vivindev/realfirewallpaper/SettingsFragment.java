package com.vivindev.realfirewallpaper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TextView;
import com.github.javiersantos.appupdater.AppUpdater;
import com.vivindev.realfirewallpaper.config.admob;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.vivindev.realfirewallpaper.SettingsClasse.privacy_policy_url;

/**
 * Created by kingdov on 17/01/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    LinearLayout rate,share,contact,privacy,moreapps,update,consent;
    TextView app_name, version_name;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout linearlayout = (LinearLayout)v.findViewById(R.id.unitads);
        admob.admobBannerCall(getActivity(), linearlayout);

        app_name = (TextView) v.findViewById(R.id.app_name);
        version_name = (TextView) v.findViewById(R.id.version_name);
        app_name.setText(getResources().getString(R.string.app_name));
        try {
            version_name.setText("Version "+(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0)).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        rate = (LinearLayout) v.findViewById(R.id.about_rate);
        share = (LinearLayout) v.findViewById(R.id.about_share);
        moreapps = (LinearLayout) v.findViewById(R.id.about_moreapps);
        contact = (LinearLayout) v.findViewById(R.id.about_contact);
        privacy = (LinearLayout) v.findViewById(R.id.about_privacy);
        update = (LinearLayout) v.findViewById(R.id.update);
        consent = (LinearLayout) v.findViewById(R.id.gdpr_reset);

        update.setOnClickListener(this);
        rate.setOnClickListener(this);
        share.setOnClickListener(this);
        moreapps.setOnClickListener(this);
        contact.setOnClickListener(this);
        privacy.setOnClickListener(this);
        consent.setOnClickListener(this);

        ConsentInformation consentInformation = ConsentInformation.getInstance(getActivity().getApplicationContext());
//        consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        if(consentInformation.isRequestLocationInEeaOrUnknown()){
            consent.setVisibility(View.VISIBLE);
        }else{
            consent.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_rate:
                try {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getActivity().getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getActivity().getPackageName())));
                }
                break;
            case R.id.about_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey my friend(s) check out this amazing application \n https://play.google.com/store/apps/details?id="+ getActivity().getPackageName() +" \n";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.update:
                AppUpdater appUpdater = new AppUpdater(getActivity())
                        .setContentOnUpdateAvailable("Check out the latest version available to get the latest features and bug fixes")
                        .setCancelable(false)
                        .setButtonDoNotShowAgain(null)
                        .setButtonUpdate("Update now")
                        .setButtonDismiss("later")
                        .showAppUpdated(true)
                        .setTitleOnUpdateNotAvailable("Update not available")
                        .setContentOnUpdateNotAvailable("No update available. Check for updates again later!");
                appUpdater.start();
                Toast.makeText(getActivity(), "Wait to check update", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_moreapps:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SettingsClasse.more_apps_link)));
                } catch (android.content.ActivityNotFoundException anfe) {
                }
                break;
            case R.id.about_contact:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{SettingsClasse.contactMail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getPackageName());
                emailIntent.setType("text/plain");
                //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Messg content");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
                break;
            case R.id.about_privacy:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SettingsClasse.privacy_policy_url)));
                } catch (android.content.ActivityNotFoundException anfe) {
                }
                break;
            case R.id.gdpr_reset:
                ConsentInformation consentInformation = ConsentInformation.getInstance(getActivity().getApplicationContext());
                consentInformation.reset();
                consentInformation.requestConsentInfoUpdate(new String[]{"pub-6658816078552159"}, new ConsentInfoUpdateListener() {
                    @Override
                    public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                        // User's consent status successfully updated. Display the consent form if Consent Status is UNKNOWN
                        if (consentStatus == ConsentStatus.UNKNOWN) {
                            displayConsentForm();
                        }
                    }

                    @Override
                    public void onFailedToUpdateConsentInfo(String errorDescription) {
                        // Consent form error.
                        Log.e("ERROR", errorDescription);
                    }
                });
                Toast.makeText(getActivity(), "Consent reseted", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
        }

    }

    private ConsentForm consentForm;

    private void displayConsentForm() {

        consentForm = new ConsentForm.Builder(this.getContext(), getAppsPrivacyPolicy())
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Display Consent Form When Its Loaded
                        consentForm.show();
                    }

                    @Override
                    public void onConsentFormOpened() {

                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form is closed. From this method you can decided to display PERSONLIZED ads or NON-PERSONALIZED ads based 				on consentStatus.
                        Log.d("Consent","Status : " + consentStatus);
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                        Log.e("Error",errorDescription);

                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build();
        consentForm.load();
    }

    private URL getAppsPrivacyPolicy() {
        URL mUrl = null;
        try
        {
            mUrl = new URL(privacy_policy_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return mUrl;
    }

}
