package com.lucky.blocks.mods.mcpeaddons;

import android.util.Log;

import androidx.browser.trusted.sharing.ShareTarget;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@SuppressWarnings("ALL")
public class HttpHandler {
    private static final String TAG = "HttpHandler";

    public String makeServiceCall(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setRequestMethod(ShareTarget.METHOD_GET);
            return convertStreamToString(new BufferedInputStream(httpURLConnection.getInputStream()));
        } catch (MalformedURLException e) {
            String str2 = TAG;
            Log.e(str2, "MalformedURLException: " + e.getMessage());
            return null;
        } catch (ProtocolException e2) {
            String str3 = TAG;
            Log.e(str3, "ProtocolException: " + e2.getMessage());
            return null;
        } catch (IOException e3) {
            String str4 = TAG;
            Log.e(str4, "IOException: " + e3.getMessage());
            return null;
        } catch (Exception e4) {
            String str5 = TAG;
            Log.e(str5, "Exception: " + e4.getMessage());
            return null;
        }
    }

    private String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                    sb.append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                throw th;
            }
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
