package com.example.smartservices;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LibDoneActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    ArrayList<String> al;
    ArrayList<String> dl;
    Libdb libdb;
    TextView rno,date;
    String imei;
    LoadingButton btn;
    GoogleSignInClient googleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
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
                window.setStatusBarColor(Color.parseColor("#1e3d59"));
            }

        }
        setContentView(R.layout.activity_lib_done);
        listView = findViewById(R.id.books);
        arrayList = new ArrayList<>();
        al = new ArrayList<>();
        dl=new ArrayList<>();
        Date currentTime = Calendar.getInstance().getTime();
        date=findViewById(R.id.datelib);
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imei = android_id;
        }
        String currentDate = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.getDefault()).format(new Date());
        date.setText(""+currentDate);
        rno = findViewById(R.id.rno);
        btn = findViewById(R.id.confirm);

        rno.setText("Roll no :  " + getIntent().getStringExtra("res"));
        libdb = new Libdb(this);
        Cursor c = libdb.getAllData();
        while (c.moveToNext()) {
            arrayList.add("\u25BA   " + c.getString(0));
            al.add(c.getString(0));
            dl.add(date.getText().toString());
        }
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, arrayList);
        listView.setAdapter(arrayAdapter);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        btn.reset();
        btn.setBackgroundShader(new LinearGradient(0f,0f,1000f,100f, 0xAA1E3D59, 0xAA1E3D59, Shader.TileMode.CLAMP));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.startLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        db.collection("library").document(imei).collection(acct.getEmail()).document("books").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                HashMap<String, List<String>> hm = new HashMap<>();
                                hm.put("books", al);
                                HashMap<String, List<String>> hm1 = new HashMap<>();
                                hm1.put("date", dl);
                                DocumentSnapshot documentSnapshot = task.getResult();
                                try {
                                    String str = documentSnapshot.get(documentSnapshot.getId()).toString();
                                    str = str.replaceAll("\\[", "").replaceAll("\\]", "");
                                    String[] s1 = str.split(",");

                                    if (s1.length>=4)
                                    {
                                        Toast.makeText(LibDoneActivity.this, "Maximum 4 books allowed per person", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else {
                                        for (int i = 0; i < al.size(); i++){
                                            db.collection("library").document(imei).collection(acct.getEmail()).document("books").update("books", FieldValue.arrayUnion(al.get(i)));
                                            db.collection("library").document(getIntent().getStringExtra("res")).collection(acct.getEmail()).document("date").update("date", FieldValue.arrayUnion(date.getText().toString()));
                                            libdb.deleteData(al.get(i));
                                        }
                                        Toast.makeText(LibDoneActivity.this, "Issued Successfully....", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(LibDoneActivity.this,DashBoard.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                } catch (Exception e) {
                                    db.collection("library").document(imei).collection(acct.getEmail()).document("books").set(hm);
                                    db.collection("library").document(imei).collection(acct.getEmail()).document("date").set(hm1);
                                    Toast.makeText(LibDoneActivity.this, "Issued Successfully....", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LibDoneActivity.this,DashBoard.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }

                            }
                        });
                        btn.cancelLoading();
                        libdb.drop();


                    }
                },2000);
                //start loading

            }
        });
    }


}