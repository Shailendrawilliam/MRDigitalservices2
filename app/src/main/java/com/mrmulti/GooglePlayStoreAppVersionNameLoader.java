package com.mrmulti;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;


public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

    public static String newVersion;

    protected String doInBackground(String... urls) {
        try {
            return
                    Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.solution.payseva" + "&hl=en")
                            .timeout(5000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select(".htlgb.htlgb")
                            .get(7)
                            .ownText();

        } catch (Exception e) {
            return "";
        }
    }

    protected void onPostExecute(String string) {
        newVersion = string;
        Log.e("newVersion", newVersion);
    }
}