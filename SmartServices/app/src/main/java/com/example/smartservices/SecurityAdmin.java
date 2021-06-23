package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.ArrayList;

public class SecurityAdmin extends AppCompatActivity {
SecurityAdapter securityAdapter;
RecyclerView recyclerView;
Securitycdb securitycdb;
ArrayList<Securitycdb> arrayList;
ImageView menusec;
GoogleSignInClient googleSignInClient;
FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       getSupportActionBar().hide();
        setContentView(R.layout.activity_security_admin);
        recyclerView = findViewById(R.id.secrv);
        arrayList = new ArrayList<>();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        menusec = findViewById(R.id.menu_sec);
        TextView name1 = findViewById(R.id.name1);
        name1.setText("Welcome " + acct.getDisplayName());
        recyclerView.setLayoutManager(new GridLayoutManager(SecurityAdmin.this, 2));
        db.collection("security").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                arrayList.removeAll(arrayList);
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    securitycdb = queryDocumentSnapshot.toObject(Securitycdb.class);
                    String roomno = securitycdb.getRoomno();
                    String status = securitycdb.getStatus();
                    if (roomno.startsWith("1")) {
                        Securitycdb securitycdb = new Securitycdb(roomno, status);
                        arrayList.add(securitycdb);
                    }
                }
                securityAdapter = new SecurityAdapter(SecurityAdmin.this, arrayList, "firstfloor");
                recyclerView.setAdapter(securityAdapter);
            }
        });

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("First Floor", BlankFragment.class)
                .add("Second Floor", BlankFragment.class)
                .add("Third Floor", BlankFragment.class)
                .add("Other Rooms", BlankFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager1);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.view);
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    db.collection("security").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            arrayList.removeAll(arrayList);
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                securitycdb = queryDocumentSnapshot.toObject(Securitycdb.class);
                                String roomno = securitycdb.getRoomno();
                                String status = securitycdb.getStatus();
                                if (roomno.startsWith("1")) {
                                    Securitycdb securitycdb = new Securitycdb(roomno, status);
                                    arrayList.add(securitycdb);
                                }
                            }
                            securityAdapter = new SecurityAdapter(SecurityAdmin.this, arrayList, "firstfloor");
                            recyclerView.setAdapter(securityAdapter);
                        }
                    });

                } else if (position == 1) {
                    db.collection("security").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            arrayList.removeAll(arrayList);
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                securitycdb = queryDocumentSnapshot.toObject(Securitycdb.class);
                                String roomno = securitycdb.getRoomno();
                                String status = securitycdb.getStatus();
                                if (roomno.startsWith("2")) {
                                    Securitycdb securitycdb = new Securitycdb(roomno, status);
                                    arrayList.add(securitycdb);
                                }
                            }
                            securityAdapter = new SecurityAdapter(SecurityAdmin.this, arrayList, "secondfloor");
                            recyclerView.setAdapter(securityAdapter);
                        }
                    });
                } else if (position == 2) {
                    db.collection("security").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            arrayList.removeAll(arrayList);
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                securitycdb = queryDocumentSnapshot.toObject(Securitycdb.class);
                                String roomno = securitycdb.getRoomno();
                                String status = securitycdb.getStatus();
                                if (roomno.startsWith("3")) {
                                    Securitycdb securitycdb = new Securitycdb(roomno, status);
                                    arrayList.add(securitycdb);
                                }
                            }
                            securityAdapter = new SecurityAdapter(SecurityAdmin.this, arrayList, "thirdfloor");
                            recyclerView.setAdapter(securityAdapter);
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerTab.setViewPager(viewPager);
        menusec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout layoutsecurity = findViewById(R.id.layoutsecurity);
                final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutsecurity);

                        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                layoutsecurity.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        googleSignInClient.signOut()
                                .addOnCompleteListener(SecurityAdmin.this,new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(SecurityAdmin.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), GoogleSignin.class);
                                        intent.putExtra("finish", true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                });
                    }
                });

    }


}