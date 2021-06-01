package com.vivindev.realfirewallpaper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.javiersantos.appupdater.AppUpdater;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vivindev.realfirewallpaper.config.admob;
import com.vivindev.realfirewallpaper.func.DataUrl;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.vivindev.realfirewallpaper.SettingsClasse.privacy_policy_url;

/**
 * Created by kingdov on 17/01/2017.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences sharedPrefs;
    public static SharedPreferences.Editor editor;
    public static Gson gson ;
    public static List<String> listFavorites = new ArrayList<String>();
    public static List<DataUrl> favoriteData = new ArrayList<>();
    androidx.appcompat.widget.AppCompatTextView textView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // eu consent
        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
//        consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        consentInformation.requestConsentInfoUpdate(new String[]{getResources().getString(R.string.pubid)}, new ConsentInfoUpdateListener() {
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        admob.initialInterstitial(this);

        AppUpdater appUpdater = new AppUpdater(this)
                .setContentOnUpdateAvailable("Check out the latest version available to get the latest features and bug fixes")
                .setCancelable(false)
                .setButtonDoNotShowAgain(null)
                .setButtonUpdate("Update now")
                .setButtonDismiss("later")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!");
        appUpdater.start();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        gson = new Gson();
        String json = sharedPrefs.getString("favorites", "");
        if(json!="") {
            Type type = new TypeToken<List<String>>(){}.getType();
            listFavorites = gson.fromJson(json, type);
        }

        FavoritesFragment fragment2 = new FavoritesFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragementMain, fragment2);
        fragmentTransaction.commit();

        MainFragment fragment1 = new MainFragment();
        fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragementMain, fragment1);
        fragmentTransaction.commit();
        setTitle(getResources().getString(R.string.app_name));



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    private ConsentForm consentForm;

    private void displayConsentForm() {

        consentForm = new ConsentForm.Builder(this, getAppsPrivacyPolicy())
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            AppRater.showRateDialog(MainActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingsFragment fragment2 = new SettingsFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragementMain, fragment2);
            fragmentTransaction.commit();
            setTitle("Settings");
            admob.showInterstitial(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallpaper) {
            MainFragment fragment1 = new MainFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragementMain, fragment1);
            fragmentTransaction.commit();
            setTitle(getResources().getString(R.string.app_name));
            admob.showInterstitial(true);
        } else if (id == R.id.nav_favorites) {
            FavoritesFragment fragment2 = new FavoritesFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragementMain, fragment2);
            fragmentTransaction.commit();
            setTitle("Favorites Wallpaper");
            admob.showInterstitial(true);
        }  else if (id == R.id.nav_manage) {
            SettingsFragment fragment2 = new SettingsFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragementMain, fragment2);
            fragmentTransaction.commit();
            setTitle("Settings");
            admob.showInterstitial(true);
        } else if (id == R.id.nav_rate) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
            }
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey my friend(s) check out this amazing application \n https://play.google.com/store/apps/details?id="+ getPackageName() +" \n";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Application");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_send) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{SettingsClasse.contactMail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getPackageName());
            emailIntent.setType("text/plain");
            //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Messg content");
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for(final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
            if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            startActivity(emailIntent);
        } else if (id == R.id.nav_privacy) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(privacy_policy_url)));
            } catch (android.content.ActivityNotFoundException anfe) {
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
