package pl.eldzi.aimpanel;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.eldzi.aimpanel.profile.AimMinecraftServer;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.CommandSendEvent;
import pl.eldzi.aimpanel.utils.events.ConsoleLogEvent;
import pl.eldzi.aimpanel.utils.events.ConsoleServerEvent;
import pl.eldzi.aimpanel.utils.events.RestartServerEvent;
import pl.eldzi.aimpanel.utils.events.StartServerEvent;
import pl.eldzi.aimpanel.utils.events.StopServerEvent;

import static de.keyboardsurfer.android.widget.crouton.Style.INFO;

public class ServerActivity extends AppCompatActivity {
    private AimMinecraftServer s;
    private TextView sv;
    private TextView cons;

    private ImageView st, rl, ps, con, sett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");
        s = AimProfilesManager.admin_account.getMinecraftServers().get(id);
        sv = (TextView) findViewById(R.id.serverinfo);
        cons = (TextView) findViewById(R.id.consoletv);
        cons.setText("Inicjacja..");
        cons.setMovementMethod(new ScrollingMovementMethod());
        sv.setText("Serwer #" + id);
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
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder d = new AlertDialog.Builder(ServerActivity.this);
                d.setTitle("WPROWADŹ KOMENDĘ");

                final EditText t = new EditText(getApplicationContext());
                d.setView(t);

                d.setPositiveButton("wyślij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            s.sendCommand(t.getText().toString());
                        } catch (JSONException a) {
                            a.printStackTrace();
                        }
                    }
                });
                d.show();
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

    @Subscribe
    public void onSendCo(CommandSendEvent e) {
        JSONObject o = (JSONObject) e.getResult();
        for (int k = 0; k < 20; k++)
            System.out.println(o.toString());
    }
}
