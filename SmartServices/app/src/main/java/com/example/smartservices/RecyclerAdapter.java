package com.example.smartservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Ordersdb> mData ;


    public RecyclerAdapter(Context mContext, List<Ordersdb> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.orderrvui,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.orderno.setText("ORDER NUMBER : "+mData.get(position).getOrderno());
        holder.itemnametext.setText(mData.get(position).getItems()+" X "+mData.get(position).getQuantity());

       Glide.with(mContext).load(mData.get(position).getImage()).into(holder.itemimg);





    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemnametext,orderno;
        ImageView itemimg;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemnametext = (TextView) itemView.findViewById(R.id.itemnametext) ;
            itemimg = (ImageView) itemView.findViewById(R.id.itemimg);
            orderno=itemView.findViewById(R.id.orderno);


        }
    }


}