package pl.eldzi.aimpanel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.utils.DateUtils;
import pl.eldzi.aimpanel.utils.Events;

public class CreateMcActivity extends AppCompatActivity {
    private Button b;
    private AutoCompleteTextView t;
    private TextView data;
    public String nl = System.getProperty("line.separator");
    private int lastID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mc);
        Events.getListenerHandler().register(this);
        b = (Button) findViewById(R.id.createserver);
        t = (AutoCompleteTextView) findViewById(R.id.mcid);
        data = (TextView) findViewById(R.id.textView8);
        t.setHint("Wpisz ID");
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
                lastID = k;
                AimProfile p = AimProfilesManager.admin_account;
                if (p.getMinecraftServers().containsKey(k)) {
                    Toast.makeText(getApplicationContext(), "Serwer o takim numerze juz istnieje!", Toast.LENGTH_LONG).show();

                    return;
                }



                cToast = Toast.makeText(getApplicationContext(), "Tworzenie...", Toast.LENGTH_SHORT);
                cToast.show();
            }
        });
    }

    private Toast cToast;

/*    @Subscribe
    public void onEvent(AsyncResultEvent e) {
        try {
            JSONObject p = (JSONObject) e.getResult();
            String stat = p.getString("status");
            if (stat.equalsIgnoreCase("ok")) {
                Toast.makeText(getApplicationContext(), "Stworzono!", Toast.LENGTH_LONG).show();
                data.setVisibility(View.VISIBLE);
                Button ab = (Button) findViewById(R.id.back);
                ab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CreateMcActivity.this, ProfileActivity.class);
                        startActivity(i);
                    }
                });
                ab.setVisibility(View.VISIBLE);
                t.setVisibility(View.INVISIBLE);
                cToast.cancel();
                b.setVisibility(View.INVISIBLE);
                data.setText("  DANE DO SERWERA #" + lastID + " :" + nl + nl + "  Game Port:        " + p.getInt("game_port") + nl + nl + "  RCON Port:         " + p.getInt("rcon_port") + nl + nl + "  RCON Password:           " + p.getString("rcon_password") + nl + nl + nl + "ZAPISZ TE DANE!!!");

            }
        } catch (JSONException a) {
            a.printStackTrace();
        }
    }*/
}
