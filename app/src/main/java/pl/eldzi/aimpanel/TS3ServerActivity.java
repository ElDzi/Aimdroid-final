package pl.eldzi.aimpanel;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.eldzi.aimpanel.profile.AimMinecraftServer;
import pl.eldzi.aimpanel.profile.AimTeamSpeakServer;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.CommandSendEvent;
import pl.eldzi.aimpanel.utils.events.ConsoleLogEvent;
import pl.eldzi.aimpanel.utils.events.RestartServerEvent;
import pl.eldzi.aimpanel.utils.events.StartServerEvent;
import pl.eldzi.aimpanel.utils.events.StopServerEvent;

public class TS3ServerActivity extends AppCompatActivity {
    private AimTeamSpeakServer s;
    private TextView sv;
    private TextView cons;

    private ImageView st, rl, ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ts3_server);
        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");
        s = AimProfilesManager.admin_account.getTeamSpeak3Servers().get(id);
        sv = (TextView) findViewById(R.id.serverinfo);
        cons = (TextView) findViewById(R.id.consoletv);
        cons.setText("Inicjacja..");
        cons.setMovementMethod(new ScrollingMovementMethod());
        sv.setText("Serwer TS3 #" + id);
        Events.getListenerHandler().register(this);
        st = findByR(R.id.play);
        s.getLastLinesFromConsole();
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.startServer();
            }
        });
        rl = findByR(R.id.reload);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.restartServer();
            }
        });
        ps = findByR(R.id.pause);
        ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.stopServer();
            }
        });


    }

    public ImageView findByR(int d) {
        return (ImageView) findViewById(d);
    }

    @Subscribe
    public void onAsyncS(StartServerEvent e) {
        try {
            JSONObject o = (JSONObject) e.getResult();
            if (!o.isNull("msg"))
                Crouton.makeText(this, "Polecenie startu dodane do kolejki!", Style.INFO).show();
            else
                o.getString("msg");
        } catch (JSONException a) {
            a.printStackTrace();
        }
        s.getLastLinesFromConsole();
    }

    @Subscribe
    public void onAsyncR(RestartServerEvent e) {
        try {
            JSONObject o = (JSONObject) e.getResult();
            if (!o.isNull("msg"))
                Crouton.makeText(this, "Polecenie restartu dodane do kolejki!", Style.INFO).show();
            else
                o.getString("msg");
        } catch (JSONException a) {
            a.printStackTrace();
        }
        s.getLastLinesFromConsole();
    }

    @Subscribe
    public void onAsyncR(StopServerEvent e) {
        try {
            JSONObject o = (JSONObject) e.getResult();
            if (!o.isNull("msg"))
                Crouton.makeText(this, "Polecenie stopu dodane do kolejki!", Style.INFO).show();
            else
                o.getString("msg");
        } catch (JSONException a) {
            a.printStackTrace();
        }
        s.getLastLinesFromConsole();
    }

    @Subscribe
    public void onASyncS(ConsoleLogEvent e) {
        String o = (String) e.getResult();
        String f = o.toString();
        f = f.replaceAll("\\<[^>]*>", "");
        f = f.replace("[\"", "").replace("\"", "").replace("\\tat", "").replace("type &quot;help&quot; or &quot;?&quot;", "");
        int k = f.split(",").length;
        String s = f.replaceAll(",", "\n");
        cons.setText(s);
        cons.setTextColor(Color.WHITE);
        cons.setMaxLines(k);

    }


}
