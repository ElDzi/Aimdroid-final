package pl.eldzi.aimpanel.utils.events;

import org.json.JSONException;
import org.json.JSONObject;

import pl.eldzi.aimpanel.AimProfilesManager;

/**
 * Created by ElDzi on 2016-06-05.
 */
public class LicenseGetEvent {

    private JSONObject auth;

    public JSONObject getAuth() {
        return auth;
    }

    public LicenseGetEvent(JSONObject o) {
        auth = o;
    }
}
