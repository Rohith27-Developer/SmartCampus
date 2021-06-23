package com.example.smartservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CleanlinessPostsAdapter extends RecyclerView.Adapter<CleanlinessPostsAdapter.ViewHolder> {

    Context context;
    ArrayList<CleanAdminGetter> list;
    public CleanlinessPostsAdapter(Context context, ArrayList<CleanAdminGetter> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public CleanlinessPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpostscleanliness,parent,false);
        return new CleanlinessPostsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CleanlinessPostsAdapter.ViewHolder holder, int position) {

        String text = list.get(position).getDustplace();
        String text1 = list.get(position).getRollno();
        String url=list.get(position).getUrl();
        holder.textView.setText(text);
        holder.crno.setText(text1);
        Glide.with(context).load(url).into(holder.kenBurnsView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView,crno;
        KenBurnsView kenBurnsView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.ctext);
            crno=itemView.findViewById(R.id.crno);
            kenBurnsView=itemView.findViewById(R.id.cimage);

        }
    }
}
