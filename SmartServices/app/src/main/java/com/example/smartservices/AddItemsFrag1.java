package com.example.smartservices;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import moe.feng.common.stepperview.VerticalStepperItemView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemsFrag1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemsFrag1 extends Fragment {
    private VerticalStepperItemView mSteppers[] = new VerticalStepperItemView[3];
    private Button mNextBtn0, mNextBtn1, mPrevBtn1, mNextBtn2, mPrevBtn2,uploadimage;
    String[] str=new String[]{"lunch","tiffin","drinks"};
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;
    Spinner spinner;
    ImageView imageView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageTask mUploadTask;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    EditText ed1,ed2,ed3,ed4,ed5,ed6;
    private int mActivatedColorRes = R.color.material_blue_500;
    private int mDoneIconRes = R.drawable.ic_done_white_16dp;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddItemsFrag1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemsFrag1.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemsFrag1 newInstance(String param1, String param2) {
        AddItemsFrag1 fragment = new AddItemsFrag1();
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
        View v=inflater.inflate(R.layout.fragment_add_items_frag1, container, false);
        spinner=v.findViewById(R.id.spinner1);
        imageView=v.findViewById(R.id.addimgiv);
        mStorageRef = FirebaseStorage.getInstance().getReference("items");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),R.layout.dropdown_menu_popup_item,str);
        spinner.setAdapter(arrayAdapter);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSteppers[0] = view.findViewById(R.id.stepper_0);
        mSteppers[1] = view.findViewById(R.id.stepper_1);
        mSteppers[2] = view.findViewById(R.id.stepper_2);

        VerticalStepperItemView.bindSteppers(mSteppers);
        ed1=view.findViewById(R.id.itemname);
        ed2=view.findViewById(R.id.price);
        ed3=view.findViewById(R.id.caloriesadmin);
        ed4=view.findViewById(R.id.carbsadmin);
        ed5=view.findViewById(R.id.protsadmin);
        ed6=view.findViewById(R.id.fatsadmin);
        mNextBtn0 = view.findViewById(R.id.button_next_0);
        mNextBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(ed1.getText().toString().isEmpty())
               {
                   Toast.makeText(getContext(), "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                   return;
               }
               if(ed2.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Price cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSteppers[0].nextStep();
            }
        });



        mPrevBtn1 = view.findViewById(R.id.button_prev_1);
        mPrevBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppers[1].prevStep();
            }
        });

        mNextBtn1 = view.findViewById(R.id.button_next_1);
        mNextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed3.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Calories cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ed4.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Carbohydrates be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ed5.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Proteins cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ed6.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Fats be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSteppers[1].nextStep();

            }
        });

        mPrevBtn2 = view.findViewById(R.id.button_prev_2);
        mPrevBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppers[2].prevStep();
            }
        });

        mNextBtn2 = view.findViewById(R.id.button_next_2);
        uploadimage=view.findViewById(R.id.addimage);
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        mNextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Upload in progress.. Please wait", Toast.LENGTH_SHORT).show();
                    uploadFile();
                }
                Snackbar.make(view, "Finish!", Snackbar.LENGTH_LONG).show();
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imageView);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            progressDialog.setCanceledOnTouchOutside(false);
            final StorageReference FileRef = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = FileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);
                            FileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    CanteenCDB upload = new CanteenCDB(ed1.getText().toString(),ed2.getText().toString(),spinner.getSelectedItem().toString(),ed3.getText().toString(),ed4.getText().toString(),ed5.getText().toString(),ed6.getText().toString(),uri.toString());
                                    //   Toast.makeText(ImageActivity.this, "This"+uri, Toast.LENGTH_SHORT).show();
                                    HashMap<String,String> hm=new HashMap<>();
                                    hm.put("image",upload.getImage());
                                    hm.put("name",ed1.getText().toString());
                                    hm.put("price",ed2.getText().toString());
                                    hm.put("calories",ed3.getText().toString());
                                    hm.put("carbs",ed4.getText().toString());
                                    hm.put("prots",ed5.getText().toString());
                                    hm.put("fats",ed6.getText().toString());
                                    HashMap<String,String> hm1=new HashMap<>();
                                    hm1.put("image",upload.getImage());
                                    hm1.put("name",ed1.getText().toString());
                                    hm1.put("price",ed2.getText().toString());
                                    hm1.put("calories",ed3.getText().toString());
                                    hm1.put("carbs",ed4.getText().toString());
                                    hm1.put("prots",ed5.getText().toString());
                                    hm1.put("fats",ed6.getText().toString());
                                    hm1.put("category",spinner.getSelectedItem().toString());
                                    hm1.put("count","0");
                                    hm1.put("sum","0");
                                    db.collection(spinner.getSelectedItem().toString()).add(hm);
                                    if(!spinner.getSelectedItem().toString().equalsIgnoreCase("drinks")) {
                                        db.collection("popular").add(hm1);
                                    }
                                    db.collection(spinner.getSelectedItem().toString()).whereEqualTo("name",ed1.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                            {
                                                db.collection(spinner.getSelectedItem().toString()).document(queryDocumentSnapshot.getId()).update("rating",0);
                                            }
                                        }
                                    });
                                    if(!spinner.getSelectedItem().toString().equalsIgnoreCase("drinks")) {
                                        db.collection("popular").whereEqualTo("name", ed1.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                    db.collection("popular").document(queryDocumentSnapshot.getId()).update("rating", 0);
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                            Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            Toast.makeText(getContext(), "No File Selected", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}