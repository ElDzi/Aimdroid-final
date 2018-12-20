package pl.eldzi.aimpanel.profile;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pl.eldzi.aimpanel.utils.DateUtils;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.CommandSendEvent;
import pl.eldzi.aimpanel.utils.events.ConsoleLogEvent;
import pl.eldzi.aimpanel.utils.events.RestartServerEvent;
import pl.eldzi.aimpanel.utils.events.StartServerEvent;
import pl.eldzi.aimpanel.utils.events.StopServerEvent;

public class AimMinecraftServer {
    /*
    *TODO: Working Events System..
    * @author ElDzi
    * CLASS OFF
     */
    private int spid, pid, ram, upt;
    private float cpu_per;
    private String sess, state;
    private boolean cr;
    private Date s, c;
    private int ID;
    public AimProfile profile;

    public AimMinecraftServer(AimProfile profile, JSONObject obj) {
        try {
            cr = obj.getBoolean("crashed");
            c = DateUtils.parse(obj.getString("created_at"));
            this.state = obj.getString("state");
            ID = obj.getInt("id");
            this.profile = profile;

            update();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getID() {
        return ID;
    }

    public Date getStartDate() {
        return s;
    }

    public int getRamKb() {
        return ram;
    }

    public int getUptime() {
        return upt;
    }

    public String getState() {
        return state;
    }

    public Date getCreateDate() {
        return c;
    }

    public float getCpuUsagePercentage() {
        return cpu_per;
    }

    public boolean isCrashed() {
        return cr;
    }

    public int getScreenSessionPID() {
        return spid;
    }

    public int getPID() {
        return pid;
    }

    public String getSessionName() {
        return sess;
    }

    private String startMSG = "";
    private String restartMSG = "";
    private String stopMSG = "";

    public String startServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/start", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    StartServerEvent s = new StartServerEvent(getID(), o);
                    Events.getListenerHandler().post(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ;
                }
            }

            @Override
            public void onError(Exception e) {
                startMSG = "";
            }
        });
        return startMSG;
    }

    public String restartServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/restart", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    RestartServerEvent s = new RestartServerEvent(getID(), o);
                    Events.getListenerHandler().post(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                restartMSG = "";
            }
        });
        return restartMSG;
    }


    public void deleteServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.delete("/api/v1/services/mc/" + ID, null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    //AsyncResultEvent s = new AsyncResultEvent(o);
                    //Events.getListenerHandler().post(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public String stopServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/stop", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    StopServerEvent s = new StopServerEvent(getID(), o);
                    Events.getListenerHandler().post(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                stopMSG = "";
            }
        });
        return stopMSG;

    }


    public String[] getLastLinesFromLog() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/log", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                //lastLogs = new JSONObject(httpResponse.getBodyAsString());
            }


        });


        // REMOVING HTML and another shits
        String f = "";
        f = f.replaceAll("\\<[^>]*>", "");
        f = f.replace("[\"", "").replace("\"", "").replace("\\tat", "")
                .replace("type &quot;help&quot; or &quot;?&quot;", "");
        String[] kl = f.split(",");
        return kl;
    }

    public void getLastLinesFromConsole() {
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/console", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                String lastConsole = httpResponse.getBodyAsString();
                ConsoleLogEvent e = new ConsoleLogEvent(getID(), lastConsole);
                Events.getListenerHandler().post(e);
            }
        });
    }

    public String[] getLastLinesFromConsole(int range, String[] liness) {

        List<String> lines = Arrays.asList(liness);
        if (range > lines.size())
            return lines.toArray(new String[lines.size()]);
        ListIterator<String> it = lines.listIterator(lines.size());
        List<String> current = new ArrayList<>();
        while (it.previousIndex() != lines.size() - range) {
            current.add(it.previous());
        }
        return current.toArray(new String[current.size()]);
    }

    public String[] getLastLinesFromLog(int range) {

        List<String> lines = Arrays.asList(getLastLinesFromLog());
        if (range > lines.size())
            return lines.toArray(new String[lines.size()]);
        ListIterator<String> it = lines.listIterator(lines.size());
        List<String> current = new ArrayList<>();
        while (it.previousIndex() != lines.size() - range) {
            if (it.previous() == "")
                current.add("");
            else
                current.add(it.previous());
        }
        return current.toArray(new String[current.size()]);
    }

    private boolean lastSendCommandResult;

    public boolean sendCommand(String command) throws JSONException {
        profile.loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(profile.getUrl());
        h.setMaxRetries(5);
        ParameterMap p = h.newParams().add("cmd", command);
        h.post("/api/v1/services/mc/" + ID + "/cmd", p, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    CommandSendEvent event = new CommandSendEvent(getID(), o);
                    Events.getListenerHandler().post(event);
                } catch (JSONException a) {
                    a.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

                lastSendCommandResult = false;
            }
        });
        return lastSendCommandResult;
    }

    private String lastRandomPassSFTP;

    public String getRandomPasswordForSFTP() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/password/random", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    lastRandomPassSFTP = o.getString("new_password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                lastRandomPassSFTP = "";
            }
        });
        return lastRandomPassSFTP;
    }

    private HashMap<String, Object> outMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getServerProperties() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/server.properties", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                outMap.clear();
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    Iterator<String> nameItr = o.keys();
                    while (nameItr.hasNext()) {
                        String k = nameItr.next();
                        Object v = o.get(k);
                        outMap.put(k, v);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        return outMap;
    }

    private ArrayList<String> lastStartParams;

    public ArrayList<String> getStartParametrs() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/mc/" + ID + "/java/parameters", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONArray o = new JSONArray(httpResponse.getBodyAsString());
                    for (int k = 0; k < o.length(); k++) {
                        lastStartParams.add(o.getString(k));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        return lastStartParams;
    }

    public void setStartParametrs(ArrayList<String> parametrs) {
        profile.loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(profile.getUrl());
        h.setMaxRetries(5);
        JSONArray a = new JSONArray(parametrs);
        ParameterMap m = h.newParams().add("params", a.toString());
        h.post("/api/v1/services/mc/" + ID + "/java/parameters", m, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {

            }
        });

    }

    public void update() throws JSONException {
        profile.loaded = false;
        AndroidHttpClient h = new AndroidHttpClient(profile.getUrl());
        h.setMaxRetries(5);
        h.addHeader("Authorization", profile.getAuthToken());
        h.get("/api/v1/services/mc/" + ID + "/info", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    if (!o.isNull("session"))
                        sess = o.getString("session");
                    if (!o.isNull("screen_pid"))
                        spid = o.getInt("screen_pid");
                    if (!o.isNull("pid"))
                        pid = o.getInt("pid");
                    if (!o.isNull("cpu"))
                        cpu_per = o.getInt("cpu");
                    if (!o.isNull("ram_kb"))
                        ram = o.getInt("ram_kb");
                    if (!o.isNull("crashed"))
                        cr = o.getBoolean("crashed");
                    if (!o.isNull("started_at"))
                        s = DateUtils.parse(o.getString("started_at"));
                    if (!o.isNull("created_at"))
                        c = DateUtils.parse(o.getString("created_at"));
                    if (!o.isNull("state"))
                        state = o.getString("state");
                    if (!o.isNull("uptime_sec"))
                        upt = o.getInt("uptime_sec");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
