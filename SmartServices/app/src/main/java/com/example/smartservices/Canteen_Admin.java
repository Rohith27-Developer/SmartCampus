package com.example.smartservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;

import java.util.ArrayList;

public class Canteen_Admin extends AppCompatActivity {
LinearLayout myorders;
    final Fragment fragment1 = new ViewItems();
    final Fragment fragment2 = new BlankFragment3();
    final Fragment fragment3=new AddItems();
    final Fragment fragment4=new CanteenAdminProfile();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_canteen__admin);
        fm.beginTransaction().add(R.id.container4, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.container4, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container4, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container4,fragment1, "1").commit();
        final FlareBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.home_icon),"Home","#1e3d59"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.orders_icon),"Orders","#1e3d59"));
      //  tabs.add(new Flaretab(getResources().getDrawable(R.drawable.popular_icon),"Popular","#1e3d59"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.add_icon),"Add","#1e3d59"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.profile_icon),"Profile","#1e3d59"));
        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(Canteen_Admin.this);

        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                switch (selectedIndex)
                {
                    case 0:
                        Window window3 = getWindow();
                        window3.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window3.setStatusBarColor(Color.parseColor("#1e3d59"));
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        break;
                    case 1:
                        Window window2 = getWindow();
                        window2.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window2.setStatusBarColor(Color.parseColor("#1e3d59"));
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        break;
                    case 2:
                        Window window1 = getWindow();
                        window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window1.setStatusBarColor(Color.parseColor("#1e3d59"));
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        break;
                    case 3:
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#293859"));
                        fm.beginTransaction().hide(active).show(fragment4).commit();
                        active = fragment4;
                        break;

                }
            }
        });
    }
}