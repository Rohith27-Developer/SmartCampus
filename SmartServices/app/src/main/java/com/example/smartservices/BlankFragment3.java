package com.example.smartservices;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment3 extends Fragment {
FirebaseFirestore db=FirebaseFirestore.getInstance();
ArrayList<Ordersdb> list=new ArrayList<>();
Ordersdb ordersdb;
LinearLayout prev_button;
    private SearchView mSearchField;

Canteen_Admin_Orders_Adapter canteen_admin_orders_adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment3 newInstance(String param1, String param2) {
        BlankFragment3 fragment = new BlankFragment3();
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
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_blank3, container, false);
        mSearchField = v.findViewById(R.id.search_field);
        int searcid=getResources().getIdentifier("android:id/search_src_text",null,null);
        EditText lne=mSearchField.findViewById(searcid);
        prev_button=v.findViewById(R.id.prev_button);
        prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),PrevOrdersAdmin.class);
                startActivity(intent);
            }
        });

        mSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchField.onActionViewExpanded();
            }
        });
        mSearchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    canteen_admin_orders_adapter.filter("");
                }
                else
                {
                    canteen_admin_orders_adapter.filter(newText);
                }

                return true;
            }
        });
        RecyclerView recyclerView=v.findViewById(R.id.canteen_orders_admin_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
       GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        db.collection("orders").orderBy("orderno").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    list.add(new Ordersdb(item,quantity,image,email,orderno));
                }
                if(list.size()==0)
                {

                }
                canteen_admin_orders_adapter=new Canteen_Admin_Orders_Adapter(getContext(),list);
                recyclerView.setAdapter(canteen_admin_orders_adapter);

            }
        });
        return v;
    }
}