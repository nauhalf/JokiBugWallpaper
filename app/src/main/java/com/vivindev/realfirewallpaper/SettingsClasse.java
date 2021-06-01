package com.vivindev.realfirewallpaper;

import android.app.Activity;

/**
 * Created by kingdov on 17/01/2017.
 */

public class SettingsClasse extends Activity{
	public static String contactMail = " arlinvitm@gmail.com"; //Todo: replace email
	public static int interstitialFrequence = 3; //Todo: set freq
	public static String admBanner   ="ca-app-pub-3940256099942544/6300978111"; //adtest
	public static String Interstitial ="ca-app-pub-3940256099942544/1033173712"; //adtest
//	public static String admBanner   ="ca-app-pub-4211437030537226/6993034555"; //Todo: add banner id
//	public static String Interstitial ="ca-app-pub-4211437030537226/6801434544"; //Todo: add interstitial id
	public static String privacy_policy_url = "https://vivindev.blogspot.com/2020/11/vivin-privacy-policy-and-gdpr.html"; //Todo: Replace privacy
	public static String more_apps_link = "https://play.google.com/store/apps/developer?id=VinTm"; //Todo: Repplace your store

	public static Boolean isOnlineDB = true; // true = online, false = offline

	//online url
	public static String wallpaperDataBase = "https://www.dropbox.com/s/jvu7aj4qdcykcuy/db_realfire.json?raw=1"; //Todo: Replace DB

	//offline db
	public static String ourDataFilenameNoSlash = "db.json";
	public static String ourDataFilename = "/" + ourDataFilenameNoSlash;
}