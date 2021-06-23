package com.example.smartservices;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.local.QueryEngine;

import java.util.HashMap;
import java.util.List;

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Securitycdb> mData ;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    String floor;
    Securitycdb securitycdb;
    public SecurityAdapter(Context mContext, List<Securitycdb> mData,String floor) {
        this.mContext = mContext;
        this.mData = mData;
        this.floor=floor;
    }

    @Override
    public SecurityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.sec_admin_rv,parent,false);
        return new SecurityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SecurityAdapter.MyViewHolder holder, final int position) {

        holder.roomno.setText(mData.get(position).getRoomno());
        holder.status.setText(mData.get(position).getStatus());
        db.collection("security1").document(mData.get(position).getRoomno()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String status = documentSnapshot.get("status").toString();
                    String rno = documentSnapshot.get("roomno").toString();
                    TransitionDrawable drawable = (TransitionDrawable) holder.bulb_img.getDrawable();
                    Animation aniSlide;
                    if (status.equals("ON") && rno.equals(mData.get(position).getRoomno())) {
                        holder.imageView.setVisibility(View.VISIBLE);
                        drawable.startTransition(1000);
                        aniSlide = AnimationUtils.loadAnimation(mContext, R.anim.animate_slide_up_enter);
                        holder.imageView.startAnimation(aniSlide);
                        holder.labeledSwitch.setOn(true);
                        holder.imageView.setImageResource(R.drawable.secbg3);
                        holder.status.setText("ON");
                    } else {

                        // holder.labeledSwitch.setOn(false);

                    }
                }catch(Exception e1)
                {

                }
            }
        });
     /*   db.collection("security").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    securitycdb=queryDocumentSnapshot.toObject(Securitycdb.class);
                    String status=securitycdb.getStatus();
                    String rno=securitycdb.getRoomno();
                    Toast.makeText(mContext, rno+""+status, Toast.LENGTH_SHORT).show();
                  try
                  {
                      TransitionDrawable drawable= (TransitionDrawable) holder.bulb_img.getDrawable();
                      Animation aniSlide;
                    if(status.equals("ON") && rno.equals(mData.get(position).getRoomno()))
                    {
                        holder.imageView.setVisibility(View.VISIBLE);
                        drawable.startTransition(1000);
                        aniSlide = AnimationUtils.loadAnimation(mContext,R.anim.animate_slide_up_enter);
                        holder.imageView.startAnimation(aniSlide);
                        holder.labeledSwitch.setOn(true);
                        holder.imageView.setImageResource(R.drawable.secbg3);
                        holder.status.setText("ON");
                    }
                    else
                    {

                       // holder.labeledSwitch.setOn(false);

                    }
                  }
                    catch(Exception e1)
                      {

                      }
                }
            }
        });*/
        holder.labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                TransitionDrawable drawable= (TransitionDrawable) holder.bulb_img.getDrawable();
                Animation aniSlide;

                if(isOn)
                {
                    holder.imageView.setVisibility(View.VISIBLE);
                    drawable.startTransition(1000);
                    aniSlide = AnimationUtils.loadAnimation(mContext,R.anim.animate_slide_up_enter);
                    holder.imageView.startAnimation(aniSlide);
                    holder.imageView.setImageResource(R.drawable.secbg3);
                    myRef=database.getReference(holder.roomno.getText().toString());
                    myRef.setValue(1);

                    db.collection("security1").document(holder.roomno.getText().toString()).update("status","ON");
                    holder.status.setText("ON");
                }
                else
                {
                    aniSlide = AnimationUtils.loadAnimation(mContext,R.anim.animate_slide_up_exit);
                    holder.imageView.startAnimation(aniSlide);
                    holder.imageView.setVisibility(View.INVISIBLE);
                    drawable.reverseTransition(1000);
                    myRef=database.getReference(holder.roomno.getText().toString());
                    myRef.setValue(0);
                    db.collection("security1").document(holder.roomno.getText().toString()).update("status","OFF");
                    holder.status.setText("OFF");

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView roomno,status;
        CardView cardView ;
        LabeledSwitch labeledSwitch;
        ImageView imageView,bulb_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            roomno = itemView.findViewById(R.id.roomnotv) ;
            status = itemView.findViewById(R.id.status_sec);
            labeledSwitch = itemView.findViewById(R.id.switch3);
            cardView=itemView.findViewById(R.id.cardsec);
            imageView=itemView.findViewById(R.id.img2);
            bulb_img=itemView.findViewById(R.id.bulb_img);

        }
    }


}
