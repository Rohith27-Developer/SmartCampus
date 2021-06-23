package com.example.smartservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Fragment_intro3 extends Fragment {
    DatabaseHelperIntro databaseHelperIntro;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_intro3,container,false);
        TextView next=view.findViewById(R.id.next_acct);
        databaseHelperIntro=new DatabaseHelperIntro(getContext());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelperIntro.insertData("completed");
                Intent intent=new Intent(getContext(),Account.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
