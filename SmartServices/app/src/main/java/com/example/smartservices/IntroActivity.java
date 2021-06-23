package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {
    private static final int NUM=3;
    private ViewPager viewPager;
    DatabaseHelperIntro databaseHelperIntro;
    private ScreenSlidePagerAdapter screenSlidePagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intro);
        databaseHelperIntro=new DatabaseHelperIntro(IntroActivity.this);
        Cursor cursor=databaseHelperIntro.getAllData();
        if(cursor.getCount()!=0)
        {
         if(cursor.moveToNext())
         {
            if(cursor.getString(0).equalsIgnoreCase("Completed"))
            {
            Intent intent=new Intent(IntroActivity.this,Account.class);
            startActivity(intent);
            finish();
            }
         }
        }
        viewPager=findViewById(R.id.pager);
        screenSlidePagerAdapter=new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(screenSlidePagerAdapter);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
    {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new Fragment_intro1();
                case 1:
                    return new Fragment_intro2();
                case  2:
                    return new Fragment_intro3();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM;
        }
    }
}