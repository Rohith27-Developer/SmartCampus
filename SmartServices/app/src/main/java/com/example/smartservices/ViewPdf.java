package com.example.smartservices;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URLEncoder;

public class ViewPdf extends AppCompatActivity {
WebView pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_pdf);
        pdf=findViewById(R.id.viewPdf);
        pdf.getSettings().setJavaScriptEnabled(true);
        String fileurl=getIntent().getStringExtra("fileurl");
        String filename=getIntent().getStringExtra("filename");
        final ProgressDialog pd=new ProgressDialog(this);
       pd.setTitle(filename);
        pd.setMessage("Opening....!!!");


        pdf.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String url="";
        try {
            url= URLEncoder.encode(fileurl,"UTF-8");
        }catch (Exception ex)
        {}

        pdf.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);

    }


}