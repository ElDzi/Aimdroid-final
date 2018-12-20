package pl.eldzi.aimpanel;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

public class DeleteServerMCActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
/*    private Button b;
    private AutoCompleteTextView t;
    private TextView data;
    public String nl = System.getProperty("line.separator");
    private int lastID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_server_mc);
        Events.getListenerHandler().register(this);
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
                lastID = k;
                AimProfile p = AimProfilesManager.admin_account;
                if (!p.getMinecraftServers().containsKey(k)) {
                    Toast.makeText(getApplicationContext(), "Serwer o takim numerze nie istnieje!", Toast.LENGTH_LONG).show();
                    return;
                }

                p.getMinecraftServers().get(k).deleteServer();
            }
        });
    }

    @Subscribe
    public void onEvents(DeleteTS3ServerEvent e) {
        try {
            JSONObject o = (JSONObject) e.getResult();
            if (o.getString("status").equalsIgnoreCase("ok")) {
                Toast.makeText(getApplicationContext(), "Usunięto serwer Minecraft #" + lastID + " !", Toast.LENGTH_LONG).show();
                Intent i = new Intent(DeleteServerMCActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        } catch (JSONException a) {
            Toast.makeText(getApplicationContext(), "Błąd przy usuwaniu serwera #" + lastID + " !", Toast.LENGTH_LONG).show();
        }
    }
}*/
