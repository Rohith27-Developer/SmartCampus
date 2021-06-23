package com.example.smartservices;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    Ordersdb ordersdb;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    List<Ordersdb> list=new ArrayList<>();
    RecyclerView lv;
    RecyclerAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank, container, false);
        lv=view.findViewById(R.id.list1);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.addItemDecoration(new DividerItemDecoration(lv.getContext(), DividerItemDecoration.VERTICAL));
        String em=getActivity().getIntent().getStringExtra("email");
        db.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable final QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                list.removeAll(list);
                for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                    ordersdb = documentSnapshot.toObject(Ordersdb.class);
                    String item = ordersdb.getItems();
                    String quantity = ordersdb.getQuantity();
                    String email=ordersdb.getEmail();
                    String image=ordersdb.getImage();
                    String orderno=ordersdb.getOrderno();
                    String id=documentSnapshot.getId();
                    if(email!=null && email.equalsIgnoreCase(em))
                    {
                        list.add(new Ordersdb(item,quantity,image,email,orderno));
                    }
                }
               if(list.size()==0)
                {

                }
                Toast.makeText(getContext(), ""+list.size(), Toast.LENGTH_SHORT).show();
                adapter=new RecyclerAdapter(getContext(),list);
                lv.setAdapter(adapter);

            }
        });
        return view;
    }
}