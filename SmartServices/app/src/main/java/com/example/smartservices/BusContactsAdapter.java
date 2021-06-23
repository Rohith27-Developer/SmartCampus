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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

public class BusContactsAdapter extends RecyclerView.Adapter<BusContactsAdapter.ViewHolder> {
    private List<BusContactsdb> Models;
    private Context context;
    private static final int REQUEST_CALL = 1;
    public BusContactsAdapter(Context context,List<BusContactsdb> Models) {
        this.Models = Models;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_contactsrv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusContactsdb Model=Models.get(position);
        holder.textView1.setText(Model.getPhone());
        holder.textView2.setText(Model.getBusno());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String p = "tel:" + Model.getPhone();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(p));
                    context.startActivity(intent);

                }
            }
        });
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
            textView1=(TextView)itemView.findViewById(R.id.desc);
            textView2=(TextView)itemView.findViewById(R.id.busno);
            img=itemView.findViewById(R.id.callbus);
        }
    }
}