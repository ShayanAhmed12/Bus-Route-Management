package com.example.eprojectbuscitymanagementshayan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class UserDB extends SQLiteOpenHelper {

    public static final String DBNAME = "myusers.db";

    public UserDB(Context context) {
        super(context, DBNAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table myusers(user_id INTEGER primary key, user_name TEXT, user_password TEXT, user_email TEXT, isSignedIn INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists myusers");

    }

    public Boolean insertData(String username, String password, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_name",username);
        values.put("user_password",password);
        values.put("user_email",email);
        values.put("isSignedIn",0);



        long result = db.insert("myusers", null, values);

        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Boolean checkUsername(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from myusers where user_name=?", new String[] {username});

        if (cursor.getCount()>0) {
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean checkUsernamePassword(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from myusers where user_name=? and user_password=?", new String[] {username,password});


        if(cursor.getCount()>0)
        {
            db.execSQL("UPDATE myusers SET isSignedIn=0 WHERE isSignedIn>0");
            db.execSQL("UPDATE myusers SET isSignedIn=1 WHERE user_name=? and user_password=?", new String[] {username,password});
            return true;
        }
        else
        {
            return false;
        }

    }

    public Boolean isLoggedIn()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select user_id from myusers where isSignedIn=1", new String[] {});

        if(cursor.getCount()==1)
        {
            return true;
        }
        else if (cursor.getCount()>1)
        {
            db.execSQL("UPDATE myusers SET isSignedIn=0 WHERE isSignedIn>0");
            return false;
        }
        else
        {
            return false;
        }

    }

    public int getUserID()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select user_id from myusers where isSignedIn=1", new String[] {});

        cursor.moveToFirst();
        int ID = cursor.getInt(0);
        return ID;

    }

    public String getUsername()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select user_name from myusers where isSignedIn=1", new String[] {});

        cursor.moveToFirst();
        String name = cursor.getString(0);
        return name;

    }


    void logout()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE myusers SET isSignedIn=0 WHERE isSignedIn>0");
    }

    public Boolean isOldUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select user_id from myusers", new String[] {});

        if(cursor.getCount()>0)
        {
            return true;
        }
        else
        {

            return false;
        }


    }
}
