package pl.eldzi.aimpanel;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class InfoActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:slimebox123@gmail.com"));
                startActivity(browserIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_info, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            int a = getArguments().getInt(ARG_SECTION_NUMBER);
            if (a == 1) {
                ImageView v = (ImageView) rootView.findViewById(R.id.imageView3);
                v.setVisibility(View.VISIBLE);
                ImageView va = (ImageView) rootView.findViewById(R.id.aimpanel);
                va.setVisibility(View.INVISIBLE);
                textView.setText("Nazywam się Wiktor Dylewski i jestem 16 letnim programistą JAVA. Programowaniem zajmuję się od ok. 4lat. Zaczynałem od prostych modyfikacji do gier, a skończyłem na zaawansowanych programach, aplikacjach okienkowych oraz aplikacjach na telefony z sstemem android. Programowanie kręciło mnie od zawsze. Odczuwałem wewnętrzną chęć manipulacji czymś, wylaniem czegoś z siebie. Poznając grafikę, stwierdziłem że jednak to nie dla mnie, przy okazji pojawiła się JAVA, dzięki której mogę wyrazić siebie poprzez kod. Przez lata współpracowałem z wieloma stronami, tworzyłem także wiele projektów, bibliotek dla różnych instytucji. Część z nich znajduję się na moim githubie: (http://github.com/ElDzi).");
            } else {
                ImageView v = (ImageView) rootView.findViewById(R.id.imageView3);
                v.setVisibility(View.INVISIBLE);
                ImageView va = (ImageView) rootView.findViewById(R.id.aimpanel);
                va.setVisibility(View.VISIBLE);

                textView.setText("Aplikacja AimDroid jest to aplikacja stworzona dla użytkowników aplikacji AimPanel.PRO . Została ona stworzona w celu ułatwienia pracy z serwerami. AimDroid nie jest w 100% ukończoną aplikacją i czeka mnie wiele pisania, aczkolwiek posiada on już dość rozbudowany system zarządzania aplikacją aimpanel.");
                Crouton.makeText(getActivity(), "PAMIĘTAJ! Aplikacja jest w wersji BETA. Nie jest ona jeszcze wykonano w 100%!", Style.ALERT).show();
            }
            return rootView;
        }
    }

    public static InfoActivity a;

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "O Mnie";
                case 1:
                    return "O Aplikacji";
            }
            return null;
        }
    }
}
