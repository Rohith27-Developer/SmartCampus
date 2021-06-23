package com.example.smartservices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ViewItems extends Fragment {
  RecyclerView recyclerView;
  List<CanteenCDB> list=new ArrayList<>();
  CanteenCDB canteenCDB;
    ViewItemsAdminAdapter viewItemsAdminAdapter;
  FirebaseFirestore db=FirebaseFirestore.getInstance();
    CollectionReference notebookref = db.collection("lunch");
    public ViewItems() {
        // Required empty public constructor
    }

    public static ViewItems newInstance(String param1, String param2) {
        ViewItems fragment = new ViewItems();
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
        View view=inflater.inflate(R.layout.fragment_view_items, container, false);
        recyclerView=view.findViewById(R.id.viewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db.collection("lunch").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                list.removeAll(list);
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    canteenCDB=queryDocumentSnapshot.toObject(CanteenCDB.class);
                    list.add(new CanteenCDB(canteenCDB.getName(),canteenCDB.getStatus(),canteenCDB.getRating(),canteenCDB.getImage()));
                }
                viewItemsAdminAdapter=new ViewItemsAdminAdapter(getContext(),list,"lunch");
                recyclerView.setAdapter(viewItemsAdminAdapter);
            }
        });
        ImageView imageView= view.findViewById(R.id.choosecat);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(getContext(),imageView);
                popupMenu.getMenuInflater().inflate(R.menu.view_items_admin,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getContext(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                        if(item.getTitle().toString().equalsIgnoreCase("lunch"))
                        {
                            db.collection("lunch").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    list.removeAll(list);
                                    for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                                    {
                                        canteenCDB=queryDocumentSnapshot.toObject(CanteenCDB.class);
                                        list.add(new CanteenCDB(canteenCDB.getName(),canteenCDB.getStatus(),canteenCDB.getRating(),canteenCDB.getImage()));
                                    }
                                    viewItemsAdminAdapter=new ViewItemsAdminAdapter(getContext(),list,"lunch");
                                    recyclerView.setAdapter(viewItemsAdminAdapter);
                                }
                            });
                        }
                        else if(item.getTitle().toString().equalsIgnoreCase("tiffin"))
                        {
                            db.collection("tiffin").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    list.removeAll(list);
                                    for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                                    {
                                        canteenCDB=queryDocumentSnapshot.toObject(CanteenCDB.class);
                                        list.add(new CanteenCDB(canteenCDB.getName(),canteenCDB.getStatus(),canteenCDB.getRating(),canteenCDB.getImage()));
                                    }
                                    viewItemsAdminAdapter=new ViewItemsAdminAdapter(getContext(),list,"tiffin");
                                    recyclerView.setAdapter(viewItemsAdminAdapter);
                                }
                            });
                        }
                        else
                        {
                            db.collection("drinks").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    list.removeAll(list);
                                    for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                                    {
                                        canteenCDB=queryDocumentSnapshot.toObject(CanteenCDB.class);
                                        list.add(new CanteenCDB(canteenCDB.getName(),canteenCDB.getStatus(),canteenCDB.getRating(),canteenCDB.getImage()));
                                    }
                                    viewItemsAdminAdapter=new ViewItemsAdminAdapter(getContext(),list,"drinks");
                                    recyclerView.setAdapter(viewItemsAdminAdapter);
                                }
                            });

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        return view;
    }

}
