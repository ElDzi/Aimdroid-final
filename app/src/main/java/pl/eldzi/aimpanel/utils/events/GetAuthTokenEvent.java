package pl.eldzi.aimpanel.utils.events;

import org.json.JSONObject;

/**
 * Created by ElDzi on 2016-06-17.
 */
public class GetAuthTokenEvent {
    private JSONObject obj;

    public GetAuthTokenEvent(JSONObject obj) {
        this.obj = obj;
    }


    public JSONObject getResult() {
        return obj;
    }


}
