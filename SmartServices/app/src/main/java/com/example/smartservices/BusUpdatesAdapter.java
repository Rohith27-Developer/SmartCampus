package com.example.smartservices;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusUpdatesAdapter extends RecyclerView.Adapter<BusUpdatesAdapter.ViewHolder> {
    private List<BusUpdatesdb> Models;
    private Context context;
    private static final int REQUEST_CALL = 1;
    public BusUpdatesAdapter(Context context,List<BusUpdatesdb> Models) {
        this.Models = Models;
        this.context = context;
    }

    @NonNull
    @Override
    public BusUpdatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.busupdatesadapter,parent,false);
        return new BusUpdatesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusUpdatesAdapter.ViewHolder holder, int position) {
        BusUpdatesdb Model=Models.get(position);
        holder.textView2.setText(Model.getText());
        holder.textView1.setText(Model.getDate());
        //  holder.img.setImageDrawable(context.getResources().getDrawable(Model.getImage()));
    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView textView1,textView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=(TextView)itemView.findViewById(R.id.datetv);
            textView2=(TextView)itemView.findViewById(R.id.update1);
        }
    }
}