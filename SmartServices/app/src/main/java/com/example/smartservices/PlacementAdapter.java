package com.example.smartservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PlacementAdapter extends FirestoreRecyclerAdapter<Note,PlacementAdapter.PlacementHolder> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnItemClickListener listener;
    Context context;
    String role;
    public PlacementAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context,String role) {
        super(options);
        this.context=context;
        this.role=role;
    }
    @Override
    protected void onBindViewHolder(@NonNull PlacementAdapter.PlacementHolder holder, int position, @NonNull Note model) {
        holder.link.setText(model.getLink());
        holder.link.setVisibility(View.INVISIBLE);
        holder.desc.setText(model.getDesc());
        holder.title.setText(model.getTitle());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(position);
        String s=holder.title.getText().toString();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(s.substring(0,1).toUpperCase(),color);
        holder.image.setImageDrawable(drawable);
    }
    @NonNull
    @Override
    public PlacementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,
                parent,false);

        return new PlacementHolder(v);
    }
    class PlacementHolder extends RecyclerView.ViewHolder{
        TextView link;
        //private OnItemClickListener listener;
        TextView desc;
        TextView title;
        ImageView image;
        CardView cardView;
        Note user;
        String first;
        // TextView prior;

        public PlacementHolder(@NonNull final View itemView) {
            super(itemView);

            Button btn=itemView.findViewById(R.id.textButton);
            image = (ImageView) itemView.findViewById(R.id.cname);

            link=itemView.findViewById(R.id.textview_link);
            desc=itemView.findViewById(R.id.des);
            title=itemView.findViewById(R.id.maintit);
            cardView=itemView.findViewById(R.id.cardview);
            if(role.equals("admin"))
            {
                cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                link.setTextColor(Color.parseColor("#000000"));
                title.setTextColor(Color.parseColor("#000000"));
                desc.setTextColor(Color.parseColor("#000000"));
                btn.setTextColor(Color.parseColor("#000000"));
                btn.setText("DELETE");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Are you Sure");
                        alertDialog.setMessage("The activity will be deleted..");
                        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.collection("placements").whereEqualTo("title",title.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                        db.collection("placements").document(documentSnapshot.getId()).delete();

                                                    }
                                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
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
            else {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.image_click));
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(link.getText().toString()));
                        context.startActivity(i);
                    }
                });
            }
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
                    Intent in=new Intent(itemView.getContext(),Placement2.class);
                    in.putExtra("link",link.getText().toString());
                    in.putExtra("desc",desc.getText().toString());
                    in.putExtra("title",title.getText().toString());
                    in.putExtra("role",role);
                    context.startActivity(in);
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
