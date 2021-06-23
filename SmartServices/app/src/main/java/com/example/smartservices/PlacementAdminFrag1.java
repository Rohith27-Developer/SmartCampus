package com.example.smartservices;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.tayfuncesur.stepper.Stepper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacementAdminFrag1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacementAdminFrag1 extends Fragment {
TextInputEditText textInputEditText,textInputEditText1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
Button button;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Stepper stepper;
    public PlacementAdminFrag1(Stepper stepper) {
        this.stepper=stepper;
        // Required empty public constructor
    }
    public PlacementAdminFrag1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlacementAdminFrag1.
     */
    // TODO: Rename and change types and number of parameters
    public static PlacementAdminFrag1 newInstance(String param1, String param2) {
        PlacementAdminFrag1 fragment = new PlacementAdminFrag1();
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
        View view=inflater.inflate(R.layout.fragment_placement_admin_frag1, container, false);
        textInputEditText=view.findViewById(R.id.placetext);
        textInputEditText1=view.findViewById(R.id.editTextplacelink);
        button=view.findViewById(R.id.nextplacebtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textInputEditText.getText().toString().isEmpty() && textInputEditText1.getText().toString().isEmpty())
                {
                    return;
                }

                stepper.forward();
                Fragment fragment = new PlacementAdminFrag2(stepper);
                Bundle bundle = new Bundle();
                bundle.putString("title",textInputEditText.getText().toString());
                bundle.putString("link",textInputEditText1.getText().toString());
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container2,fragment).commit();
            }
        });
        return view;
    }
}