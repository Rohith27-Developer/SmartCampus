package com.example.smartservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BottomSheetPrevOrders extends BottomSheetDialogFragment {
    private String foodCommentSelected;
    public String orderno, items;
    DecimalFormat df = new DecimalFormat("#.#");
    private ProgressDialog comment_progress;
    public EditText review;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private Float posScoreFloat, negScoreFloat;
    double rate=0.0;
    int max = 0;
    List<Integer> list = new ArrayList<>();
    private int posSelected;
    String datetime;
    String cat;
    GoogleSignInAccount acct;
    GoogleSignInClient mGoogleSignInClient;
    public BottomSheetPrevOrders(String orderno, String items,int position,String datetime,String cat) {
        this.orderno=orderno;
        this.items=items;
        this.datetime=datetime;
        posSelected=position;
        this.cat=cat;
    }

    public BottomSheetPrevOrders() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_prevorders, container, false);
        comment_progress = new ProgressDialog(getContext());
        review = v.findViewById(R.id.review_write);
        TextView or=v.findViewById(R.id.ordernoprev);
        TextView itemnameprev=v.findViewById(R.id.itemnameprev);
        or.setText(orderno);
        itemnameprev.setText(items);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        acct = GoogleSignIn.getLastSignedInAccount(getContext());
        comment_progress.setTitle("Running ML Model");
        comment_progress.setMessage("Please wait while we analyze your feedback !");
        comment_progress.setCanceledOnTouchOutside(false);
        comment_progress.setCancelable(false);
        TextView save_prevbtn=v.findViewById(R.id.save_prevbtn);
        df.setRoundingMode(RoundingMode.CEILING);
        save_prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comment_progress.show();
                if (!review.getText().toString().trim().equals("")) {

                    try {
                        String cleaned_review = review.getText().toString().trim().replaceAll("[^\\w\\s]", "");
                        float pos_score = MyOrders.processReview(cleaned_review);
                        Toast.makeText(getContext(), "" + pos_score, Toast.LENGTH_SHORT).show();
                        runMLSentimentOnComment(pos_score);
                        foodCommentSelected = review.getText().toString().trim();
                    }catch(Exception e)
                    {
                        Toast.makeText(getContext(), "Please enter valid text", Toast.LENGTH_SHORT).show();
                        comment_progress.dismiss();
                    }

                }
                else {
                    Toast.makeText(getContext(), "Review can't be emmpty!!", Toast.LENGTH_SHORT).show();
                    comment_progress.dismiss();
                }
            }
        });

        return v;
    }
    private void runMLSentimentOnComment(float pos_score) {

        posScoreFloat = pos_score;
        negScoreFloat = 1 - posScoreFloat;
        if(posScoreFloat>=0.80)
        {
            rate=(posScoreFloat*100*4.5)/90;
        }
        else if(posScoreFloat>=0.60)
        {
            rate=(posScoreFloat*100*3.5)/70;
        }
        else if(posScoreFloat>=0.60)
        {
            rate=(posScoreFloat*100*2.5)/50;
        }
        else if(posScoreFloat>=0.40)
        {
            rate=(posScoreFloat*100*1.5)/30;
        }
        else {
            rate=(posScoreFloat*100*0.5)/10;
        }
        db.collection("popular").whereEqualTo("name",items).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        db.collection("popular").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot1=task.getResult();
                                double sum=Double.parseDouble(documentSnapshot1.get("sum").toString());
                                int count=Integer.parseInt(documentSnapshot1.get("count").toString());
                                count=count+1;
                                sum=sum+(rate);
                                sum=Double.parseDouble(df.format(sum));
                                double rating=(double) (sum/count);
                                rating=Double.parseDouble(df.format(rating));
                                db.collection("prevorders").whereEqualTo("datetime",datetime).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                QuerySnapshot queryDocumentSnapshots=task.getResult();
                                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                                                {
                                                    db.collection("prevorders").document(queryDocumentSnapshot.getId()).update("status","Feedback Sent");
                                                }
                                            }
                                    }
                                });
                                double finalRating = rating;
                                db.collection(cat).whereEqualTo("name",items).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                                {
                                                    db.collection(cat).document(queryDocumentSnapshot.getId()).update("rating", finalRating);
                                                }
                                            }
                                    }
                                });
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date date = new Date();
                                HashMap<String,String> hm=new HashMap<>();
                                hm.put("review",foodCommentSelected);
                                hm.put("email",acct.getEmail());
                                hm.put("time",formatter.format(date));
                                hm.put("name",acct.getDisplayName());
                                db.collection("reviews").document("1").collection(items).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                list.add(Integer.parseInt(documentSnapshot.getId()));
                                            }
                                            try {
                                                max = Collections.max(list);
                                            } catch (Exception e) {
                                                max = 0;
                                            }
                                            max=max+1;
                                            db.collection("reviews").document("1").collection(items).document(String.valueOf(max)).set(hm);
                                            dismiss();
                                        }
                                    }
                                });

                                db.collection("popular").document(documentSnapshot1.getId()).update("rating",rating);
                                db.collection("popular").document(documentSnapshot1.getId()).update("sum",String.valueOf(sum));
                                db.collection("popular").document(documentSnapshot1.getId()).update("count",String.valueOf(count));
                            }
                        });
                    }
                }
            }
        });

        comment_progress.dismiss();
    }
    }
