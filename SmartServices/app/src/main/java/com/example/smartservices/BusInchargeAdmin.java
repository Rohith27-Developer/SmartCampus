package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BusInchargeAdmin extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    LinearLayout logout;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getSupportActionBar().hide();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#1E3D59"));
        setContentView(R.layout.activity_bus_incharge_admin);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        ImageView imageView=findViewById(R.id.dpbusi);
        logout=findViewById(R.id.logoutincharge);
        TextView bustext=findViewById(R.id.bustext);
        ImageView busicon=findViewById(R.id.busicon);
        TextView nottext=findViewById(R.id.nottext);
        ImageView noticon=findViewById(R.id.noticon);
        TextView logouttext=findViewById(R.id.logouttext);
        ImageView logouticon=findViewById(R.id.logouticon);
        LinearLayout track=findViewById(R.id.trackall);
        LinearLayout not=findViewById(R.id.notifybus);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                track.setBackground(getDrawable(R.drawable.busin_colorbg));
                logout.setBackground(getDrawable(R.drawable.regstroke));
                not.setBackground(getDrawable(R.drawable.regstroke));
                busicon.setColorFilter(Color.parseColor("#FFFFFF"));
                bustext.setTextColor(Color.parseColor("#FFFFFF"));
                noticon.setColorFilter(Color.parseColor("#1e3d59"));
                nottext.setTextColor(Color.parseColor("#1e3d59"));
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(
                        BusInchargeAdmin.this,R.style.BottomSheetDialogTheme1
                );
                View bottom= LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottomsheet_trackbus,(LinearLayout)findViewById(R.id.bottomsheetcontainer));
                EditText ed=bottom.findViewById(R.id.rno);
                bottom.findViewById(R.id.trackbs).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(BusInchargeAdmin.this, ""+ed.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),TrackActivity.class);
                        i.putExtra("rno",ed.getText().toString());
                        startActivity(i);
                    }
                });
                bottomSheetDialog.setContentView(bottom);
                bottomSheetDialog.show();
            }
        });
        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                not.setBackground(getDrawable(R.drawable.busin_colorbg));
                logout.setBackground(getDrawable(R.drawable.regstroke));
                track.setBackground(getDrawable(R.drawable.regstroke));
                busicon.setColorFilter(Color.parseColor("#1e3d59"));
                bustext.setTextColor(Color.parseColor("#1e3d59"));
                noticon.setColorFilter(Color.parseColor("#FFFFFF"));
                nottext.setTextColor(Color.parseColor("#FFFFFF"));
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(
                        BusInchargeAdmin.this,R.style.BottomSheetDialogTheme1
                );
                View bottom= LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottomsheet_notifications,(LinearLayout)findViewById(R.id.bottomsheetcontainer));
                EditText ed=bottom.findViewById(R.id.notifyusers);
                bottom.findViewById(R.id.notifybtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String> hm=new HashMap<>();
                        hm.put("text",ed.getText().toString());
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String currentdate = df.format(c);
                        hm.put("date",currentdate);
                        db.collection("busupdates").add(hm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(BusInchargeAdmin.this, "Posted Successfully!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                bottomSheetDialog.setContentView(bottom);
                bottomSheetDialog.show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackground(getDrawable(R.drawable.busin_colorbg));
                track.setBackground(getDrawable(R.drawable.regstroke));
                not.setBackground(getDrawable(R.drawable.regstroke));
                logouticon.setColorFilter(Color.parseColor("#FFFFFF"));
                logouttext.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        TextView textView=findViewById(R.id.namebusi);
        textView.setText(acct.getDisplayName()+"!");
        Glide.with(this).load(acct.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(imageView);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(BusInchargeAdmin.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BusInchargeAdmin.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), GoogleSignin.class);
                                intent.putExtra("finish", true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
    }
}