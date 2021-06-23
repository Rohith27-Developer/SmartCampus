package com.example.smartservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Cart1 extends AppCompatActivity {
    private ExpandingList mExpandingList;
    ProgressBar progressBar;
    TextView tv;
    private SlidrInterface slidr;
    int tot1 = 0;
    long mLastClickTime = 0;
    StatsDb sb;
    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int max = 0;
    String uid, uname;
    List<Integer> list = new ArrayList<>();
    boolean result = false;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> list1 = new ArrayList<>();
    ImageView orders;
    private static final int TEZ_REQUEST_CODE = 123;
    ArrayAdapter<String> arrayAdapter;
    String email;
    DatabaseHelper1 mydb;
    private int refreshcount = 0;
    Button btn;
    private SwipeRefreshLayout refreshLayout;

    ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (InitApplication.getInstance().isNightModeEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#212121"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#1e3d59"));
            }

        }
        setContentView(R.layout.activity_cart1);
        slidr = Slidr.attach(this);
        orders = findViewById(R.id.myorders);
        email = getIntent().getStringExtra("email");
        mydb = new DatabaseHelper1(Cart1.this);
    /*    db.collection("rohith2712@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                                if(count==0)
                                {
                                    Intent intent = new Intent(Cart1.this, Empty.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Log.d("Hello", "Error getting documents: ", task.getException());
                        }
                    }
                });*/
        mExpandingList = findViewById(R.id.expanding_list_main);
        //refreshLayout = findViewById(R.id.swipe_refresh_layout);
        btn = findViewById(R.id.btn_pay);
//        progressBar=findViewById(R.id.progressbar);
        mExpandingList.removeAllViews();
        tv = findViewById(R.id.tot);
        // progressBar.setVisibility(View.VISIBLE);
      /*progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );*/
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(Cart1.this, MyOrders.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        createItems();
        sb=new StatsDb(this);
        db.collection("canteen_admin").document("1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    uid = documentSnapshot.getString("uid");
                    uname = documentSnapshot.getString("uname");
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getText().toString() != null) {
                    gpay(uid, uname, tot1);
                /*    Intent intent = new Intent(Cart1.this, Payment.class);
                    intent.putExtra("email", email);
                    intent.putExtra("total", tot1);
                    startActivity(intent);
                    finish();*/

                }
            }
        });
    }

    private void createItems() {
        Cursor res = mydb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Cart1.this, Empty.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
            return;
        }
        tot1 = 0;
        while (res.moveToNext()) {
            addItem(res.getString(0), new String[]{res.getString(1), res.getString(2), res.getString(3)}, R.color.sky, R.drawable.din);
            tot1 += Integer.parseInt(res.getString(3));

        }
        tv.setText("Total Bill Amount:" + " " + "₹" + " " + tot1);
    }

    private void addItem(final String title, String[] subItems, int colorRes, int iconRes) {
        //Let's create an item with R.layout.expanding_layout

        final ExpandingItem item = mExpandingList.createNewItem(R.layout.expanding_layout);
        subItems[0] = "Price: " + subItems[0];
        subItems[1] = "Quantity: " + subItems[1];
        subItems[2] = "Total: " + subItems[2];
        //If item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            //It is possible to get any view inside the inflated layout. Let's set the text in the item
            ((TextView) item.findViewById(R.id.title)).setText(title);

            //We can create items in batch.
            item.createSubItems(subItems.length);
            for (int i = 0; i < item.getSubItemsCount(); i++) {
                //Let's get the created sub item by its index
                final View view = item.getSubItemView(i);

                //Let's set some values in
                configureSubItem(item, view, subItems[i]);
            }
            item.findViewById(R.id.add_more_sub_items).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //progressBar.setVisibility(View.VISIBLE);
//                    progressDialog.show();
                    mExpandingList.removeAllViews();
                    Cursor res = mydb.getAllData();
                    mExpandingList.removeItem(item);
                    while (res.moveToNext()) {
                        if (title.equalsIgnoreCase(res.getString(0))) {
                            mydb.updateData(res.getString(0), res.getString(1), String.valueOf(Integer.parseInt(res.getString(2)) + 1), String.valueOf((Integer.parseInt(res.getString(2)) + 1) * (Integer.parseInt(res.getString(1)))),res.getString(4));
                            Toast.makeText(Cart1.this, ""+res.getString(4), Toast.LENGTH_SHORT).show();
                        }
                    }
                    createItems();

                }

            });

            item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    progressDialog.show();
                    mExpandingList.removeAllViews();
                    Cursor res = mydb.getAllData();
                    mExpandingList.removeItem(item);
                    while (res.moveToNext()) {
                        if (title.equalsIgnoreCase(res.getString(0))) {
                            mydb.deleteData(res.getString(0));
                        }
                    }
                    createItems();
                }
            });

        }
    }

    private void configureSubItem(final ExpandingItem item, final View view, String subTitle) {
        ((TextView) view.findViewById(R.id.sub_title)).setText(subTitle);
    }


    interface OnItemCreated {
        void itemCreated(String title);
    }
    private void gpay(String upi_id, String upi_name, int amt) {
        // m.start();
        try {
            Uri uri = Uri.parse("upi://pay").buildUpon()
                    .appendQueryParameter("pa", upi_id)
                    .appendQueryParameter("pn", upi_name)
                    .appendQueryParameter("cu", "INR")
                    .appendQueryParameter("tn", "Order")
                    .appendQueryParameter("am", String.valueOf(amt))
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            // intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
            startActivityForResult(intent, TEZ_REQUEST_CODE);
        }catch(Exception e)
        {
            Toast.makeText(this, "Install UPI APP", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TEZ_REQUEST_CODE:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        gpayPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        gpayPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    gpayPaymentDataOperation(dataList);
                }
                break;

        }
    }
    private void gpayPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Cart1.this)) {
            String str = data.get(0);
            Log.d("gpay", "upiPaymentDataOperation1: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                Toast.makeText(Cart1.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                Cursor res = mydb.getAllData();
                db.collection("orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                list.add(Integer.parseInt(documentSnapshot.getId()));
                            }
                            try {
                                max = Collections.max(list);
                            } catch (Exception e) {
                                max = 0;
                            }
                        }
                        while (res.moveToNext()) {
                            max=max+1;
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("items", res.getString(0));
                            hm.put("quantity", res.getString(2));
                            hm.put("orderno", String.valueOf(max));
                            hm.put("email", email);
                            hm.put("image",res.getString(5));
                            hm.put("category",res.getString(6));
                            hm.put("datetime",formatter.format(date));
                            db.collection("orders").document(String.valueOf(max)).set(hm);
                            result = true;
                        }
                        Cursor res = mydb.getAllData();
                        Cursor r=sb.getAllData();
                        sb.drop();
                        while (res.moveToNext()) {
                            sb.insertData(res.getString(0), res.getString(4),res.getString(7));

                        }
                        mydb.drop();
                    }
                });

                Intent intent = new Intent(Cart1.this, ContinueBooking.class);
                intent.putExtra("email",email);
                startActivity(intent);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Cart1.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(Cart1.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(Cart1.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Cart1 context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
