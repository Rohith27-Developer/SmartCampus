package com.example.smartservices;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Auth Error. Too many attempts. Try again after 30 seconds\n\nIf the fingerprint sensor isn't working,disable it in your phone's settings", false);


    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Fingerprint not Recognized. Try again ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        this.update("You can now access the app.", true);
        context.startActivity(new Intent(context, GoogleSignin.class));
        ((AppCompatActivity) context).finish();


    }


    private void update(String s, boolean b) {
        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.fingerprintImage);
        paraLabel.setText(s);

        if(b == false){
            Thread myThread=new Thread(){
                @Override
                public void run()
                {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            myThread.start();
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.lightred));
            imageView.setImageResource(R.drawable.fingerprinterror);

        } else {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.lightgreen));
            imageView.setImageResource(R.drawable.fingerprintaccept);

        }

    }

}
