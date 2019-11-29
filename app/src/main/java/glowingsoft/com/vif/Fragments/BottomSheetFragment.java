package glowingsoft.com.vif.Fragments;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import glowingsoft.com.vif.Adapters.BottomSheetAdapter;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;


public class BottomSheetFragment extends BottomSheetDialogFragment {
    List<LeftMenuModel> leftMenuModels;
    ListView listView;
    BottomSheetAdapter bottomSheetAdapter;

    public void setdata(List<LeftMenuModel> leftMenuModels) {
        this.leftMenuModels = leftMenuModels;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        listView = view.findViewById(R.id.listView);
        bottomSheetAdapter = new BottomSheetAdapter(getActivity(), leftMenuModels);
        listView.setAdapter(bottomSheetAdapter);
        return view;
    }


}
