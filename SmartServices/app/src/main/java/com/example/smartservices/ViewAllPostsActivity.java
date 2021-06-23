package com.example.smartservices;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewAllPostsActivity extends AppCompatActivity {
RecyclerView recyclerView;
    ArrayList<CleanAdminGetter> al=new ArrayList<>();
    CleanAdminGetter cleanAdminGetter;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    CleanlinessAllpostsAdapter cleanlinessAllpostsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#121212"));
            } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_all_posts);
        recyclerView=findViewById(R.id.viewpostsrv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db.collection("cleanliness").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    cleanAdminGetter=queryDocumentSnapshot.toObject(CleanAdminGetter.class);
                    String url=cleanAdminGetter.getUrl();
                    String place=cleanAdminGetter.getDustplace();
                    String rno=cleanAdminGetter.getRollno();
                    CleanAdminGetter cleanAdminGetter=new CleanAdminGetter(place,url,rno);
                    al.add(cleanAdminGetter);
                }
                cleanlinessAllpostsAdapter=new CleanlinessAllpostsAdapter(ViewAllPostsActivity.this,al);
                recyclerView.setAdapter(cleanlinessAllpostsAdapter);
            }
        });
    }
}