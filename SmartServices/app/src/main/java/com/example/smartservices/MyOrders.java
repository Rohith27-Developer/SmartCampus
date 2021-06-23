package com.example.smartservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.HelperUtils;
import com.kekstudio.dachshundtablayout.indicators.DachshundIndicator;
import com.kekstudio.dachshundtablayout.indicators.LineFadeIndicator;
import com.kekstudio.dachshundtablayout.indicators.LineMoveIndicator;
import com.kekstudio.dachshundtablayout.indicators.PointFadeIndicator;
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrders extends AppCompatActivity {
Ordersdb user;
FirebaseFirestore db=FirebaseFirestore.getInstance();
List<String> list=new ArrayList<>();
    ArrayAdapter<String> adapter1;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    ImageView back;
    ListView lv;

    public static Context mContext;
    public static Activity mActivity;

    private static float[][] input_val;
    private static float[][] outputValue;
    private static Interpreter tfliteInterpreter;
    private static HashMap<String, Float> word_to_idx_dict;
    private SlidrInterface slidr;
    private static final String DOG_BREEDS[] = {"CURRENT ORDERS","PREVIOUS ORDERS"};

    private ViewPager viewPager;
    private DachshundTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        if (InitApplication.getInstance().isNightModeEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#1e3d59"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#1e3d59"));
            }

        }
        setContentView(R.layout.activity_orders);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        input_val = new float[1][500];
        outputValue = new float[1][1];
        loadModel();
        mContext=this;
        mActivity=MyOrders.this;
        // 2. Loading Vocab
        loadVocab();
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        String em=getIntent().getStringExtra("email");
       /* lv=findViewById(R.id.list1);

        db.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable final QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data, data1;
                list.removeAll(list);
                for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                    data = "";
                    data1 = "";
                    user = documentSnapshot.toObject(Ordersdb.class);
                    String item = user.getItems();
                    String quantity = user.getQuantity();
                    String email=user.getEmail();
                    String id=documentSnapshot.getId();
                    if(email!=null && email.equalsIgnoreCase(em))
                    {
                        list.add(item+" "+"X"+" "+quantity+"\n"+"Order Number: "+id);
                    }
                }
               /* if(list.size()==0)
                {
                    Intent intent=new Intent(MyOrders.this,EmptyOrders.class);
                    startActivity(intent);
                    finish();
                }*/
           /*     adapter1 = new ArrayAdapter(MyOrders.this, android.R.layout.simple_list_item_1, list);
               lv.setAdapter(adapter1);
                LayoutAnimationController controller= AnimationUtils.loadLayoutAnimation(MyOrders.this,R.anim.list_layout_controller);
                lv.setLayoutAnimation(controller);
            }
        });*/
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i)
            {
                case 0:
                    return new BlankFragment();

                case 1:
                    return new BlankFragment2();

            }
           return new BlankFragment();
        }

        @Override
        public int getCount() {
            return DOG_BREEDS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return DOG_BREEDS[position];
        }
    }

    public static class PageFragment extends Fragment {

        public PageFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_blank, container, false);
        }
    }
    public static class PageFragment1 extends Fragment {

        public PageFragment1() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_blank2, container, false);
        }
    }
    private void loadModel() {

        try{
            tfliteInterpreter = new Interpreter(loadModelFile());

        } catch(Exception ex){
            ex.printStackTrace();

        }

    }

    public static float processReview(String cleaned_review){
        input_val = new float[1][500];
        outputValue = new float[1][1];

        String words[] = cleaned_review.split("\\s+");

        int j=input_val[0].length-1;
        for(int i=0; i<words.length && j>=0; i++){

            if(word_to_idx_dict.get(words[i].toLowerCase())!=null){
                input_val[0][j] = word_to_idx_dict.get(words[i].toLowerCase());
                j--;
            }
        }

        String res= "";

        for(int i=0; i<input_val[0].length; i++){
            res+=input_val[0][i]+", ";
        }


        int left = -1;
        int right = input_val[0].length-1;
        for(int i=0; i<=right; i++){
            if(input_val[0][i]!=0){
                left = i;
                break;
            }
        }

        while(left<=right){
            float temp = input_val[0][left];
            input_val[0][left] = input_val[0][right];
            input_val[0][right] = temp;
            left++;
            right--;
        }

        res= "";
        for(int i=0; i<input_val[0].length; i++){
            res+=input_val[0][i]+", ";
        }



        return runModel(input_val,outputValue);
    }
    private static float runModel(float[][] input_val, float[][] outputValue) {
        tfliteInterpreter.run(input_val, outputValue);
        float output[] = MyOrders.outputValue[0];
        Float res = output[0];
        return res;

    }
    private MappedByteBuffer loadModelFile() throws IOException {
        // Open the model using an input stream, and memory map it to load
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("rnn_model_food_review_analysis.tflite");
        FileInputStream inputStream = new FileInputStream((fileDescriptor.getFileDescriptor()));
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void loadVocab() {

        AssetManager assetManager = getAssets();
        InputStream input;
        try {
            input = assetManager.open("food_review_vocab_word_to_idx_dict.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();




            // byte buffer into a string
            String text = new String(buffer);


            String sub_str = text.substring(1,text.length()-1);
            String[] strs = sub_str.split(",");
            word_to_idx_dict = new HashMap<String, Float>();

            for(int i=0; i<strs.length;i++){
                String one_map = strs[i];
                int key_st = one_map.indexOf("\"");
                int key_end = one_map.indexOf(":")-1;
                String key = one_map.substring(key_st+1,key_end);

                int val_st = key_end+2;
                float value = Float.parseFloat(one_map.substring(val_st));
                word_to_idx_dict.put(key,value);
                //System.out.println(strs[i]);
            }


            //Log.d("xlr8_map_tst",String.valueOf(word_to_idx_dict.get("movie")));


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
}

