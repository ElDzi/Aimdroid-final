package pl.eldzi.aimpanel.profile;

import android.util.Log;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.GetAuthTokenEvent;
import pl.eldzi.aimpanel.utils.events.GetUserEvent;
import pl.eldzi.aimpanel.utils.events.LoadServersTS3Event;
import pl.eldzi.aimpanel.utils.events.LoadUsersEvent;
import pl.eldzi.aimpanel.utils.events.TeamSpeakServerCreateEvent;

public class AimProfile {
    private String url, user, password;
    public String authToken;
    private HashMap<Integer, AimMinecraftServer> servers_mc = new HashMap<>();
    private HashMap<Integer, AimTeamSpeakServer> servers_ts3 = new HashMap<>();
    private ArrayList<AimUser> users;
    private AimUser myUser;
    private AimLicense license;
    private AimSystem sys;
    public boolean loaded;

    public void setAuthToken(String l) {
        authToken = l;
        loadLater();


    }

    public AimProfile(String url, String user, String password) {

        this.url = url;
        this.user = user;
        this.password = password;
        loaded = true;
        loadAuthToken();


    }

    public AimSystem getSystem() {
        return sys;
    }

    private void loadLater() {
        license = new AimLicense(this);
        sys = new AimSystem(this);
        users = getUsers();
        loadTeamSpeak3Servers();
        loaded = true;
    }

    public AimLicense getAimLicense() {
        return license;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public AimUser getMy() {
        if (myUser == null) {
            getByUsername(getUser());
            return myUser;
        }
        return myUser;
    }

    public HashMap<Integer, AimMinecraftServer> getMinecraftServers() {
        return servers_mc;
    }

    public HashMap<Integer, AimTeamSpeakServer> getTeamSpeak3Servers() {
        return servers_ts3;
    }

    public AimUser lastUser;

    public void setLastUser(AimUser u) {
        if (lastUser != null) {
            if (u.getUsername().equalsIgnoreCase(getUser())) {
                myUser = lastUser;
            } else {
                lastUser = u;
            }
        }
    }

    public void getByUsername(String username) {
        loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(url);
        h.setMaxRetries(5);
        h.addHeader("Authorization", getAuthToken());
        h.get("/api/v1/user/" + username + "/info", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    GetUserEvent e = new GetUserEvent(o);
                    Events.getListenerHandler().post(e);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public ArrayList<AimUser> getUsers() {
        loaded = false;
        if (users == null) {
            users = new ArrayList<AimUser>();
        }
        AndroidHttpClient h = new AndroidHttpClient(url);
        h.setMaxRetries(5);
        h.addHeader("Authorization", getAuthToken());
        h.get("/api/v1/user", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {

                try {
                    JSONArray o = new JSONArray(httpResponse.getBodyAsString());
                    LoadUsersEvent e = new LoadUsersEvent(o);
                    Events.getListenerHandler().post(e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return users;

    }

    public void loadAuthToken() {
        loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(getUrl());
        h.setMaxRetries(5);
        ParameterMap m = h.newParams().add("username", user).add("password", password);
        h.post("/api/v1/auth/login", m, new AsyncCallback() {
                    @Override
                    public void onComplete(HttpResponse httpResponse) {
                        try {
                            JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                            GetAuthTokenEvent e = new GetAuthTokenEvent(o);
                            Events.getListenerHandler().post(e);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("LOL", e.toString());
                    }
                }

        );


    }



/*    public JSONObject createMinecraftServer(int ID) {
        loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(getUrl());
        h.setMaxRetries(5);
        h.addHeader("Authorization", getAuthToken());
        ParameterMap m = h.newParams().add("id", String.valueOf(ID));
        h.post("/api/v1/services/mc", m, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    AsyncResultEvent e = new AsyncResultEvent(o);
                    Events.getListenerHandler().post(e);
                } catch (JSONException e) {

                }
            }


        });
        return getLastCreateServerData;

    }*/


    public void createTeamSpeak3Server(int ID) {
        loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(getUrl());
        h.setMaxRetries(5);
        h.addHeader("Authorization", getAuthToken());
        ParameterMap m = h.newParams().add("id", String.valueOf(ID));
        h.post("/api/v1/services/ts3", m, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    TeamSpeakServerCreateEvent e = new TeamSpeakServerCreateEvent(o);
                    Events.getListenerHandler().post(e);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    public void loadTeamSpeak3Servers() {
        loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(getUrl());
        h.setMaxRetries(2);
        h.addHeader("Authorization", getAuthToken());
        h.get("/api/v1/services/ts3", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    LoadServersTS3Event a = new LoadServersTS3Event(new JSONArray(httpResponse.getBodyAsString()));
                    Events.getListenerHandler().post(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    /*public void loadMinecraftServers() {
        AndroidHttpClient h = new AndroidHttpClient(getUrl());
        h.setMaxRetries(5);
        h.addHeader("Authorization", getAuthToken());
        h.get("/api/v1/services/mc", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    AsyncResultEvent s = new AsyncResultEvent(new JSONArray(httpResponse.getBodyAsString()));
                    Events.getListenerHandler().post(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }
*/
}
