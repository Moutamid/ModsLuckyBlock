package com.lucky.blocks.mods.mcpeaddons;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    static Dialog dialog;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String ITEM = "ITEM";
    public static final String MIX = "mix";
    public static final String MOD = "mod";
    public static final String MAP = "map";
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgJMiq7qhNcmNRDFMzPWGLx9MOfmFV9/myZ/Boi7+GZPvftT/6jzOa0baSiJ5HSX8bsBwvWRvIpz2y4GJyVEHhcOYoPR0dRZMUQ5LvsxTAdXWAZk/Arr+jhivV0ttJsqpgsr6BJDNtVky7gtK/4aN3PLbyhQz16He/rR/TA5XnqOkNqo6R2uN34mLR0eVaxY52p9MaaOY4z2G1th2KJB9B2TgQGq8FVLu9KJddZwfRPX1zwCR8XKyQcs0OYeWCXKqY4JmB/Wpoflg2UnWNO7IldndlqUjZmy5AM3KbPztPBHI9kIZ27qCLHWju7Fph6ZTl9YciX3o9Y2Y8di6NwdVtQIDAQAB";
    public static final  String VIP_MONTH = "vip.month.com.lucky.blocks.mods";
    public static final  String VIP_LIFE = "vip.lifetime.com.lucky.blocks.mods";

    public static String getFormattedDate(long date){
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog(){
        dialog.show();
    }

    public static void dismissDialog(){
        dialog.dismiss();
    }

    public static void checkApp(Activity activity) {
        String appName = "modsApp";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("modsApp");
        db.keepSynced(true);
        return db;
    }

}
