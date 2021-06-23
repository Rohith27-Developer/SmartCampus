package com.example.smartservices;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.vstechlab.easyfonts.EasyFonts;
import com.yalantis.pulltomakesoup.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AAH_FabulousFragment.Callbacks {
    ListView list;
    TextView tv1, tv2, menu;
    String[] stringArray;
    List<Book> lstBook;
    TextView tv3;
    ImageView im1,im2,im3;
    CanteenAdapter adapter;
    CanteenAdapter1 adapter1;
    Button btn;
    FloatingActionButton fab;
    TextView tv;
    RecyclerView myrv;
    BottomSheetFragment dialogFrag;
    ImageView imageButton;
    long mLastClickTime = 0;
    private SwipeRefreshLayout refreshLayout;
    String[] stringArray1;
    private SlidrInterface slidr;
    DatabaseHelper1 mydb;
    int imgList[] = {R.drawable.din2};
    ImageView imageView;
    ArrayList<String> al;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notebookref = db.collection("lunch");
    CollectionReference notebookref1 = db.collection("popular");
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      getSupportActionBar().hide();
        if (InitApplication.getInstance().isNightModeEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#121212"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#1e3d59"));
            }

        }
        setContentView(R.layout.activity_main);

        im1=findViewById(R.id.img1);
        im2=findViewById(R.id.img2);
        im3=findViewById(R.id.img3);
        im2.setVisibility(View.INVISIBLE);
        im3.setVisibility(View.INVISIBLE);
        tv1=findViewById(R.id.textView2);
        tv2=findViewById(R.id.textView3);
        tv3=findViewById(R.id.textView);
        tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD_ITALIC);
        tv1.setTextColor(getResources().getColor(R.color.sel));
        tv2.setTextColor(getResources().getColor(R.color.nonsel));
        tv3.setTextColor(getResources().getColor(R.color.nonsel));
        im1.setVisibility(View.VISIBLE);
        im2.setVisibility(View.INVISIBLE);
        im3.setVisibility(View.INVISIBLE);
        tv1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD_ITALIC);
                tv1.setTextColor(getResources().getColor(R.color.sel));
                tv2.setTextColor(getResources().getColor(R.color.nonsel));
                tv3.setTextColor(getResources().getColor(R.color.nonsel));
                im1.setVisibility(View.VISIBLE);
                im2.setVisibility(View.INVISIBLE);
                im3.setVisibility(View.INVISIBLE);

                notebookref = db.collection("lunch");
                Query query = notebookref.orderBy("name", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<CanteenCDB> options = new FirestoreRecyclerOptions.Builder<CanteenCDB>()
                        .setQuery(query,CanteenCDB.class)
                        .build();
                adapter = new CanteenAdapter(options,MainActivity.this,"lunch");
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.scheduleLayoutAnimation();
                recyclerView.smoothScrollToPosition(0);
                recyclerView.setAdapter(adapter);
                adapter.startListening();

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                tv2.setTypeface(tv2.getTypeface(), Typeface.BOLD_ITALIC);
                tv2.setTextColor(getResources().getColor(R.color.sel));
                tv1.setTextColor(getResources().getColor(R.color.nonsel));
                tv3.setTextColor(getResources().getColor(R.color.nonsel));


                im2.setVisibility(View.VISIBLE);
                im1.setVisibility(View.INVISIBLE);
                im3.setVisibility(View.INVISIBLE);

                notebookref = db.collection("drinks");
                Query query = notebookref.orderBy("name", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<CanteenCDB> options = new FirestoreRecyclerOptions.Builder<CanteenCDB>()
                        .setQuery(query,CanteenCDB.class)
                        .build();
                adapter = new CanteenAdapter(options,MainActivity.this,"drinks");
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.scheduleLayoutAnimation();
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                tv3.setTypeface(tv3.getTypeface(), Typeface.BOLD_ITALIC);
                tv3.setTextColor(getResources().getColor(R.color.sel));
                tv2.setTextColor(getResources().getColor(R.color.nonsel));
                tv1.setTextColor(getResources().getColor(R.color.nonsel));

                im3.setVisibility(View.VISIBLE);
                im2.setVisibility(View.INVISIBLE);
                im1.setVisibility(View.INVISIBLE);
                notebookref = db.collection("tiffin");
                Query query = notebookref.orderBy("name", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<CanteenCDB> options = new FirestoreRecyclerOptions.Builder<CanteenCDB>()
                        .setQuery(query,CanteenCDB.class)
                        .build();
                adapter = new CanteenAdapter(options,MainActivity.this,"tiffin");
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.scheduleLayoutAnimation();
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
        Query query = notebookref.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CanteenCDB> options = new FirestoreRecyclerOptions.Builder<CanteenCDB>()
                .setQuery(query,CanteenCDB.class)
                .build();
        adapter = new CanteenAdapter(options,this,"lunch");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setAdapter(adapter);
        Query query1 = notebookref1.orderBy("rating", Query.Direction.DESCENDING).limit(5);
        FirestoreRecyclerOptions<CanteenCDB> options1 = new FirestoreRecyclerOptions.Builder<CanteenCDB>()
                .setQuery(query1, CanteenCDB.class)
                .build();
        adapter1 = new CanteenAdapter1(options1,this);
        RecyclerView recyclerView1 = findViewById(R.id.side_recycler_view);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView1.scheduleLayoutAnimation();
        recyclerView1.setAdapter(adapter1);

        al = new ArrayList<>();
        String name=getIntent().getStringExtra("name");
        fab = (FloatingActionButton) findViewById(R.id.fab);

        dialogFrag = BottomSheetFragment.newInstance();
        dialogFrag.setParentFab(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });


    }

    @Override
    public void onResult(Object result) {

    }
    @Override
    protected void onStart()

    {


        super.onStart();
        adapter.startListening();
        adapter1.startListening();

    }
    @Override
    protected void onStop() {


        super.onStop();
        adapter.stopListening();
        adapter1.stopListening();

    }
}


