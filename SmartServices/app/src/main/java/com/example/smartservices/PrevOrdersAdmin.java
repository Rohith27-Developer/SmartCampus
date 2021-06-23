package com.example.smartservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PrevOrdersAdmin extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ArrayList<Ordersdb> list=new ArrayList<>();
    Ordersdb ordersdb;
    PrevOrdersAdminAdapter prevOrdersAdminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_prev_orders_admin);
        RecyclerView recyclerView=findViewById(R.id.prev_orders_admin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db.collection("prevorders").orderBy("orderno").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable final QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                list.removeAll(list);
                for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                    ordersdb = documentSnapshot.toObject(Ordersdb.class);
                    String item = ordersdb.getItems();
                    String quantity = ordersdb.getQuantity();
                    String email=ordersdb.getEmail();
                    String image=ordersdb.getImage();
                    String orderno=ordersdb.getOrderno();
                    String cat=ordersdb.getCategory();
                    String datetime=ordersdb.getDatetime();
                    String id=documentSnapshot.getId();
                    list.add(new Ordersdb(item,quantity,image,email,orderno,cat,datetime));
                }
                if(list.size()==0)
                {
                   list.add(new Ordersdb("No Data Available","","","",""));
                }
                prevOrdersAdminAdapter=new PrevOrdersAdminAdapter(PrevOrdersAdmin.this,list);
                recyclerView.setAdapter(prevOrdersAdminAdapter);

            }
        });
    }
}