package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Account extends AppCompatActivity {
    ImageView imageView;
    TextView textName, textEmail, texts;
    GoogleSignInClient mGoogleSignInClient;
    String imageuri, id;
    AccountDb mydb;
    int PERMISSION_ALL = 1;
    BusContactsdb busContactsdb;
    ArrayList<String> al;
    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    List<String> list;
    List<String> list1;
    int count1 = 0;
    long mLastClickTime = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account);
        list = new ArrayList();
        mydb = new AccountDb(this);
        list1 = new ArrayList();

        b = findViewById(R.id.arrow);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            id = personId;
            Uri personPhoto = acct.getPhotoUrl();
            Cursor res = mydb.getRole(acct.getEmail());
            if (res.getCount() != 0) {
                if (res.moveToNext()) {
                    if (res.getString(1).equalsIgnoreCase("canteen_admin")) {
                        Intent intent = new Intent(Account.this, Canteen_Admin.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());
                        startActivity(intent);
                        finish();
                    } else if (res.getString(1).equalsIgnoreCase("clean_admin")) {
                        Intent intent = new Intent(Account.this, Main3Activity.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());
                        startActivity(intent);
                        finish();
                    } else if (res.getString(1).equalsIgnoreCase("events_admin")) {
                        Intent intent = new Intent(Account.this, Events_Admin.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());

                        startActivity(intent);
                        finish();
                    } else if (res.getString(1).equalsIgnoreCase("admin")) {
                        Intent intent = new Intent(Account.this, AdminActivity.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());

                        startActivity(intent);
                        finish();
                    }
                    else if (res.getString(1).equalsIgnoreCase("securityadmin")) {
                        Intent intent = new Intent(Account.this, SecurityAdmin.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());

                        startActivity(intent);
                        finish();
                    }
                    else if (res.getString(1).equalsIgnoreCase("placementsadmin")) {
                        Intent intent = new Intent(Account.this, PlacementsAdmin.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());

                        startActivity(intent);
                        finish();
                    }
                    else if (res.getString(1).equalsIgnoreCase("busdriver")) {
                        Intent intent = new Intent(Account.this, BusDriverActivity.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());

                        startActivity(intent);
                        finish();
                    }
                    else if (res.getString(1).equalsIgnoreCase("busincharge")) {
                        Intent intent = new Intent(Account.this, BusInchargeAdmin.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());

                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(Account.this, DashBoard.class);
                        intent.putExtra("email",acct.getEmail());
                        intent.putExtra("name",acct.getDisplayName());
                        intent.putExtra("imageUri", acct.getPhotoUrl());
                        intent.putExtra("id", acct.getId());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String str = acct.getEmail();
                if(al.contains(acct.getEmail()))
                {
                    mydb.insertData(str,"busdriver");
                    Intent intent = new Intent(Account.this, BusDriverActivity.class);
                    intent.putExtra("email",acct.getEmail());
                    intent.putExtra("name",acct.getDisplayName());
                    intent.putExtra("imageUri", acct.getPhotoUrl());
                    intent.putExtra("id", acct.getId());
                    startActivity(intent);
                    finish();
                    return;
                }

                db.collection("users").document("roles").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.get("canteen_admin").toString().equalsIgnoreCase(str)) {
                                mydb.insertData(str,"canteen_admin");
                                Intent intent = new Intent(Account.this, Canteen_Admin.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            } else if (documentSnapshot.get("clean_admin").toString().equalsIgnoreCase(str)) {
                                mydb.insertData(str, "clean_admin");
                                Intent intent = new Intent(Account.this, Main3Activity.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            } else if (documentSnapshot.get("events_admin").toString().equalsIgnoreCase(str)) {
                                mydb.insertData(str, "events_admin");
                                Intent intent = new Intent(Account.this, Events_Admin.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            } else if (documentSnapshot.get("Main_admin").toString().equalsIgnoreCase(str)) {
                                mydb.insertData(str, "admin");
                                Intent intent = new Intent(Account.this, AdminActivity.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            }
                            else if (documentSnapshot.get("securityadmin").toString().equalsIgnoreCase(str)) {
                                mydb.insertData(str, "securityadmin");
                                Intent intent = new Intent(Account.this, SecurityAdmin.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            }
                            else if(documentSnapshot.get("placementsadmin").toString().equalsIgnoreCase(str))
                            {
                                mydb.insertData(str, "placementsadmin");
                                Intent intent = new Intent(Account.this, PlacementsAdmin.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            }
                            else if(documentSnapshot.get("busincharge").toString().equalsIgnoreCase(str))
                            {
                                mydb.insertData(str, "busincharge");
                                Intent intent = new Intent(Account.this, BusInchargeAdmin.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            }
                            else {
                                mydb.insertData(str, "student");
                                Intent intent = new Intent(Account.this, DashBoard.class);
                                intent.putExtra("email",acct.getEmail());
                                intent.putExtra("name",acct.getDisplayName());
                                intent.putExtra("imageUri", acct.getPhotoUrl());
                                intent.putExtra("id", acct.getId());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });


            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onStart() {
        super.onStart();
        al=new ArrayList<>();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        db.collection("busdrivers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots) {
                        busContactsdb = queryDocumentSnapshot.toObject(BusContactsdb.class);
                        al.add(busContactsdb.getEmail());
                    }
            }
        });
    }

}
