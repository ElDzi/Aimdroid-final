package pl.eldzi.aimpanel.profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pl.eldzi.aimpanel.utils.DateUtils;

public class AimUser {
    private String username;

    public String getUsername() {
        return username;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public boolean exists() {
        return exists;
    }

    private Date created_at;
    private boolean exists;

    public AimUser(String user, Date ca) {
        username = user;
        created_at = ca;
        exists = true;

    }
}
