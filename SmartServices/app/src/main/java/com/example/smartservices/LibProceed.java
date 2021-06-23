package com.example.smartservices;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LibProceed extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        mScannerView = new ZXingScannerView(LibProceed.this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        Toast.makeText(this, "" + rawResult.getText(), Toast.LENGTH_SHORT).show();
        mScannerView.resumeCameraPreview(this);
        Intent intent=new Intent(LibProceed.this,LibDoneActivity.class);
        intent.putExtra("res",rawResult.getText());
        startActivity(intent);
        finish();
    }
}