package pl.eldzi.aimpanel.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.eldzi.aimpanel.ActuallySettings;
import pl.eldzi.aimpanel.AimProfilesManager;
import pl.eldzi.aimpanel.R;
import pl.eldzi.aimpanel.profile.AimLicense;
import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.LicenseGetEvent;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link LicenseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LicenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LicenseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textview;
    private OnFragmentInteractionListener mListener;

    public LicenseFragment() {
        Events.getListenerHandler().register(this);
    }

    public static LicenseFragment newInstance(String param1, String param2) {
        LicenseFragment fragment = new LicenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Subscribe
    public void onAsync(LicenseGetEvent e) {
        JSONObject o = (JSONObject) e.getAuth();
        AimProfilesManager.admin_account.getAimLicense().set(o);
        license = AimProfilesManager.admin_account.getAimLicense();
        if (license == null) return;
        if (view == null) System.exit(0);
        if (textview == null) textview = (TextView) view.findViewById(R.id.textView6);
        if (license.isValid()) {
            String name = license.getName();
            String note = license.getNotes();
            Date to = license.getTo();
            String nl = System.getProperty("line.separator");
            SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            textview.setHint("Nazwa Licencji:    " + name + "" + nl + nl + "Informacje:    " + note + "" + nl + nl + "Data wygaśnięcia licencji:    " + format.format(to));
            textview.setText("Nazwa Licencji:    " + name + "" + nl + nl + "Informacje:    " + note + "" + nl + nl + "Data wygaśnięcia licencji:    " + format.format(to));

        } else {
            textview.setTextColor(Color.RED);
            textview.setText("Twoja licencja się skończyła");
        }
    }

    AimLicense license;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_license, container, false);
        textview = (TextView) view.findViewById(R.id.textView6);
        AimProfile profile = ActuallySettings.adminProfile;
        profile.getAimLicense().send();
        //TODO: NUMER 696800350
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
