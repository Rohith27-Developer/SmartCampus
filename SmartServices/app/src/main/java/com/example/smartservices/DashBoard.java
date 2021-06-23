package com.example.smartservices;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.smartservices.activities.NotesActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class DashBoard extends AppCompatActivity {
    CardView lostandfound,clean,events,placements,bustrack,library;
    LinearLayout canteen,calcgpa,myprofile,chatbot,notescard;
    long mLastClickTime = 0;
    GoogleSignInClient googleSignInClient;
    TextView textView;
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
        setContentView(R.layout.activity_dash_board);
       canteen=findViewById(R.id.canteen);
        lostandfound=findViewById(R.id.lostandfound);
        bustrack=findViewById(R.id.bustrack);
        calcgpa=findViewById(R.id.calcgpa);
        library=findViewById(R.id.librarycard);
        textView=findViewById(R.id.nameacct);
        notescard=findViewById(R.id.notescard);
        ImageView img=findViewById(R.id.userpicdash);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        Glide.with(this).load(acct.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(img);
        textView.setText(acct.getDisplayName());
        clean=findViewById(R.id.clean);
        myprofile=findViewById(R.id.myprofile);
        events=findViewById(R.id.events);
        chatbot=findViewById(R.id.bot_card);
        placements=findViewById(R.id.placement_card);


        bustrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,BusTracking.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,LibraryQR.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        notescard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this, NotesActivity.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        canteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,MainActivity.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateZoom(DashBoard.this);
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,EventActivity1.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        calcgpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,InsideGpa.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,Cleanliness.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        lostandfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,LostAndFound.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,MyProfile.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        placements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,Placement1.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent intent=new Intent(DashBoard.this,ChatBot.class);
                intent.putExtra("email",acct.getEmail());
                intent.putExtra("name",acct.getDisplayName());
                intent.putExtra("image", acct.getPhotoUrl());
                intent.putExtra("id", acct.getId());
                startActivity(intent);
                Animatoo.animateSlideUp(DashBoard.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(1);
    }
}
