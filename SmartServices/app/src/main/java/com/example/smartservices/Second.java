package com.example.smartservices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.github.tonywills.loadingbutton.LoadingButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mursaat.extendedtextview.AnimatedGradientTextView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Second extends AppCompatActivity {
    TextView pl;
    ImageView img;
    int count=1;
    DatabaseHelper1 mydb;
    FrameLayout frameLayout;
    boolean result=false;
    Button btn;
    TextView textCartItemCount;
    private SlidrInterface slidr;

    List<String> list=new ArrayList<>();
    TextView tv1,tv2,tv3;
    ImageView im1,inc,dec;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
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
        setContentView(R.layout.activity_second);

        slidr= Slidr.attach(this);
        ImageView back=findViewById(R.id.back_second);
        TextView calories=findViewById(R.id.calories1);
        TextView textView=findViewById(R.id.text_abt_can);
         final String name=getIntent().getStringExtra("name");
        final String price=getIntent().getStringExtra("price");
        String image1=getIntent().getStringExtra("image");
        String carb=getIntent().getStringExtra("carbs");
        String prots=getIntent().getStringExtra("prots");
        String text=getIntent().getStringExtra("text");
        String fats=getIntent().getStringExtra("fats");
        String rate=getIntent().getStringExtra("rate");
        float cal=Float.parseFloat(getIntent().getStringExtra("calories"));
        String cat=getIntent().getStringExtra("cat");
        textView.setText(text);
        TextView carbs,prot,fat;
        carbs=findViewById(R.id.carbs);
        prot=findViewById(R.id.prots);
        calories.setText(cal+" g");
        fat=findViewById(R.id.fats);
        btn=findViewById(R.id.l3);
        prot.setText(prots+" g");
        fat.setText(fats+" g");
        carbs.setText(carb+" g");
        frameLayout=findViewById(R.id.cart_second);
        textCartItemCount=findViewById(R.id.cart_badge);
        RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);


        tv1=findViewById(R.id.food_name);

        im1=findViewById(R.id.food_img);
        inc=findViewById(R.id.incre);
        dec=findViewById(R.id.decre);
        tv2=findViewById(R.id.pri);

        tv3=findViewById(R.id.value_id);
        tv2.setText(price);
        mydb=new DatabaseHelper1(Second.this);
        int c=mydb.getItems().getCount();
        if(c==0)
        {
            textCartItemCount.setVisibility(View.INVISIBLE);
        }
        else
        {
            textCartItemCount.setVisibility(View.VISIBLE);
            textCartItemCount.setText(""+c);
        }
        ratingbar.setRating(Float.parseFloat(rate));
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Second.this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Second.this,Cart1.class);
                intent.putExtra("email", acct.getEmail());
                startActivity(intent);
            }
        });
        inc.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String present_value_string = tv3.getText().toString();
                int present_value_int = Integer.parseInt(present_value_string);
                if(present_value_int>=10)
                {
                    return;
                }
                present_value_int++;
                tv3.setText(String.valueOf(present_value_int));
                tv2.setText(String.valueOf(Integer.parseInt(price)*present_value_int));
            }
        });
        dec.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String present_value_string = tv3.getText().toString();
                int present_value_int = Integer.parseInt(present_value_string);
                present_value_int--;
                if(present_value_int<=0)
                {
                    return;
                }
                tv3.setText(String.valueOf(present_value_int));
                tv2.setText(String.valueOf(Integer.parseInt(price)*present_value_int));
            }
        });

        tv1.setText(name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Glide.with(this).load(image1).into(im1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=mydb.getAllData();
                count=Integer.parseInt(tv3.getText().toString());
                while (res.moveToNext())
                {
                    list.add(res.getString(0));
                }
                for(int i=0;i<list.size();i++)
                {
                    if(list.get(i).equalsIgnoreCase(name))
                    {
                        Toast.makeText(Second.this, name, Toast.LENGTH_SHORT).show();
                        result=true;
                        break;
                    }
                    else
                    {
                        result=false;
                    }
                }
                if(result==true)
                {

                    mydb.updateData(name,price,String.valueOf(count), String.valueOf(Integer.parseInt(price) * count),String.valueOf(cal));
                    Toast.makeText(Second.this, "Data updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean isInserted = mydb.insertData(name, price, String.valueOf(count), String.valueOf(Integer.parseInt(price) * count),String.valueOf(cal),image1,cat,fats);
                    if (isInserted = true) {
                        Toast.makeText(Second.this, "data inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Second.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
                int c=mydb.getItems().getCount();
                textCartItemCount.setVisibility(View.VISIBLE);
                textCartItemCount.setText(""+c);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mydb=new DatabaseHelper1(Second.this);
        int c=mydb.getItems().getCount();
        if(c==0)
        {
            textCartItemCount.setVisibility(View.INVISIBLE);
        }
        else
        {
            textCartItemCount.setVisibility(View.VISIBLE);
            textCartItemCount.setText(""+c);
        }
    }
}