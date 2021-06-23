package com.example.smartservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ViewItemsAdminAdapter extends RecyclerView.Adapter<ViewItemsAdminAdapter.MyViewHolder> {

    private Context mContext ;
    private List<CanteenCDB> mData ;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String str;
    public ViewItemsAdminAdapter(Context mContext, List<CanteenCDB> mData,String str) {
        this.mContext = mContext;
        this.mData = mData;
        this.str=str;
    }

    @Override
    public ViewItemsAdminAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.viewitemsadmincard,parent,false);
        return new ViewItemsAdminAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewItemsAdminAdapter.MyViewHolder holder, final int position) {

        holder.itemname.setText(mData.get(position).getName());

        holder.ratingBar.setRating(Float.parseFloat(mData.get(position).getRating().toString()));
        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.imageView);
        holder.status.setText(mData.get(position).getStatus());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mContext,ReviewsActivity.class);
                intent.putExtra("rating",mData.get(position).getRating().toString());
                intent.putExtra("name",mData.get(position).getName());
                mContext.startActivity(intent);
            }
        });
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mData.get(position).getStatus().equalsIgnoreCase("Not Available")) {
                        holder.status.setText("Available");
                        db.collection(str).whereEqualTo("name", mData.get(position).getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    db.collection(str).document(queryDocumentSnapshot.getId()).update("status", "Available");
                                }
                                db.collection("popular").whereEqualTo("name", mData.get(position).getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            db.collection("popular").document(queryDocumentSnapshot.getId()).update("status", "Available");
                                        }
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        holder.status.setText("Not Available");
                        db.collection(str).whereEqualTo("name", mData.get(position).getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    db.collection(str).document(queryDocumentSnapshot.getId()).update("status", "Not Available");
                                }
                                db.collection("popular").whereEqualTo("name", mData.get(position).getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            db.collection("popular").document(queryDocumentSnapshot.getId()).update("status", "Not Available");
                                        }
                                    }
                                });
                            }
                        });
                    }
                    return false;
                }
            });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemname,status;
        ImageView imageView;
        RatingBar ratingBar;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemname=itemView.findViewById(R.id.item_name_admin);
            imageView=itemView.findViewById(R.id.item_image_admin);
            ratingBar=itemView.findViewById(R.id.ratingBar2);
            cardView=itemView.findViewById(R.id.item_card_view_admin);
            status=itemView.findViewById(R.id.status_admin);

        }
    }


}


