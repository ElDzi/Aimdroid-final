package pl.eldzi.aimpanel;


import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pl.eldzi.aimpanel.profile.AimProfile;

public class AimProfilesManager {
    public static HashMap<String, AimProfile> profiles = new HashMap<>();
    public static AimProfile admin_account;

    public static HashMap<String, AimProfile> getProfiles() {
        return profiles;
    }

    public static AimProfile registerAdminAccount(String username, String password, String url) {
        admin_account = new AimProfile(url, username, password);
        return admin_account;
    }

    public static AimProfile registerAimProfile(String username, String password, String url) {
        if (profiles.containsKey(username.toLowerCase()))
            return profiles.get(username.toLowerCase());
        return createUser(url, username, password);
    }

    private static AimProfile lastProfile;

    public static AimProfile createUser(final String url, final String username, final String password) {
        if (admin_account != null) {
            AndroidHttpClient h = new AndroidHttpClient(url);
            h.addHeader("Authorization", admin_account.getAuthToken());
            ParameterMap m = h.newParams().add("username", username).add("password", password);
            h.post("/api/v1/user", m, new AsyncCallback() {
                @Override
                public void onComplete(HttpResponse httpResponse) {
                    try {
                        JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                        if (o.getString("status").equalsIgnoreCase("ok")) {
                            AimProfile p = new AimProfile(url, username, password);
                            lastProfile = p;
                            profiles.put(p.getUser().toLowerCase(), p);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
        return lastProfile;
    }
}
