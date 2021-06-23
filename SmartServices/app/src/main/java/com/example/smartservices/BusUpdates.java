package com.example.smartservices;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BusUpdates extends AppCompatActivity {
RecyclerView recyclerView;
List<BusUpdatesdb> arrayList;
BusUpdatesdb busUpdatesdb;
BusUpdatesAdapter adapter;
FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (InitApplication.getInstance().isNightModeEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#121212"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#FAFAFA"));
            }
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_bus_updates);
        arrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.rvupdates);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<>();
        db.collection("busupdates").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                arrayList.removeAll(arrayList);
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    busUpdatesdb=documentSnapshot.toObject(BusUpdatesdb.class);
                    String text=busUpdatesdb.getText();
                    String date=busUpdatesdb.getDate();
                    arrayList.add(new BusUpdatesdb(text,date));
                }
                adapter=new BusUpdatesAdapter(BusUpdates.this,arrayList);
                recyclerView.setAdapter(adapter);
            }
        });
        //findViewById(R.id.bg).setBackgroundColor(Color.parseColor("#E0E5EC"));
    }
}