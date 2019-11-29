package glowingsoft.com.vif.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import glowingsoft.com.vif.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoveFilesFragment extends Fragment {


    public MoveFilesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_move_files, container, false);
    }

}
