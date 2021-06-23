package com.example.smartservices;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;

public class BottomSheetFragment extends AAH_FabulousFragment implements View.OnClickListener {
RecyclerView rv;
LinearLayout l1,l2,l3;
   private static final int NONE = 0;
    private static final int SWIPE = 1;
    int mode = NONE;
    float startY;
    float stopY;
    final int TRESHOLD = 100;
    public static BottomSheetFragment newInstance() {
        BottomSheetFragment f = new BottomSheetFragment();
        return f;
    }

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override

    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        RelativeLayout rl_content = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);
        l1=contentView.findViewById(R.id.cart);
        l2=contentView.findViewById(R.id.orders);
        l3=contentView.findViewById(R.id.stats);
        l1.setOnClickListener((View.OnClickListener) this);
        l2.setOnClickListener((View.OnClickListener) this);
        l3.setOnClickListener((View.OnClickListener) this);
        contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter("closed");
            }
        });

        //params to set
        setAnimationDuration(400); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        setCallbacks((Callbacks) getActivity()); //optional; to get back result
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
//        setViewPager(vp_types); //optional; if you use viewpager that has scrollview
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog,style); //call super at last
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart:
                // textView.setText("SHARE");

                Intent intent=new Intent(getContext(),Cart1.class);
                intent.putExtra("email", getActivity().getIntent().getStringExtra("email"));
                startActivity(intent);
                break;
            case R.id.orders:
                // textView.setText("UPLOAD");
                Intent intent1=new Intent(getContext(),MyOrders.class);
                intent1.putExtra("email", getActivity().getIntent().getStringExtra("email"));
                startActivity(intent1);
                break;
            case R.id.stats:
                Intent intent2=new Intent(getContext(),Statistics.class);
                intent2.putExtra("email", getActivity().getIntent().getStringExtra("email"));
                startActivity(intent2);
                break;

        }
    }



    }
