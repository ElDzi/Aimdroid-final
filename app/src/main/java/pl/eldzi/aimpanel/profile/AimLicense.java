package pl.eldzi.aimpanel.profile;

import android.util.Log;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pl.eldzi.aimpanel.utils.DateUtils;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.LicenseGetEvent;

public class AimLicense {

    private AimProfile p;
    public boolean valid;
    public String notes, name;
    public Date to;

    public AimLicense(AimProfile prof) {
        p = prof;


    }

    public AimLicense send() {
        p.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(p.getUrl());
        httpClient.setMaxRetries(5);
        String t = p.getAuthToken();
        httpClient.addHeader("Authorization", t);
        httpClient.get("/api/v1/license", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    LicenseGetEvent event = new LicenseGetEvent(o);
                    Events.getListenerHandler().post(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                valid = false;
            }
        });


        return this;
    }

    public void set(JSONObject o) {
        try {
            valid = o.getBoolean("valid");
            notes = o.getString("notes");
            to = DateUtils.parse(o.getString("valid_to"));
            name = o.getString("name");
            Log.e("FINISHED", "AimLicense set!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        return valid;
    }

    public String getNotes() {
        return notes;
    }

    public String getName() {
        return name;
    }

    public Date getTo() {
        return to;
    }

}
