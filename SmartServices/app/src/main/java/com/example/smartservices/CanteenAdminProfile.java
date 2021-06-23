package com.example.smartservices;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.tonywills.loadingbutton.HorizontalLoadingButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.r0adkll.slidr.model.SlidrInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CanteenAdminProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CanteenAdminProfile extends Fragment {
    TextView name,emailid,account;
    private SlidrInterface slidr;
    GoogleSignInClient googleSignInClient;
    ImageView imageView;
    private HorizontalLoadingButton horizontalLoadingButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CanteenAdminProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CanteenAdminProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static CanteenAdminProfile newInstance(String param1, String param2) {
        CanteenAdminProfile fragment = new CanteenAdminProfile();
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
        View view=inflater.inflate(R.layout.fragment_canteen_admin_profile, container, false);
        account = view.findViewById(R.id.accountname);
        imageView = view.findViewById(R.id.userphoto);
        horizontalLoadingButton = (HorizontalLoadingButton) view.findViewById(R.id.signoutuser);
        name = view.findViewById(R.id.personname);
        emailid = view.findViewById(R.id.emailid);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        account.setText(acct.getDisplayName());
        name.setText(acct.getDisplayName());
        emailid.setText(acct.getEmail());
        Glide.with(getContext()).load(acct.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(imageView);
        horizontalLoadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalLoadingButton.setLoading(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signOut();
                        horizontalLoadingButton.setLoading(false);
                    }
                }, 1000);
            }
        });
        return view;
    }
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), GoogleSignin.class);
                        intent.putExtra("finish", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }

}