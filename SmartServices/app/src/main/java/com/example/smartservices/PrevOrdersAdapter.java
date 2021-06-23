package com.example.smartservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import static java.security.AccessController.getContext;

public class PrevOrdersAdapter extends RecyclerView.Adapter<PrevOrdersAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Ordersdb> mData ;
    private AlertDialog dialog;
    private Activity mActivity;
    public PrevOrdersAdapter(Context mContext, List<Ordersdb> mData,Activity mActivity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity=mActivity;
    }

    @Override
    public PrevOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.prevordersrv,parent,false);
        return new PrevOrdersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PrevOrdersAdapter.MyViewHolder holder, final int position) {

        holder.orderno.setText("ORDER NUMBER : "+mData.get(position).getOrderno());
        holder.itemnametext.setText(mData.get(position).getItems()+" X "+mData.get(position).getQuantity());
        holder.writereview.setText(mData.get(position).getStatus());
        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.itemimg);

        holder.prevorderll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.writereview.getText().equals("Feedback Sent") && !mData.get(position).getCategory().equalsIgnoreCase("drinks")) {
                    BottomSheetPrevOrders bottomSheetPrevOrders = new BottomSheetPrevOrders(mData.get(position).getOrderno(), mData.get(position).getItems(), position,mData.get(position).getDatetime(),mData.get(position).getCategory());
                    bottomSheetPrevOrders.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                    bottomSheetPrevOrders.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "BottomSheet");

                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemnametext,orderno,writereview;
        ImageView itemimg;
        LinearLayout prevorderll;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemnametext = (TextView) itemView.findViewById(R.id.itemnametext) ;
            itemimg = (ImageView) itemView.findViewById(R.id.itemimg);
            orderno=itemView.findViewById(R.id.orderno);
            prevorderll=itemView.findViewById(R.id.prevorderll);
            writereview=itemView.findViewById(R.id.writereview);
        }
    }


}