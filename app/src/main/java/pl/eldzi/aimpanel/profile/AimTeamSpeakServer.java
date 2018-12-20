package pl.eldzi.aimpanel.profile;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import pl.eldzi.aimpanel.utils.DateUtils;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.ConsoleLogEvent;
import pl.eldzi.aimpanel.utils.events.DeleteTS3ServerEvent;
import pl.eldzi.aimpanel.utils.events.RestartServerEvent;
import pl.eldzi.aimpanel.utils.events.StartServerEvent;
import pl.eldzi.aimpanel.utils.events.StopServerEvent;

public class AimTeamSpeakServer {
    private int spid, pid, ram, upt;
    private float cpu_per;
    private String sess, state;
    private boolean cr;
    private Date s, c;
    private int ID;
    private AimProfile profile;

    public AimTeamSpeakServer(AimProfile prof, JSONObject o) {
        profile = prof;
        try {
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
            if (!o.isNull("crashed"))
                cr = o.getBoolean("crashed");
            if (!o.isNull("created_at"))
                c = DateUtils.parse(o.getString("created_at"));
            if (!o.isNull("id"))
                ID = o.getInt("id");
            if (!o.isNull("id"))
                ID = o.getInt("id");
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

    public void startServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/ts3/" + ID + "/start", null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    StartServerEvent s = new StartServerEvent(getID(), o);
                    Events.getListenerHandler().post(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    public void restartServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/ts3/" + ID + "/restart", null, new AsyncCallback() {
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

        });
    }


    public void deleteServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.delete("/api/v1/services/ts3/" + ID, null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                try {
                    JSONObject o = new JSONObject(httpResponse.getBodyAsString());
                    DeleteTS3ServerEvent e = new DeleteTS3ServerEvent(o);
                    Events.getListenerHandler().post(e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void stopServer() {
        profile.loaded = false;
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/ts3/" + ID + "/stop", null, new AsyncCallback() {
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

        });


    }

    public void getLastLinesFromConsole() {
        final AndroidHttpClient httpClient = new AndroidHttpClient(profile.getUrl());
        httpClient.addHeader("Authorization", profile.getAuthToken());
        httpClient.setMaxRetries(5);
        httpClient.get("/api/v1/services/ts3/" + ID + "/console", null, new AsyncCallback() {
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
//
//
//    public String[] getLastLinesFromLog() {
//        Webb w = Webb.create();  w.setDefaultHeader("Connection", "close");
//        JSONArray o = w.get(p.getUrl() + "api/v1/services/ts3/" + ID + "/log").header("Authorization", p.getAuthToken())
//                .ensureSuccess().asJsonArray().getBody();
//
//        String f = o.toString();
//        f = f.replaceAll("\\<[^>]*>", "");
//        f = f.replace("[\"", "").replace("\"", "").replace("\\tat", "")
//                .replace("type &quot;help&quot; or &quot;?&quot;", "");
//        String[] kl = f.split(",");
//        return kl;
//    }
//
//    public String[] getLastLinesFromConsole() {
//        Webb w = Webb.create();  w.setDefaultHeader("Connection", "close");
//        JSONArray o = w.get(p.getUrl() + "api/v1/services/ts3/" + ID + "/console")
//                .header("Authorization", p.getAuthToken()).ensureSuccess().asJsonArray().getBody();
//
//        String f = o.toString();
//        f = f.replaceAll("\\<[^>]*>", "");
//        f = f.replace("[\"", "").replace("\"", "").replace("\\tat", "")
//                .replace("type &quot;help&quot; or &quot;?&quot;", "");
//        String[] kl = f.split(",");
//        for (String a : kl) {
//            System.out.println(a);
//        }
//        return kl;
//    }
//
//    public String[] getLastLinesFromConsole(int range) {
//
//        List<String> lines = Arrays.asList(getLastLinesFromConsole());
//        if (range > lines.size())
//            return lines.toArray(new String[lines.size()]);
//        ListIterator<String> it = lines.listIterator(lines.size());
//        List<String> current = new ArrayList<>();
//        while (it.previousIndex() != lines.size() - range) {
//            current.add(it.previous());
//        }
//        return current.toArray(new String[current.size()]);
//    }
//
//    public String[] getLastLinesFromLog(int range) {
//
//        List<String> lines = Arrays.asList(getLastLinesFromLog());
//        if (range > lines.size())
//            return lines.toArray(new String[lines.size()]);
//        ListIterator<String> it = lines.listIterator(lines.size());
//        List<String> current = new ArrayList<>();
//        while (it.previousIndex() != lines.size() - range) {
//            if (it.previous() == "")
//                current.add("");
//            else
//                current.add(it.previous());
//        }
//        return current.toArray(new String[current.size()]);
//    }
//
//    public String getRandomPasswordForSFTP() {
//        Webb w = Webb.create();  w.setDefaultHeader("Connection", "close");
//        JSONObject o = w.get(p.getUrl() + "api/v1/services/ts3/" + ID + "/password/random")
//                .header("Authorization", p.getAuthToken()).ensureSuccess().asJsonObject().getBody();
//        try {
//            return o.getString("new_password");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

}
