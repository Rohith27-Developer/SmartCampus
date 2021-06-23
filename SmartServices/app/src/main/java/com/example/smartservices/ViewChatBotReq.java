package com.example.smartservices;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewChatBotReq#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewChatBotReq extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<ChatBotCDB> al=new ArrayList<>();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ChatBotCDB chatBotCDB;
    ChatBotAdminAdapter chatBotAdminAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewChatBotReq() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewChatBotReq.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewChatBotReq newInstance(String param1, String param2) {
        ViewChatBotReq fragment = new ViewChatBotReq();
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
        View view=inflater.inflate(R.layout.fragment_view_chat_bot_req, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.recychatbot);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db.collection("chatbotreq").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    chatBotCDB=queryDocumentSnapshot.toObject(ChatBotCDB.class);
                  String email=chatBotCDB.getEmail();
                   String issue= chatBotCDB.getIssue();
                    String mobno=chatBotCDB.getMobno();
                    String name=chatBotCDB.getName();
                    String regno=chatBotCDB.getRegno();
                    ChatBotCDB chatBotCDB=new ChatBotCDB(email,issue,regno,mobno,name);
                    al.add(chatBotCDB);
                }
                chatBotAdminAdapter=new ChatBotAdminAdapter(getContext(),al);
                recyclerView.setAdapter(chatBotAdminAdapter);
            }
        });
        return view;
    }
}