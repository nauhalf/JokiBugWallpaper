package com.vivindev.realfirewallpaper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.vivindev.realfirewallpaper.func.DataUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingdov on 17/01/2017.
 */

public class SplashActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    protected static List<DataUrl> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new AsyncFetch().execute();
    }

    private boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void writeJsonToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(SettingsClasse.ourDataFilenameNoSlash, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i("io", "Wrote file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String buffToString(Reader ourReader, boolean save) {
        try {
            BufferedReader reader = new BufferedReader(ourReader);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // write it to ourdata.json
            if (save) {
                if (!result.toString().equals(null)) {
                    writeJsonToFile(result.toString(), SplashActivity.this);
                }
            }

            return (result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {

        HttpURLConnection connection;
        URL url = null;

        @Override
        protected String doInBackground(String... strings) {

            if (isNetworkAvailable(SplashActivity.this) && SettingsClasse.isOnlineDB) {




                try {
                    url = new URL(SettingsClasse.wallpaperDataBase);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return e.toString();
                }

                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECTION_TIMEOUT);
                    connection.setRequestMethod("GET");

                } catch (IOException e1) {
                    e1.printStackTrace();
                    return e1.toString();
                }

                try {
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        InputStream inputStream = connection.getInputStream();
                        return buffToString(new InputStreamReader(inputStream), true);

                    } else {
                        File ourData = new File(SplashActivity.this.getFilesDir().getPath() + SettingsClasse.ourDataFilename);
                        if (ourData.exists()) {
                            return buffToString(new FileReader(ourData), false);
                        } else {
                            Toast.makeText(SplashActivity.this, "Cannot connect to server, please reopen app and try again.", Toast.LENGTH_SHORT).show();
                            return ("unsuccessful");
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return e2.toString();
                } finally {
                    connection.disconnect();
                }
            } else {

                String json = null;
                try {
                    InputStream is = getAssets().open(SettingsClasse.ourDataFilenameNoSlash);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return json;
//                File ourData = new File(SplashActivity.this.getFilesDir().getPath() + SettingsClasse.ourDataFilename);
//                try {
//                    return buffToString(new FileReader(ourData), false);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return e.toString();
//                }
            }


        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    DataUrl dataUrl = new DataUrl();
                    dataUrl.wallIndex = jsonData.getInt("wallpaper_index");
                    dataUrl.wallName = jsonData.getString("wallpaper_name");
                    dataUrl.wallSite = jsonData.getString("wallpaper_site_name");
                    dataUrl.wallSiteUrl = jsonData.getString("wallpaper_site_url");
                    dataUrl.wallURL = jsonData.getString("wallpaper_url");
                    data.add(dataUrl);
                }

                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();



            } catch (JSONException e) {
                Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
