package pl.eldzi.aimpanel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.profile.AimTeamSpeakServer;
import pl.eldzi.aimpanel.utils.DateUtils;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.DeleteTS3ServerEvent;
import pl.eldzi.aimpanel.utils.events.LoadServersTS3Event;

public class DeleteServerTS3Activity extends AppCompatActivity {
    private Button b;
    private AutoCompleteTextView t;
    private TextView data;
    public String nl = System.getProperty("line.separator");
    private int lastID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_server_mc);
        Events.getListenerHandler().register(this);
        AimProfilesManager.admin_account.loadTeamSpeak3Servers();
        b = (Button) findViewById(R.id.removeserver);
        t = (AutoCompleteTextView) findViewById(R.id.mcid1);
        data = (TextView) findViewById(R.id.empty);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String tx = t.getText().toString();
                if (tx == null) {
                    Toast.makeText(getApplicationContext(), "Wprowadz ID!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!DateUtils.isNumber(tx)) {
                    Toast.makeText(getApplicationContext(), "Wprowadz ID numeryczne!", Toast.LENGTH_LONG).show();
                    return;
                }


                int k = Integer.parseInt(tx);
                if (!AimProfilesManager.admin_account.getTeamSpeak3Servers().containsKey(k)) {
                    Crouton.makeText(DeleteServerTS3Activity.this, "Serwer o takim ID nie istnieje!", Style.ALERT).show();
                }
                lastID = k;
                AimProfile p = AimProfilesManager.admin_account;
                p.loadTeamSpeak3Servers();
                p.getTeamSpeak3Servers().get(k).deleteServer();
            }
        });
    }

    @Subscribe
    public void onLoad(LoadServersTS3Event e) {
        try {
            JSONArray arr = (JSONArray) e.getResult();
            for (int k = 0; k < arr.length(); k++) {
                JSONObject o = arr.getJSONObject(k);
                AimProfile p = AimProfilesManager.admin_account;
                final AimTeamSpeakServer server = new AimTeamSpeakServer(p, o);
                if (p.getTeamSpeak3Servers().containsKey(server.getID())) return;
                if (p.getTeamSpeak3Servers().keySet().contains(server.getID())) return;

                p.getTeamSpeak3Servers().put(server.getID(), server);
            }
        } catch (JSONException ea) {
            ea.printStackTrace();
        }
    }

    @Subscribe
    public void onEvents(DeleteTS3ServerEvent e) {
        try {
            JSONObject o = (JSONObject) e.getResult();
            if (o.getString("status").equalsIgnoreCase("ok")) {
                Crouton.makeText(DeleteServerTS3Activity.this, "Usunięto serwer TeamSpeak3 #" + lastID + " !", Style.CONFIRM).show();
                Intent i = new Intent(DeleteServerTS3Activity.this, ProfileActivity.class);
                startActivity(i);
            } else {
                Crouton.makeText(DeleteServerTS3Activity.this, "Nie udało się usunąć serwera TS3 #" + lastID + " !", Style.ALERT).show();
            }
        } catch (JSONException a) {
            Toast.makeText(getApplicationContext(), "Błąd przy usuwaniu serwera #" + lastID + " !", Toast.LENGTH_LONG).show();
        }
    }
}
