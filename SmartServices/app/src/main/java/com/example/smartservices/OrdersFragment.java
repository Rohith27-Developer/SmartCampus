package com.example.smartservices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {
    private RecyclerView lv;
    private MaterialSearchBar searchBar;
    private RecyclerAdapter adapter;
    private List<Ordersdb> list;
    long mLastClickTime=0;
    Ordersdb user;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_orders, container, false);
        lv=view.findViewById(R.id.listview);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        db.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data;
                list.removeAll(list);
                for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                    {
                        data = "";
                        user = documentSnapshot.toObject(Ordersdb.class);
                        String name = user.getItems();
                        String orderno = user.getOrderno();
                        String quan =  user.getQuantity();
                        String email = user.getEmail();
                        String image = user.getImage();
                        list.add(new Ordersdb(name, quan, image, email, orderno));
                    }
                }
                adapter = new RecyclerAdapter(getContext(), list);
                lv.setAdapter(adapter);

            /*    lv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Are you Sure");
                        alertDialog.setMessage("The item is delivered!!");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                            return;
                                        }
                                        mLastClickTime = SystemClock.elapsedRealtime();
                                        String str = documentSnapshots.getDocuments().get(position).get("items").toString();
                                        String id = documentSnapshots.getDocuments().get(position).getId();
                                        db.collection("orders").document(id).delete();
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
                });*/
            }
        });
        return view;
    }

}
