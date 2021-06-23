package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {
TextView rating1,persons,itemname;
RatingBar ratingBar;
FirebaseFirestore db=FirebaseFirestore.getInstance();
String count;
ReviewsCDB reviewsCDB;
ReviewsAdapter adapter;
ArrayList<ReviewsCDB> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reviews);
        String rating=getIntent().getStringExtra("rating");
        String name=getIntent().getStringExtra("name");
        TextView itemname_reviews=findViewById(R.id.itemname_reviews);
        itemname_reviews.setText(name);
        ratingBar=findViewById(R.id.ratingBar3);
        rating1=findViewById(R.id.ratingtv);
        RecyclerView recyclerView=findViewById(R.id.recyclerView_reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewsActivity.this));
        ImageView backreviews=findViewById(R.id.backreviews);
        backreviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rating1.setText(rating);
        ratingBar.setRating(Float.parseFloat(rating));
        persons=findViewById(R.id.persons);
        db.collection("popular").whereEqualTo("name",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                {
                    db.collection("popular").document(queryDocumentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot=task.getResult();
                            count=documentSnapshot.get("count").toString();
                            ValueAnimator valueAnimator = ValueAnimator.ofInt(0,Integer.parseInt(count));
                            valueAnimator.setDuration(2000);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    persons.setText(valueAnimator.getAnimatedValue().toString());
                                }
                            });
                            valueAnimator.start();
                        }
                    });
                }

            }
        });
        db.collection("reviews").document("1").collection(name).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                    return;
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    reviewsCDB=queryDocumentSnapshot.toObject(ReviewsCDB.class);
                    arrayList.add(new ReviewsCDB(reviewsCDB.getEmail(),reviewsCDB.getReview(),reviewsCDB.getTime(),reviewsCDB.getName()));
                }
                adapter=new ReviewsAdapter(ReviewsActivity.this,arrayList);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}