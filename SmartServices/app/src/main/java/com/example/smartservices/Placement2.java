package com.example.smartservices;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class Placement2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tv1,tv2;
    String link;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Menu collapsedMenu;
    private boolean appBarExpanded = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement2);
//        getSupportActionBar().hide();
        tv1=findViewById(R.id.textviewdes);
        tv2=findViewById(R.id.textviewlin);
        NestedScrollView coordinatorLayout=findViewById(R.id.coordinatorlayoutplace);
        TextView textView=findViewById(R.id.sampledescpl);
        String title= getIntent().getStringExtra("title");
        String desc=getIntent().getStringExtra("desc");
        String role=getIntent().getStringExtra("role");
        link=getIntent().getStringExtra("link");
        tv1.setText(desc);
        tv2.setText(link);
        if(role.equals("admin"))
        {
            tv1.setTextColor(Color.parseColor("#393E46"));
            tv2.setTextColor(Color.parseColor("#1e3d59"));
            coordinatorLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            textView.setTextColor(Color.parseColor("#1e3d59"));
        }
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(link));
                    startActivity(i);
                }
                catch (Exception e){
                    Toast.makeText(Placement2.this, "This is Not a proper link", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final Toolbar toolbar = findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeButtonEnabled(true);

        appBarLayout = findViewById(R.id.appbar2);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar2);

        //  collapsingToolbar.setCollapsedTitleTextAppearance(R.font.shade);
        collapsingToolbar.setTitle(title);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

    }
}