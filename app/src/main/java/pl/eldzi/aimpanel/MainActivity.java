package pl.eldzi.aimpanel;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import pl.eldzi.aimpanel.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    public static boolean conn;
    private static RelativeLayout bar;


    protected void onCreate(Bundle b) {
        super.onCreate(b);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#d5ecd6"));
        setSupportActionBar(toolbar);
        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://aimpanel.pro/pl/"));
                startActivity(i);
            }
        });

        bar = (RelativeLayout) findViewById(R.id.loadingPanel);
        View v = getWindow().getDecorView().getRootView();
        conn = NetworkUtils.isOnline(this);
        final Snackbar utracone;
        utracone = Snackbar.make(v, "Utracone zostało połączenie z internetem!", Snackbar.LENGTH_INDEFINITE)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_MANUAL) {
                            snackbar.show();
                        }
                    }
                });
        utracone.setActionTextColor(Color.parseColor("#d5ecd6"));
        utracone.getView().setBackgroundColor(Color.parseColor("#4caf50"));


        utracone.setAction("PONÓW", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conn = NetworkUtils.isOnline(MainActivity.this);

                if (conn) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Połączenie nawiązane!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, NoInternetActivity.class);
                    startActivity(i);
                    return;
                }
            }
        });
        Button w = (Button) findViewById(R.id.wyjdz);
        Button s = (Button) findViewById(R.id.start);
        Button i = (Button) findViewById(R.id.info);
        s.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                conn = NetworkUtils.isOnline(MainActivity.this);
                if (conn) {
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                } else {

                }
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(i);

            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        if (!NetworkUtils.isOnline(this))
            showConnection(v);


    }

    private void showConnection(View v) {
        Snackbar s = Snackbar.make(v, "Utracone zostało połączenie z internetem!", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.parseColor("#d5ecd6"));
        s.getView().setBackgroundColor(Color.parseColor("#4caf50"));
        s.setAction("PONÓW", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conn = NetworkUtils.isOnline(MainActivity.this);

                if (conn) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Połączenie nawiązane!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, NoInternetActivity.class);
                    startActivity(i);
                    return;
                }
            }
        });
        s.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }
}
