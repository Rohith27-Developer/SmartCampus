package com.example.smartservices;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Events_admin_frag2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Events_admin_frag2 extends Fragment {
    public static String s2="desc";
    public static String s1="title";
    public static String s3="link";
    public static String s4="day";
    public static String s5="month";
    int max=0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText et1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    StepperIndicator stepperIndicator;
    public Events_admin_frag2(StepperIndicator stepperIndicator) {
        // Required empty public constructor
        this.stepperIndicator=stepperIndicator;
    }
    public Events_admin_frag2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Events_admin_frag2.
     */
    // TODO: Rename and change types and number of parameters
    public static Events_admin_frag2 newInstance(String param1, String param2) {
        Events_admin_frag2 fragment = new Events_admin_frag2();
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
        View view=inflater.inflate(R.layout.fragment_events_admin_frag2, container, false);
        et1=view.findViewById(R.id.editText2);
        Bundle bundle = this.getArguments();

        view.findViewById(R.id.save_evbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=bundle.getString("title");
                String link=bundle.getString("link");
                String date=bundle.getString("date");
                String str[]=date.split("/");
                String desc=et1.getText().toString().trim();
                if (title.isEmpty() ) {
                    Toast.makeText(getContext(), "Empty Fields!.....", Toast.LENGTH_SHORT).show();
                } else {
                    final Map<String, Object> u = new HashMap<>();
                    title = title.substring(0, 1).toUpperCase() + title.substring(1);
                    u.put(s1,title);
                    u.put(s2, desc);
                    u.put(s3,link);
                    u.put(s4,str[0]);
                    u.put(s5,str[1]);
                    db.collection("events_admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                List<String> list=new ArrayList();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    list.add(documentSnapshot.getId());

                                }
                                try {
                                    max = Integer.parseInt(Collections.max(list));
                                    db.collection("events_admin").document(String.valueOf(max + 1)).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(),"Succcess",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }catch (Exception e)
                                {
                                    db.collection("events_admin").document("1").set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            else
                            {

                            }
                        }
                    });

                }
            }
        });
        return view;
    }
}