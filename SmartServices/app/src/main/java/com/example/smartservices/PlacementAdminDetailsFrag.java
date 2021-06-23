package com.example.smartservices;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PlacementAdminDetailsFrag extends Fragment {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    CollectionReference notebookref=db.collection("placements");
    PlacementAdapter adapter;
    RecyclerView recyclerView;
    public PlacementAdminDetailsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_placement_admin_details, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewplacementsadmin);
        Query query = notebookref.orderBy("link", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        adapter = new PlacementAdapter(options,getContext(),"admin");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onStart() {


        super.onStart();
        adapter.startListening();


    }
    @Override
    public void onStop() {


        super.onStop();
        adapter.stopListening();


    }
}