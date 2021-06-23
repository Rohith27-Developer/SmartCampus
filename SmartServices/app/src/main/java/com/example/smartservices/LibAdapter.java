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

import java.util.ArrayList;

public class LibAdapter extends RecyclerView.Adapter<LibAdapter.ViewHolder> {

    Context context;
    ArrayList<String> list;
    TextView mybooks;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    Libdb libdb;
    public LibAdapter(Context context, ArrayList<String> list, TextView mybooks, RecyclerView recyclerView, LinearLayout linearLayout, Libdb libdb) {
        this.context = context;
        this.list = list;
        this.mybooks=mybooks;
        this.recyclerView=recyclerView;
        this.linearLayout=linearLayout;
        this.libdb=libdb;
    }

    @NonNull
    @Override
    public LibAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libadapter,parent,false);
        return new LibAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibAdapter.ViewHolder holder, int position) {

        String model = list.get(position);
        holder.textView.setText(model);
        switch (position%6) {
            case 0:
                holder.imageclassadapter.setImageResource(R.drawable.asset_bg_paleblue);
                holder.framebg.setBackgroundResource(R.drawable.gradient_color_1);
                break;
            case 1:
                holder.imageclassadapter.setImageResource(R.drawable.asset_bg_green);
                holder.framebg.setBackgroundResource(R.drawable.gradient_color_2);
                break;
            case 2:
                holder.imageclassadapter.setImageResource(R.drawable.asset_bg_yellow);
                holder.framebg.setBackgroundResource(R.drawable.gradient_color_3);
                break;
            case 3:
                holder.imageclassadapter.setImageResource(R.drawable.asset_bg_palegreen);
                holder.framebg.setBackgroundResource(R.drawable.gradient_color_4);
                break;
            case 4:
                holder.imageclassadapter.setImageResource(R.drawable.asset_bg_paleorange);
                holder.framebg.setBackgroundResource(R.drawable.gradient_color_5);
                break;
            case 5:
                holder.imageclassadapter.setImageResource(R.drawable.asset_bg_white);
                holder.framebg.setBackgroundResource(R.drawable.gradient_color_6);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView delete,imageclassadapter;
        RelativeLayout framebg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.bookid);
            delete=itemView.findViewById(R.id.delete);
            imageclassadapter=itemView.findViewById(R.id.imageClass_adapter);
            framebg=itemView.findViewById(R.id.frame_bg);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    libdb.deleteData(list.get(getAdapterPosition()));
                    list.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    if(list.size()==0)
                    {
                        mybooks.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}