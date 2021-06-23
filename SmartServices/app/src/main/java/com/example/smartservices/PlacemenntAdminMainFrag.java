package com.example.smartservices;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tayfuncesur.stepper.Stepper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacemenntAdminMainFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacemenntAdminMainFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlacemenntAdminMainFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlacemenntAdminMainFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static PlacemenntAdminMainFrag newInstance(String param1, String param2) {
        PlacemenntAdminMainFrag fragment = new PlacemenntAdminMainFrag();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_placemennt_admin_main, container, false);
        Stepper indicator = view.findViewById(R.id.stepper_indicator);
        indicator.setStepCount(2);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container2,new PlacementAdminFrag1(indicator)).commit();
        return view;
    }
}