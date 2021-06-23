package com.example.smartservices;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tayfuncesur.stepper.Stepper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacementAdminFrag2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacementAdminFrag2 extends Fragment {
EditText textInputEditText;
Button button;
    public static String s2="desc";
    public static String s1="title";
    public static String s3="link";
    ProgressDialog progressDialog;
    int max=0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Stepper stepper;
    public PlacementAdminFrag2(Stepper stepper) {
        this.stepper=stepper;
        // Required empty public constructor
    }
    public PlacementAdminFrag2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlacementAdminFrag2.
     */
    // TODO: Rename and change types and number of parameters
    public static PlacementAdminFrag2 newInstance(String param1, String param2) {
        PlacementAdminFrag2 fragment = new PlacementAdminFrag2();
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
        View view=inflater.inflate(R.layout.fragment_placement_admin_frag2, container, false);
        textInputEditText=view.findViewById(R.id.editText2company);
        button=view.findViewById(R.id.save_plbtn);
        Bundle bundle = this.getArguments();
        String title=bundle.getString("title");
        String link=bundle.getString("link");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.isEmpty() ) {
                    Toast.makeText(getContext(), "Empty Fields!.....", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    progressDialog.setCanceledOnTouchOutside(false);
                    String desc=textInputEditText.getText().toString().trim();
                    final Map<String, Object> u = new HashMap<>();
                    u.put(s1,title);
                    u.put(s2, desc);
                    u.put(s3,link);
                    db.collection("placements").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                    db.collection("placements").document(String.valueOf(max + 1)).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }catch (Exception e)
                                {
                                    db.collection("placements").document("1").set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
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