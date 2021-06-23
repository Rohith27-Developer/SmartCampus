package com.example.smartservices;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Events_admin_frag1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Events_admin_frag1 extends Fragment {
    public static String s1="title";

    public static String s3="link";
    int max=0;
    Calendar myCalendar;
    TextInputEditText et,date1;
    EditText et2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    StepperIndicator stepperIndicator;
    public Events_admin_frag1(StepperIndicator stepperIndicator) {
        // Required empty public constructor
        this.stepperIndicator=stepperIndicator;
    }
    public Events_admin_frag1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Events_admin_frag1.
     */
    // TODO: Rename and change types and number of parameters
    public static Events_admin_frag1 newInstance(String param1, String param2) {
        Events_admin_frag1 fragment = new Events_admin_frag1();
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
        View view=inflater.inflate(R.layout.fragment_events_admin_frag1, container, false);
        et=view.findViewById(R.id.editTexttitle);
        et2=view.findViewById(R.id.editTextlink);
        date1=view.findViewById(R.id.date_ev_admin);
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (date1.getRight() - date1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                    }
                }
                return false;
            }
        });
        view.findViewById(R.id.nextevbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = et.getText().toString().trim();
                String link = et2.getText().toString().trim();

                if (title.isEmpty() ) {
                    Toast.makeText(getContext(), "Empty Fields!.....", Toast.LENGTH_SHORT).show();
                } else {
                    Fragment fragment = new Events_admin_frag2(stepperIndicator);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",title);
                    bundle.putString("link",link);
                    bundle.putString("date",date1.getText().toString());
                    fragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.container2,fragment).commit();
                    stepperIndicator.setCurrentStep(stepperIndicator.getCurrentStep()+1);
                }
            }
        });
        return view;
    }
    private void updateLabel() {
        String myFormat = "dd/MMM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date1.setText(sdf.format(myCalendar.getTime()));
    }
}