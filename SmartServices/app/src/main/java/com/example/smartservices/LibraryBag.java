package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LibraryBag extends AppCompatActivity {
FirebaseFirestore db=FirebaseFirestore.getInstance();
GoogleSignInClient mGoogleSignInClient;
ListView lv1;
LibBagAdapter libBagAdapter;
    List<String> list1,list;
    String imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        setContentView(R.layout.activity_library_bag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv1=findViewById(R.id.lv1);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        list=new ArrayList<>();
        list1=new ArrayList<>();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        ImageView imageView= findViewById(R.id.avatar1);
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imei = android_id;
        }
        Glide.with(this).load(acct.getPhotoUrl()).into(imageView);
        db.collection("library").document(imei).collection(acct.getEmail()).document("books").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
               list = (List<String>) document.get("books");
                try {
                    if (list.size() == 0) {
                        //Toast.makeText(LibraryBag.this, "The size is empty", Toast.LENGTH_SHORT).show();
                        list.add("No Books Available");
                        list1.add(" ");
                    }
                    else
                    {
                        libBagAdapter=new LibBagAdapter(LibraryBag.this,list,list1);
                        lv1.setAdapter(libBagAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(LibraryBag.this, R.anim.list_layout_controller);
                        lv1.setLayoutAnimation(controller);
                    }
                }catch(Exception e)
                {
                    Toast.makeText(LibraryBag.this, "No books", Toast.LENGTH_SHORT).show();
                }


            }
        });
        db.collection("library").document(imei).collection(acct.getEmail()).document("date").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                list1 = (List<String>) document.get("date");
                try {
                    if (list1.size() == 0) {
                        //Toast.makeText(LibraryBag.this, "The size is empty", Toast.LENGTH_SHORT).show();
                        list1.add(" ");
                        list.add("No Books available");
                    }
                    else
                    {
                        libBagAdapter=new LibBagAdapter(LibraryBag.this,list,list1);
                        lv1.setAdapter(libBagAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(LibraryBag.this, R.anim.list_layout_controller);
                        lv1.setLayoutAnimation(controller);
                    }
                }catch(Exception e)
                {
                 //   list1.add(" ");
                }

            }
        });

    }
}