package pl.eldzi.aimpanel.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import pl.eldzi.aimpanel.ActuallySettings;
import pl.eldzi.aimpanel.AimProfilesManager;
import pl.eldzi.aimpanel.R;
import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.profile.AimSystem;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.SystemLoadEvent;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link LicenseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LicenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SystemFragment extends Fragment {
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

    public SystemFragment() {
        Events.getListenerHandler().register(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LicenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LicenseFragment newInstance(String param1, String param2) {
        LicenseFragment fragment = new LicenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void onEvent(SystemLoadEvent e) {
        JSONObject o = (JSONObject) e.getResult();
        AimProfilesManager.admin_account.getSystem().setObj(o);
        AimProfile profile = ActuallySettings.adminProfile;
        AimSystem license = profile.getSystem();
        String nl = System.getProperty("line.separator");
        if (license == null) return;
        if (textview == null) return;
        textview.setText(license.getResult().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_system, container, false);

        textview = (TextView) view.findViewById(R.id.textView6);
        AimProfile profile = ActuallySettings.adminProfile;
        AimSystem license = profile.getSystem();
        String nl = System.getProperty("line.separator");
        textview.setText(license.getResult().toString());


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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
