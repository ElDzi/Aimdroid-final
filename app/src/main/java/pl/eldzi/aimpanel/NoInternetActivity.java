package pl.eldzi.aimpanel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import pl.eldzi.aimpanel.utils.NetworkUtils;

public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
        setContentView(R.layout.activity_no_internet);
        Button ba = (Button) findViewById(R.id.button);
        ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.conn = NetworkUtils.isOnline(NoInternetActivity.this);
                if (MainActivity.conn) {
                    Intent i = new Intent(NoInternetActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Toast t = Toast.makeText(NoInternetActivity.this.getApplicationContext(), "W dalszym ciągu nie jesteś w trybie online!", Toast.LENGTH_LONG);
                    t.getView().setBackgroundColor(Color.parseColor("#4caf50"));
                    t.show();
                }
            }
        });
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.people);
        String dir = Environment.getExternalStorageDirectory() + "/pics/";
        File f = new File(dir);
        if (!f.mkdirs()) {
            if (!f.exists()) {
                Toast.makeText(this, "Folder error", Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(dir + "person.png");
                    b.compress(Bitmap.CompressFormat.PNG, 75, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        }
    }

    private boolean conn;

    private void showConnection(View v) {
        conn = NetworkUtils.isOnline(this);
        Snackbar s = Snackbar.make(v, "Utracone zostało połączenie z internetem!", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.parseColor("#d5ecd6"));
        s.getView().setBackgroundColor(Color.parseColor("#4caf50"));
        s.setAction("PONÓW", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conn = NetworkUtils.isOnline(NoInternetActivity.this);

                if (conn) {
                    Toast.makeText(NoInternetActivity.this.getApplicationContext(), "Połączenie nawiązane!", Toast.LENGTH_SHORT).show();
                } else {
                    showConnection(v);
                }
            }
        });
        s.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NoInternetActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
