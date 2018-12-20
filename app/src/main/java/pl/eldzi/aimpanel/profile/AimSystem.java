package pl.eldzi.aimpanel.profile;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.SystemLoadEvent;

public class AimSystem {
    private JSONObject obj;
    private AimProfile p;

    public AimSystem(AimProfile prof) {
        p = prof;
        send();
    }

    public void setObj(JSONObject s) {
        obj = s;
    }

    public void send() {
        p.loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(p.getUrl());
        h.setMaxRetries(5);
        h.addHeader("Authorization", p.getAuthToken());
        h.get("/api/v1/os/stats", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    //TODO: System Result Parser
                    SystemLoadEvent r = new SystemLoadEvent(o);
                    Events.getListenerHandler().post(r);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JSONObject getResult() {
        return obj;
    }

}
