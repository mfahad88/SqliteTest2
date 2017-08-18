package com.example.bipl.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fahad on 8/17/2017.
 */

public class Dbhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="SqliteDB";

    Dbhelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create TABLE NAMES (ID INTEGER PRIMARY KEY   AUTOINCREMENT,fname varchar,lname varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS NAMES");
        onCreate(db);
    }

    public void dropTable(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS NAMES");
    }
    public boolean insertRecord(String first,String last){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("fname",first);
        cv.put("lname",last);
        if(db.insert("NAMES",null,cv)!=-1){
            db.close();
            return true;
        }
        return false;
    }

    public List<NameBean> getRecords(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rs=db.rawQuery("SELECT * FROM NAMES",null);
        rs.moveToFirst();
        List<NameBean> list=new ArrayList<>();
        while(!rs.isAfterLast()){
        NameBean nameBean=new NameBean();
            nameBean.setFname(rs.getString(rs.getColumnIndex("fname")));
            nameBean.setLname(rs.getString(rs.getColumnIndex("lname")));
            list.add(nameBean);
            rs.moveToNext();
        }

        return list;
    }

    public boolean updateRecord(int ln,String updateFname,String updateLname){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            ContentValues cv=new ContentValues();
            cv.put("fname",updateFname);
            cv.put("lname",updateLname);
            db.update("NAMES",cv,"ID = ?",new String[]{String.valueOf(ln+1)});
            Log.e("Update: "," | "+ln+" | "+updateFname+" | "+updateLname);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return true;
    }

    public boolean deleteRecord(int ln){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            db.delete("NAMES","ID = ?",new String[]{String.valueOf(ln+1)});
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return true;
    }
}
