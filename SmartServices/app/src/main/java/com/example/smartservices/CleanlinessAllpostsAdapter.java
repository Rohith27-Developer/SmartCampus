package com.example.smartservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.ArrayList;

public class CleanlinessAllpostsAdapter extends RecyclerView.Adapter<CleanlinessAllpostsAdapter.ViewHolder> {

    Context context;
    ArrayList<CleanAdminGetter> list;
    public CleanlinessAllpostsAdapter(Context context, ArrayList<CleanAdminGetter> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public CleanlinessAllpostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewallposts,parent,false);
        return new CleanlinessAllpostsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CleanlinessAllpostsAdapter.ViewHolder holder, int position) {

        String text = list.get(position).getDustplace();
        String text1 = list.get(position).getRollno();
        holder.textView.setText(text);
        String url=list.get(position).getUrl();
        holder.textView1.setText(text1);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PopUp.class);
                intent.putExtra("Image", url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView,textView1;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.placetv);
            textView1=itemView.findViewById(R.id.rnotv);
            cardView=itemView.findViewById(R.id.cardposts);
        }
    }
}
