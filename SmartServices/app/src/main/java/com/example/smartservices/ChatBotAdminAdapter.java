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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChatBotAdminAdapter extends RecyclerView.Adapter<ChatBotAdminAdapter.MyViewHolder> {

    private Context mContext;
    private List<ChatBotCDB> mData;
    private static final int REQUEST_CALL = 1;

    public ChatBotAdminAdapter(Context mContext, List<ChatBotCDB> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ChatBotAdminAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.chatbotadmin, parent, false);
        return new ChatBotAdminAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatBotAdminAdapter.MyViewHolder holder, final int position) {

        holder.chatbotadminname.setText(mData.get(position).getName());
        holder.chatbotdesc.setText(mData.get(position).getIssue());
        String firstLetter = String.valueOf(mData.get(position).getName().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(position);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color);
        holder.itemimg.setImageDrawable(drawable);
        holder.call_chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.image_click));
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String p = "tel:" + mData.get(position).getMobno();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(p));
                    mContext.startActivity(intent);
                    Animatoo.animateZoom(mContext);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView chatbotadminname,chatbotdesc;
        ImageView itemimg,call_chatbot;

        public MyViewHolder(View itemView) {
            super(itemView);

            chatbotadminname = (TextView) itemView.findViewById(R.id.chatbotadminname) ;
            itemimg = (ImageView) itemView.findViewById(R.id.profileimage1);
            chatbotdesc=itemView.findViewById(R.id.chatbotdesc);
            call_chatbot=itemView.findViewById(R.id.call_chatbot);

        }
    }


}
