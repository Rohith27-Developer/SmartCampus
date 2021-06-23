package com.example.smartservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LibBagAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<String> modellist;
    List<String> modellist1;
    GoogleSignInClient mGoogleSignInClient;
    ArrayList<String> al5;
    public LibBagAdapter(Context mcontext,List<String> modellist,List<String> modellist1)
    {
        this.modellist=modellist;
        this.modellist1=modellist1;
        context=mcontext;
        inflater=(LayoutInflater.from(context));
      //  this.al5=new ArrayList<>();
        //this.al5.addAll(modellist);

    }
    public class ViewHolder
    {
        TextView mname,desc;
        RoundedImageView imageView;
    }
    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int position) {
        return modellist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.libbaglist,null);
            holder.mname=convertView.findViewById(R.id.datebagtext);
            holder.desc=convertView.findViewById(R.id.status_text);
            holder.imageView=convertView.findViewById(R.id.profile_image);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.mname.setText(modellist1.get(position));
       holder.desc.setText(modellist.get(position));
       switch (position%4)
       {
           case 0:
               holder.imageView.setBackgroundResource(R.drawable.read1);
               break;
           case 1:
               holder.imageView.setBackgroundResource(R.drawable.read2);
               break;
           case 2:
               holder.imageView.setBackgroundResource(R.drawable.read3);
               break;
           case 3:
               holder.imageView.setBackgroundResource(R.drawable.read4);
               break;
       }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }


}
