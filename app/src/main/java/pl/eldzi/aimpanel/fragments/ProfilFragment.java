package pl.eldzi.aimpanel.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import pl.eldzi.aimpanel.ActuallySettings;
import pl.eldzi.aimpanel.AimProfilesManager;
import pl.eldzi.aimpanel.R;
import pl.eldzi.aimpanel.profile.AimProfile;
import pl.eldzi.aimpanel.profile.AimUser;
import pl.eldzi.aimpanel.utils.DateUtils;
import pl.eldzi.aimpanel.utils.Events;
import pl.eldzi.aimpanel.utils.events.GetUserEvent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfilFragment() {
        Events.getListenerHandler().register(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
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

    TextView t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        t = (TextView) v.findViewById(R.id.textprof);
        String nl = System.getProperty("line.separator");
        AimProfile profile = ActuallySettings.adminProfile;
        AimUser m = profile.getMy();
        if (m != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            t.setText("Twoja nazwa:     " + m.getUsername());
            t.setText(nl + t.getText() + "Utworzono:     " + format.format(m.getCreated_at()));
            t.setText(nl + t.getText() + "Twój adres URL:     " + profile.getUrl());
        } else {
            t.setTextColor(Color.RED);
            t.setText("Ładowanie...");
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Subscribe
    public void onAsync(GetUserEvent e) {
        try {
            JSONObject o = (JSONObject) e.getResult();
            AimProfile profile = AimProfilesManager.admin_account;
            AimUser dd = new AimUser(o.getString("username"), DateUtils.parse(o.getString("created_at")));
            String nl = System.getProperty("line.separator");
            AimProfilesManager.admin_account.setLastUser(dd);
            SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            t.setText("Twoja nazwa:      " + profile.getUser() + nl + "Utworzono:       " + format.format(dd.getCreated_at()) + nl + "Twój adres URL:       " + profile.getUrl() + nl);
        } catch (JSONException ea) {
            ea.printStackTrace();
        }

    }
}
