package com.example.smartservices;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;

public class LibraryQR extends AppCompatActivity {
    private static final String LOGTAG = "Scan your Qrcode";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    ImageView qrcodescan;
    LinearLayout linearLayout,linearLayout1;
    RecyclerView recyclerView;
    ArrayList<String> al;
    LinearLayout proceed;
    private SlidrInterface slidr;
    ArrayList<String> bl;
    TextView mybooks;
    Libdb libdb;
    LibAdapter arrayAdapter;
    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        slidr= Slidr.attach(this);
        setContentView(R.layout.activity_library_q_r);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        qrcodescan = findViewById(R.id.qrcodescan);
        libdb = new Libdb(this);
        Cursor c = libdb.getAllData();
        proceed = findViewById(R.id.proceed);
        bl = new ArrayList<>();
        linearLayout = findViewById(R.id.linear1);
        linearLayout1=findViewById(R.id.ll1);
        recyclerView = findViewById(R.id.librecycler);
        mybooks = findViewById(R.id.mybooks);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        al = new ArrayList<>();
        while (c.moveToNext()) {

            al.add(c.getString(0));
        }
        if (al.size() > 0) {
            linearLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            mybooks.setVisibility(View.VISIBLE);
            arrayAdapter = new LibAdapter(LibraryQR.this, al, mybooks, recyclerView, linearLayout, libdb);
            recyclerView.setAdapter(arrayAdapter);
        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(al.size()>0))
                {
                    return;
                }
                else
                {
                    Intent intent=new Intent(LibraryQR.this,LibProceed.class);
                    startActivity(intent);
                }
            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LibraryQR.this,LibraryBag.class);
                startActivity(intent);
            }
        });
        qrcodescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent i = new Intent(LibraryQR.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(LibraryQR.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            mybooks.setVisibility(View.VISIBLE);
            if(al.contains(result))
            {
                Toast.makeText(this, "Same books can't be added", Toast.LENGTH_SHORT).show();
                return;
            }
            al.add(result);
            arrayAdapter = new LibAdapter(LibraryQR.this, al, mybooks, recyclerView, linearLayout, libdb);
            recyclerView.setAdapter(arrayAdapter);
            libdb.insertData(al.get(al.size() - 1));
            recyclerView.smoothScrollToPosition(0);
            Toast.makeText(this, "" + al.size(), Toast.LENGTH_SHORT).show();

        }
    }
}