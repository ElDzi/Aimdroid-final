package pl.eldzi.aimpanel.utils.events;

import org.json.JSONObject;

/**
 * Created by ElDzi on 2016-06-17.
 */
public class TeamSpeakServerCreateEvent {
    private JSONObject obj;

    public TeamSpeakServerCreateEvent(JSONObject obj) {
        this.obj = obj;
    }


    public JSONObject getResult() {
        return obj;
    }

}
