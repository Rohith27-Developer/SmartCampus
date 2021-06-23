package com.example.smartservices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper1 extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="cart.db";
    public static final String TABLE_NAME="cart_table";
    public static final String COL_1="ITEMS";
    public static final String COL_2="PRICE";
    public static final String COL_3="QUANTITY";
    public static final String COL_4="TOTAL_PRICE";
    public static final String COL_5="CAL";
    public static final String COL_6="IMAGE";
    public static final String COL_7="CAT";
    public static final String COL_8="PROTS";
    public DatabaseHelper1(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table "+TABLE_NAME+"(ITEMS TEXT PRIMARY KEY,PRICE INTEGER,QUANTITY INTEGER,TOTAL_PRICE INTEGER,CAL TEXT,IMAGE TEXT,CAT TEXT,PROTS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String items,String price,String quantity,String total,String cal,String image,String cat,String prots)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,items);
        contentValues.put(COL_2,price);
        contentValues.put(COL_3,quantity);
        contentValues.put(COL_4,total);
        contentValues.put(COL_5,cal);
        contentValues.put(COL_6,image);
        contentValues.put(COL_7,cat);
        contentValues.put(COL_8,prots);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getItems()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select ITEMS from "+TABLE_NAME,null);
        return res;
    }
    public boolean updateData(String items,String price,String quantity,String total,String cal)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,items);
        contentValues.put(COL_2,price);
        contentValues.put(COL_3,quantity);
        contentValues.put(COL_4,total);
        contentValues.put(COL_5,cal);
        db.update(TABLE_NAME,contentValues,"ITEMS=? ",new String[] {items});
        return true;
    }
    public Integer deleteData(String items)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        //db.execSQL("delete from "+TABLE_NAME+"where ITEMS="+items);
        return db.delete(TABLE_NAME,"ITEMS=? ",new String[]{items});
    }
    public void delete(String items)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+"where ITEMS="+items);
    }
    public void drop()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
