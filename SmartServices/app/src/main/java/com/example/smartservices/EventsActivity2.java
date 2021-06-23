package com.example.smartservices;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventsActivity2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tv1,tv2,tv3,tv4,tvt;
    ImageView imageView,event_delete;
    String link;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_event1);
        //getSupportActionBar().hide();
        // getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity3.this, R.color.colorPrimary));
        int[] img=new int[]{R.drawable.ebg1,R.drawable.ebg2,R.drawable.ebg3,R.drawable.ebg4,R.drawable.ebg5,R.drawable.ebg6,R.drawable.ebg7,R.drawable.ebg8,R.drawable.ebg9};
        Random r=new Random();
        int n=r.nextInt(9);
        imageView=findViewById(R.id.imageView);
        imageView.setImageResource(img[n]);
        tvt=findViewById(R.id.tit1);
        tv1=findViewById(R.id.textviewdesevents);
        tv2=findViewById(R.id.textviewlinevents);
        tv3=findViewById(R.id.date);
        tv4=findViewById(R.id.mon);
        event_delete=findViewById(R.id.delete_event1);
        String role=getIntent().getStringExtra("role");
        String title= getIntent().getStringExtra("title");
        String desc=getIntent().getStringExtra("desc");
        String day=getIntent().getStringExtra("day");
        String month=getIntent().getStringExtra("month");
        TextView deschead=findViewById(R.id.deschead);
        constraintLayout=findViewById(R.id.constraintevents);
        link=getIntent().getStringExtra("link");
        tvt.setText(title);
        tv1.setText(desc);
        tv2.setText(link);
        tv3.setText(day);
        tv4.setText(month);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(link));
                    startActivity(i);
                }
                catch (Exception e){
                    Toast.makeText(EventsActivity2.this, "This is Not a proper link", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if(role.equals("admin"))
        {
            deschead.setTextColor(Color.parseColor("#000000"));
            constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            tv1.setTextColor(Color.parseColor("#000000"));
            tv2.setTextColor(Color.parseColor("#1e3d59"));
            event_delete.setVisibility(View.VISIBLE);
            event_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(EventsActivity2.this).create();
                    alertDialog.setTitle("Are you Sure");
                    alertDialog.setMessage("The event will be deleted..");
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                  db.collection("events_admin").whereEqualTo("title",title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                          if(task.isSuccessful()) {
                                              for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                  db.collection("events_admin").document(documentSnapshot.getId()).delete();

                                              }
                                              Toast.makeText(EventsActivity2.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                  });

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });

        }
        else
        {
            event_delete.setVisibility(View.INVISIBLE);
        }

    }
}