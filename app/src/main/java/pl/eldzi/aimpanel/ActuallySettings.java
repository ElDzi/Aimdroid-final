package pl.eldzi.aimpanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.utils.NetworkUtils;

/**
 * Created by ElDzi on 2016-04-24.
 */
public class ActuallySettings {

    public static String url, login, password;

    static {
        url = "http://demo.aimpanel.pro:3131";
        login = "admin";
        password = "S8brp7tXRwa99tZF";
        try {
            adminProfile = AimProfilesManager.registerAdminAccount(login, password, url);
        } catch (NetworkOnMainThreadException e) {
            adminProfile = null;
        }
    }

    public static AimProfile adminProfile;

    public static boolean refreshAimProfile() {
        try {
            AimProfile actually = AimProfilesManager.registerAdminAccount(login, password, url);
            adminProfile = actually;
            return true;
        } catch (NetworkOnMainThreadException e) {
            return false;
        }

    }

    public static void save(Context c) {
        try {
            JSONObject ob = new JSONObject();
            ob.put("url", url).put("login", login).put("password", password);
            OutputStreamWriter out = new OutputStreamWriter(c.openFileOutput("config.json", Context.MODE_PRIVATE));
            out.write(ob.toString());
            out.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (JSONException e1) {
            Log.e("JSON Exception", "Can not save JSON Object: " + e1.toString());
        }
    }

    public static void load(Context c) {
        String res = "";

        try {
            InputStream in = c.openFileInput("config.json");
            if (in != null) {
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader buff = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String a = "";
                while ((a = buff.readLine()) != null) {
                    sb.append(a);
                }
                in.close();
                res = sb.toString();
            }
            try {
                JSONObject ob = new JSONObject(res);
                login = ob.getString("login");
                password = ob.getString("password");
                url = ob.getString("url");
            } catch (JSONException e) {
                Log.e("JSON Exception", "Can not read JSON Object: " + e.toString());
            }

        } catch (FileNotFoundException e) {
            Log.e("Config Loader", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Config Loader", "Can not read file: " + e.toString());
        }


    }
}
