package pl.eldzi.aimpanel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.utils.NetworkUtils;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    //MATERIAL DESIGN ANIMATION!
    private AutoCompleteTextView url1, url2;
    private View mLoginFormView;

    private static boolean f = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        setContentView(R.layout.activity_login);
        ColorDrawable d = new ColorDrawable(Color.parseColor("#d5ecd6"));
        getSupportActionBar().setBackgroundDrawable(d);
        url1 = (AutoCompleteTextView) findViewById(R.id.URL);
        url1.setHint("Adres URL");
        url2 = (AutoCompleteTextView) findViewById(R.id.URL1);
        url2.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);


        mLoginFormView = findViewById(R.id.login_form);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (f == false) {
                    String ip = url1.getText().toString();
                    if (!NetworkUtils.isURL(ip)) {
                        Toast.makeText(LoginActivity.this, "Wprowadź poprawny adres URL/IP", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ip = NetworkUtils.buildValidURL(ip);

                    url1.setText("");
                    url2.setText("");
                    url1.setHint("LOGIN");
                    url2.setHint("HASŁO");
                    mEmailSignInButton.setHint("PRZEJDŹ DALEJ");
                    url2.setVisibility(View.VISIBLE);

                    f = true;
                } else {
                    String login = url1.getText().toString();
                    String pass = url2.getText().toString();
                    if (login.equals("") || pass.equals("")) {
                        Toast.makeText(getApplicationContext(), "Wprowadź poprawne dane! Możliwe że adres url jest błędny!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    AimProfilesManager.registerAdminAccount(login, pass, ActuallySettings.url);
                    f = false;
                    Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(i);
                }
            }

        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });


        } else {
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (f == true) {
            url1.setText("");
            url2.setText("");
            url1.setHint("Adres URL");
            url2.setVisibility(View.INVISIBLE);
            return;
        } else {
            super.onBackPressed();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


}

