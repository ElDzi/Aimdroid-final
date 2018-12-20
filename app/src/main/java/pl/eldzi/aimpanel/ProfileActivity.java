package pl.eldzi.aimpanel;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import pl.eldzi.aimpanel.fragments.LicenseFragment;
import pl.eldzi.aimpanel.fragments.MinecraftFragment;
import pl.eldzi.aimpanel.fragments.ProfilFragment;
import pl.eldzi.aimpanel.fragments.SystemFragment;
import pl.eldzi.aimpanel.fragments.TeamSpeakFragment;
import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.NetworkUtils;
import pl.eldzi.aimpanel.utils.events.LicenseGetEvent;

public class ProfileActivity extends AppCompatActivity
        implements LicenseFragment.OnFragmentInteractionListener, TeamSpeakFragment.OnFragmentInteractionListener, MinecraftFragment.OnFragmentInteractionListener, ProfilFragment.OnFragmentInteractionListener, SystemFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    public AimProfile p;
    private LicenseFragment lic;
    private ProfilFragment prof;
    private SystemFragment sys;
    //  private MinecraftFragment mf;
    private TeamSpeakFragment tf;
    private ServerListActivity sl;


    public void onFragmentInteraction(Uri uri) {

    }

    public boolean conn;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        this.view = view;
        return view;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toast.makeText(getApplicationContext(), "Ładowanie aplikacji..", Toast.LENGTH_LONG);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lic = new LicenseFragment();
        sys = new SystemFragment();
        prof = new ProfilFragment();
        sl = new ServerListActivity();
        // mf = new MinecraftFragment();
        tf = new TeamSpeakFragment();
        setSupportActionBar(toolbar);
        CoordinatorLayout c = (CoordinatorLayout) findViewById(R.id.coordlay);
        Events.getListenerHandler().register(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        p = AimProfilesManager.admin_account;
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(ProfileActivity.this);
        conn = NetworkUtils.isOnline(this);
        if (!conn)

        {
            Snackbar s = Snackbar.make(getView(), "Utracone zostało połączenie z internetem!", Snackbar.LENGTH_INDEFINITE)
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event == Snackbar.Callback.DISMISS_EVENT_MANUAL) {
                                snackbar.show();
                            }
                        }
                    }).setActionTextColor(Color.parseColor("#d5ecd6"));
            s.getView().setBackgroundColor(Color.parseColor("#4caf50"));
            s.setAction("PONÓW", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conn = NetworkUtils.isOnline(ProfileActivity.this);

                    if (conn) {
                        Toast.makeText(ProfileActivity.this.getApplicationContext(), "Połączenie nawiązane!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(ProfileActivity.this, NoInternetActivity.class);
                        startActivity(i);
                        return;
                    }
                }
            });
            s.show();
        }


    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
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

    @Subscribe
    public void onAsync(LicenseGetEvent e) {
        String auth = e.getAuth();
        AimProfilesManager.admin_account.setAuthToken(auth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActuallySettings.save(getApplicationContext());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        android.support.v4.app.Fragment f = null;
        int id = item.getItemId();
        String title = "";
        if (id == R.id.nav_license) {
            f = lic;
            title = "Licencja";
        } else if (id == R.id.nav_profil) {

            f = prof;
            title = "Profil";

        } else if (id == R.id.nav_system) {
            f = sys;
            title = "System";
        } else if (id == R.id.nav_ts) {
            f = tf;
            title = "TeamSpeak3";
        }
        if (f != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.coordlay, f);
            ft.commit();
        }
        getSupportActionBar().setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (AimProfilesManager.admin_account == null || !AimProfilesManager.admin_account.loaded)
            return false;
        return super.onMenuOpened(featureId, menu);
    }

    private View view;

    public View getView() {
        return view;
    }

}
