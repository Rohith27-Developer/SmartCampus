package com.example.smartservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class CanteenAdapter1 extends FirestoreRecyclerAdapter<CanteenCDB,CanteenAdapter1.Noteholder> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnItemClickListener listener;
    Context context;
    public CanteenAdapter1(@NonNull FirestoreRecyclerOptions<CanteenCDB> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Noteholder holder, int position, @NonNull CanteenCDB model) {
        holder.price.setText(model.getPrice());;
        holder.name.setText(model.getName());
        Glide.with(context).load(model.getImage()).into(holder.i1);
        holder.ratingBar.setRating(Float.parseFloat(model.getRating().toString()));
        //    holder.prior.setText(String.valueOf(model.getPrior()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getStatus().equalsIgnoreCase("Not Available"))
                {
                    StyleableToast.makeText(context, "Not Available", R.style.Toast5).show();
                }
                else {
                    Intent in = new Intent(context, Second.class);
                    in.putExtra("price", holder.price.getText().toString());
                    in.putExtra("calories", model.getCalories());
                    in.putExtra("prots", model.getProts());
                    in.putExtra("fats", model.getFats());
                    in.putExtra("carbs", model.getCarbs());
                    in.putExtra("name", holder.name.getText().toString());
                    in.putExtra("image", model.getImage());
                    in.putExtra("cat", model.getCategory());
                    in.putExtra("rate", model.getRating().toString());
                    in.putExtra("text",model.getText());
                    context.startActivity(in);
                    Animatoo.animateSlideLeft(context);
                }
            }
        });
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
        DatabaseHelper1 mydb;
        mydb = new DatabaseHelper1(context);
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BottomSheetMaterialDialog mAnimatedBottomSheetDialog;
                mAnimatedBottomSheetDialog = new BottomSheetMaterialDialog.Builder((Activity) context)
                        .setTitle(holder.name.getText().toString()+"   ₹"+holder.price.getText().toString())
                        .setMessage("")
                        .setCancelable(false)
                        .setPositiveButton("Pay", R.drawable.ic_pay, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(model.getStatus().equalsIgnoreCase("Not Available"))
                                {
                                    StyleableToast.makeText(context, "Not Available", R.style.Toast5).show();
                                    dialogInterface.dismiss();
                                }
                                else {
                                    int price = Integer.parseInt(holder.price.getText().toString().replaceAll("[^0-9]", ""));
                                    mydb.insertData(holder.name.getText().toString(), String.valueOf(price), String.valueOf(1), String.valueOf(price), String.valueOf(model.getCalories()), model.getImage(), model.getCategory(), model.getFats());
                                    Toast.makeText(context, "Proceed to payment", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, Cart1.class);
                                    intent.putExtra("email", acct.getEmail());
                                    intent.putExtra("total", Integer.parseInt(holder.price.getText().toString().replaceAll("[^0-9]", "")));
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                    dialogInterface.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("cancel", R.drawable.ic_close, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setAnimation("bscan.json")
                        .build();
                mAnimatedBottomSheetDialog.show();

                return false;
            }
        });
    }

    @NonNull
    @Override
    public Noteholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.popu_card,
                parent,false);

        return new Noteholder(v);
    }

    class Noteholder extends RecyclerView.ViewHolder{

        TextView name;
        TextView price;
       CardView cardView;
        ImageView i1;
        RatingBar ratingBar;

        // TextView prior;

        public Noteholder(@NonNull final View itemView) {
            super(itemView);

            //  Button btn=itemView.findViewById(R.id.textButton);
            // image = (ImageView) itemView.findViewById(R.id.dblostimg);
   /*         btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.image_click));
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(link.getText().toString()));
                    context.startActivity(i);
                }
            });*/


       // cb=itemView.findViewById(R.id.rt);

            price=itemView.findViewById(R.id.prrice);
            i1=itemView.findViewById(R.id.immage);
            name=itemView.findViewById(R.id.namme);
            cardView=itemView.findViewById(R.id.carrd);
            ratingBar=itemView.findViewById(R.id.ratingBar1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
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
