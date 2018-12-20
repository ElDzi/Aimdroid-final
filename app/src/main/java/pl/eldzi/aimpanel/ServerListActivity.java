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

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.eldzi.aimpanel.profile.AimMinecraftServer;
import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.utils.Events;

public class ServerListActivity extends AppCompatActivity {
    private LinearLayout l;
    private ActionBar.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);
        l = (LinearLayout) findViewById(R.id.cc);
        lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Events.getListenerHandler().register(this);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    private ArrayList<Button> buttons = new ArrayList<>();

 /*   @Subscribe
    public void onAsync(AsyncResultEvent e) {
        if (e.getResult() instanceof JSONArray) {
            try {
                JSONArray arr = (JSONArray) e.getResult();
                for (int k = 0; k < arr.length(); k++) {
                    JSONObject o = arr.getJSONObject(k);
                    AimProfile p = AimProfilesManager.admin_account;
                    final AimMinecraftServer server = new AimMinecraftServer(p, o);

                    if (p.getMinecraftServers().containsKey(server.getID())) return;
                    if (p.getMinecraftServers().keySet().contains(server.getID())) return;

                    p.getMinecraftServers().put(server.getID(), server);


                    Button b = new Button(getApplicationContext());
                    b.setId(server.getID());
                    b.setText("Serwer #" + server.getID());
                    b.setBackgroundColor(Color.WHITE);
                    b.setTextColor(Color.parseColor("#8bc34a"));
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ServerListActivity.this, ServerActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("id", server.getID());
                            i.putExtras(b);
                            startActivity(i);
                            finish();

                        }
                    });
                    buttons.add(b);

                    l.addView(b, lp);
                    System.out.println("POBRANO " + k);
                }
            } catch (JSONException ea) {
                ea.printStackTrace();
            }
        } else {
            JSONObject o = (JSONObject) e.getResult();
            Crouton.makeText(ServerListActivity.this, "Błąd od strony serwera.. Spróbuj później!", Style.ALERT).show();
        }
    }*/


}
