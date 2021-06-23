package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class BusDriverActivity extends AppCompatActivity {
GoogleSignInClient mGoogleSignInClient;
LinearLayout logout;
LinearLayout share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getSupportActionBar().hide();
        setContentView(R.layout.activity_bus_driver);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        ImageView imageView=findViewById(R.id.dpbus);
        TextView textView=findViewById(R.id.namebus);
        TextInputEditText busno1=findViewById(R.id.busno1);
        TextInputEditText busphone=findViewById(R.id.busphone);
        logout=findViewById(R.id.logoutbus);
        share=findViewById(R.id.shareloc);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(BusDriverActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BusDriverActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), GoogleSignin.class);
                                intent.putExtra("finish", true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BusDriverActivity.this,DriverMapActivity.class);
                intent.putExtra("busno",busno1.getText().toString());
                startActivity(intent);
            }
        });
        textView.setText(acct.getDisplayName()+"!");
        Glide.with(this).load(acct.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}