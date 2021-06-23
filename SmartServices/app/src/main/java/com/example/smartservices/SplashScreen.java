package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
Animation top,bottom,middle;
View first,second,third,fourth,fifth,sixth;
TextView slogan,a;


private static int SPLASH_TIME=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        top= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        middle= AnimationUtils.loadAnimation(this,R.anim.middle_animation);
        bottom= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        a=findViewById(R.id.smart);
        slogan=findViewById(R.id.slogan);
        first=findViewById(R.id.first_line);
        second=findViewById(R.id.second_line);
        third=findViewById(R.id.third_line);
        fourth=findViewById(R.id.fourth_line);

        fifth=findViewById(R.id.fifth_line);
        sixth=findViewById(R.id.sixth_line);
        first.setAnimation(top);
        second.setAnimation(top);
        third.setAnimation(top);
        fourth.setAnimation(top);
        fifth.setAnimation(top);
        sixth.setAnimation(top);
        a.setAnimation(middle);
        slogan.setAnimation(bottom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,FingerPrint.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

}