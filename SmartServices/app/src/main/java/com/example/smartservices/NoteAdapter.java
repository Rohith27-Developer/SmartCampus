package com.example.smartservices;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vstechlab.easyfonts.EasyFonts;

public class NoteAdapter  extends FirestoreRecyclerAdapter<Note,NoteAdapter.Noteholder> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnItemClickListener listener;
    Context context;
    String str;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context,String str) {
        super(options);
        this.str=str;
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Noteholder holder, int position, @NonNull Note model) {
        holder.link.setText(model.getLink());
        holder.link.setVisibility(View.INVISIBLE);
        holder.desc.setText(model.getDesc());
        holder.title.setText(model.getTitle());
        holder.month.setText(model.getMonth());

        holder.day.setText(model.getDay());
        switch(position%7)
        {
            case 0:
                holder.datecard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_bg6));
                holder.title.setTextColor(Color.parseColor("#2F80ED"));
                break;
            case 1:
                holder.datecard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background2));
                holder.title.setTextColor(Color.parseColor("#F2994A"));
                break;
            case 2:
                holder.datecard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background3));
                holder.title.setTextColor(Color.parseColor("#7367F0"));
                break;
            case 3:
                holder.datecard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background4));
                holder.title.setTextColor(Color.parseColor("#29323c"));
                break;
            case 4:
                holder.datecard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background5));
                holder.title.setTextColor(Color.parseColor("#44A08D"));
                break;
            case 5:
                holder.datecard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background1));
                holder.title.setTextColor(Color.parseColor("#FC675D"));
                break;
            case 6:
                holder.datecard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_bg_7));
                holder.title.setTextColor(Color.parseColor("#3498db"));
                break;

        }
        //    holder.prior.setText(String.valueOf(model.getPrior()));

    }

    @NonNull
    @Override
    public Noteholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_xml,
                parent,false);

        return new Noteholder(v);
    }

    class Noteholder extends RecyclerView.ViewHolder{
        TextView link;
        //private OnItemClickListener listener;
        TextView desc;
        TextView title;
        TextView day;
        TextView month;
        ImageView image;
        MaterialCardView cardView;
        Note user;
        String first;
        LinearLayout datecard;
        // TextView prior;

        public Noteholder(@NonNull final View itemView) {
            super(itemView);


            link=itemView.findViewById(R.id.textview_link);
            desc=itemView.findViewById(R.id.des);
            title=itemView.findViewById(R.id.maintit);
            cardView=itemView.findViewById(R.id.cardview);
            datecard=itemView.findViewById(R.id.dateCard);
            month=itemView.findViewById(R.id.monthTv);
            day=itemView.findViewById(R.id.dayTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(itemView.getContext(), EventsActivity2.class);
                    in.putExtra("link",link.getText().toString());
                    in.putExtra("desc",desc.getText().toString());
                    in.putExtra("title",title.getText().toString());
                    in.putExtra("day",day.getText().toString());
                    in.putExtra("month",month.getText().toString());
                    in.putExtra("role",str);
                    context.startActivity(in);
                    Animatoo.animateSplit(context);
                }
            });
          /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });*/
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}