package com.example.smartservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PrevOrdersAdminAdapter extends RecyclerView.Adapter<PrevOrdersAdminAdapter.MyViewHolder> {
    private Context mContext ;
    private List<Ordersdb> mData ;
    ArrayList<Ordersdb> al;
    ArrayList<Integer> list;
    int max=0;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public PrevOrdersAdminAdapter(Context mContext, List<Ordersdb> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.al=new ArrayList<>();
        this.al.addAll(mData);
        list=new ArrayList<>();
    }

    @Override
    public PrevOrdersAdminAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.canteenadminorders,parent,false);
        return new PrevOrdersAdminAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PrevOrdersAdminAdapter.MyViewHolder holder, final int position) {

        holder.orderno.setText(mData.get(position).getOrderno());
        if(mData.get(position).getItems().equalsIgnoreCase("No Data Available"))
        {
            holder.itemnametext.setText(mData.get(position).getItems());
        }
        else {
            holder.itemnametext.setText(mData.get(position).getItems() + " X " + mData.get(position).getQuantity());
            holder.email.setText(mData.get(position).getEmail());

            Glide.with(mContext).load(mData.get(position).getImage()).into(holder.itemimg);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Are you Sure");
                    alertDialog.setMessage("The item will be sent to orders section");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = mData.get(position).getOrderno();
                                    HashMap<String, String> hm = new HashMap<>();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = new Date();
                                    hm.put("orderno", mData.get(position).getOrderno());
                                    hm.put("items", mData.get(position).getItems());
                                    hm.put("email", mData.get(position).getEmail());
                                    hm.put("quantity", mData.get(position).getQuantity());
                                    hm.put("image", mData.get(position).getImage());
                                    hm.put("datetime", formatter.format(date));
                                    hm.put("category", mData.get(position).getCategory());
                                    db.collection("prevorders").whereEqualTo("datetime", mData.get(position).getDatetime()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                db.collection("orders").document(str).set(hm);
                                                db.collection("prevorders").document(queryDocumentSnapshot.getId()).delete();
                                            }
                                        }
                                    });
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemnametext,orderno,email;
        ImageView itemimg;
        LinearLayout mailbgadmin;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemnametext = (TextView) itemView.findViewById(R.id.canteen_admin_item_name) ;
            itemimg = (ImageView) itemView.findViewById(R.id.canteen_admin_image);
            orderno=itemView.findViewById(R.id.ordernoadmin);
            email=itemView.findViewById(R.id.canteen_admin_email);
            mailbgadmin=itemView.findViewById(R.id.mailbgadmin);
            cardView=itemView.findViewById(R.id.card_admin_orders);

        }
    }


}