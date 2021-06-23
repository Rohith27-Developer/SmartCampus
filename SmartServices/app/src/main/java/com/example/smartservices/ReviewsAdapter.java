package com.example.smartservices;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private Context mContext ;
    private List<ReviewsCDB> mData ;


    public ReviewsAdapter(Context mContext, List<ReviewsCDB> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.reviews_adapter,parent,false);
        return new ReviewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.MyViewHolder holder, final int position) {

        holder.review_read.setText(mData.get(position).getReview());
        holder.name.setText(mData.get(position).getName());
        String firstLetter = String.valueOf(holder.name.getText().toString().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(position);



        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color);
        holder.imageView.setImageDrawable(drawable);
        holder.datetimetv.setText(mData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView review_read,datetimetv,name;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            review_read=itemView.findViewById(R.id.review_read);
            name=itemView.findViewById(R.id.name);
            datetimetv=itemView.findViewById(R.id.datetimetv);
            imageView=itemView.findViewById(R.id.review_profilepic);

        }
    }


}
