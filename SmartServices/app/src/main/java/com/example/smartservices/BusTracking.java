package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BusTracking extends AppCompatActivity {
CardView updates,timings,track,contact;
FirebaseFirestore db=FirebaseFirestore.getInstance();
String url;
ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_bus_tracking);
        constraintLayout=findViewById(R.id.bid);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            constraintLayout.setBackgroundResource(R.drawable.bugbg4);
        } else {
            constraintLayout.setBackgroundResource(R.drawable.busbg3);
        }
        updates=findViewById(R.id.updates);
        timings=findViewById(R.id.timings);
        track=findViewById(R.id.track);
        contact=findViewById(R.id.contactdriver);
        db.collection("bus_routes").document("pdf").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                url=documentSnapshot.get("bus").toString();
            }
        });
        timings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BusTracking.this,ViewPdf.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fileurl",url);
                intent.putExtra("filename","CMR BUS");
                startActivity(intent);
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BusTracking.this,BusContacts.class);
                startActivity(intent);
            }
        });
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(
                        BusTracking.this,R.style.BottomSheetDialogTheme1
                );
                View bottom= LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottomsheet_trackbus,(LinearLayout)findViewById(R.id.bottomsheetcontainer));
                bottom.findViewById(R.id.trackbs).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText ed=bottom.findViewById(R.id.rno);
                        Intent i=new Intent(getApplicationContext(),TrackActivity.class);
                        i.putExtra("rno",ed.getText().toString());
                        startActivity(i);
                    }
                });
                bottomSheetDialog.setContentView(bottom);
                bottomSheetDialog.show();
            }
        });
        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BusTracking.this,BusUpdates.class);
                startActivity(intent);
            }
        });
    }
}