package pl.eldzi.aimpanel;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.profile.AimTeamSpeakServer;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.LoadServersTS3Event;

public class TS3ServerListActivity extends AppCompatActivity {
    private LinearLayout l;
    private ActionBar.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);
        l = (LinearLayout) findViewById(R.id.cc);
        lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Events.getListenerHandler().register(this);
        AimProfilesManager.admin_account.loadTeamSpeak3Servers();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TS3ServerListActivity.this, ProfileActivity.class);
        startActivity(i);
    }

    private ArrayList<Button> buttons = new ArrayList<>();

    @Subscribe
    public void onAsync(LoadServersTS3Event e) {
        try {
            JSONArray arr = (JSONArray) e.getResult();
            for (int k = 0; k < arr.length(); k++) {
                JSONObject o = arr.getJSONObject(k);
                AimProfile p = AimProfilesManager.admin_account;
                final AimTeamSpeakServer server = new AimTeamSpeakServer(p, o);
                if (p.getTeamSpeak3Servers().containsKey(server.getID())) return;
                if (p.getTeamSpeak3Servers().keySet().contains(server.getID())) return;

                p.getTeamSpeak3Servers().put(server.getID(), server);


                Button b = new Button(getApplicationContext());
                b.setId(server.getID());
                b.setText("Serwer #" + server.getID());
                b.setBackgroundColor(Color.WHITE);
                b.setTextColor(Color.parseColor("#8bc34a"));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(TS3ServerListActivity.this, TS3ServerActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("id", server.getID());
                        i.putExtras(b);
                        startActivity(i);
                        finish();

                    }
                });
                buttons.add(b);

                l.addView(b, lp);
            }
        } catch (JSONException ea) {
            ea.printStackTrace();
        }
    }


}
