package com.example.smartservices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment {
    public static String s1="title";
    public static String s2="desc";
    public static String s3="link";
    int max=0;
    TextInputEditText et;
    EditText et1,et2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        StepperIndicator indicator1 = view.findViewById(R.id.stepper_indicator1);
        int i=indicator1.getCurrentStep();
        indicator1.setCurrentStep(i);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container2,new Events_admin_frag1(indicator1)).commit();

      //  ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
     //   pager.setAdapter(new PagerAdapter());
        return view;
    }


}
