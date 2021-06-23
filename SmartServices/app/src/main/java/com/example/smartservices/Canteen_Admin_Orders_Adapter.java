package com.example.smartservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
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

public class Canteen_Admin_Orders_Adapter extends RecyclerView.Adapter<Canteen_Admin_Orders_Adapter.MyViewHolder> {
        private Context mContext ;
        private List<Ordersdb> mData ;
        ArrayList<Ordersdb> al;
    ArrayList<Integer> list;
    int max=0;
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        public Canteen_Admin_Orders_Adapter(Context mContext, List<Ordersdb> mData) {
            this.mContext = mContext;
            this.mData = mData;
            this.al=new ArrayList<>();
            this.al.addAll(mData);
            list=new ArrayList<>();
        }

        @Override
        public Canteen_Admin_Orders_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view ;
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.canteenadminorders,parent,false);
            return new Canteen_Admin_Orders_Adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Canteen_Admin_Orders_Adapter.MyViewHolder holder, final int position) {

            holder.orderno.setText(mData.get(position).getOrderno());
            holder.itemnametext.setText(mData.get(position).getItems()+" X "+mData.get(position).getQuantity());
            holder.email.setText(mData.get(position).getEmail());

            Glide.with(mContext).load(mData.get(position).getImage()).into(holder.itemimg);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Are you Sure");
                    alertDialog.setMessage("The item is delivered");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = mData.get(position).getOrderno();
                                    db.collection("prevorders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                    list.add(Integer.parseInt(documentSnapshot.getId()));
                                                }
                                                try {
                                                    max = Collections.max(list);
                                                } catch (Exception e) {
                                                    max = 0;
                                                }
                                                max=max+1;
                                                db.collection("orders").document(str).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot documentSnapshot=task.getResult();
                                                        HashMap<String,String> hm=new HashMap<>();
                                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                        Date date = new Date();
                                                        hm.put("orderno",documentSnapshot.get("orderno").toString());
                                                        hm.put("items",documentSnapshot.get("items").toString());
                                                        hm.put("email",documentSnapshot.get("email").toString());
                                                        hm.put("quantity",documentSnapshot.get("quantity").toString());
                                                        hm.put("image",documentSnapshot.get("image").toString());
                                                        hm.put("datetime",formatter.format(date));
                                                        hm.put("category",documentSnapshot.get("category").toString());
                                                        hm.put("status","Write a review");
                                                        db.collection("prevorders").document(String.valueOf(max)).set(hm);
                                                        db.collection("orders").document(str).delete();
                                                    }
                                                });

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
    public void filter(String charText)
    {
        charText=charText.toLowerCase(Locale.getDefault());
        mData.clear();
        if(charText.length()==0)
        {
            mData.addAll(al);
        }
        else
        {
            for(Ordersdb ordersdb:al)
            {
                if(ordersdb.getOrderno().toLowerCase(Locale.getDefault()).contains(charText) || ordersdb.getEmail().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mData.add(ordersdb);
                }
            }
        }
        notifyDataSetChanged();
    }

}