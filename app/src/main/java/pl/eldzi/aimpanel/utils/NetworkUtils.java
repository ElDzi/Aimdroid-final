package pl.eldzi.aimpanel.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.net.URL;

/**
 * Created by ElDzi on 2016-04-23.
 */
public class NetworkUtils {
    public static boolean isOnline(Activity v) {
        ConnectivityManager cm =
                (ConnectivityManager) v.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean isURL(String url) {
        if (Patterns.WEB_URL.matcher(url).matches()) return true;
        if (Patterns.IP_ADDRESS.matcher(url).matches()) return true;
        return false;
    }

    public static String buildValidURL(String url) {
        if (URLUtil.isValidUrl(url))
            return url;

        if (!url.startsWith("http://") || !url.startsWith("https://"))
            url = "http://" + url;
        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url;
    }
}
