package com.example.eprojectbuscitymanagementshayan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;


public class MainDB extends SQLiteOpenHelper{

    public static final String DBNAME = "main.db";

    public MainDB(Context context) {
        super(context, DBNAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table routes(route_id INTEGER primary key, route_name TEXT, route_destination_from TEXT, route_destination_to TEXT, belongsToUser INTEGER, isFav INTEGER)");
        db.execSQL("create table buses(bus_id INTEGER primary key, bus_name TEXT, bus_number TEXT, bus_color TEXT, bus_fare TEXT, belongsToUser INTEGER,  belongsToRoute INTEGER, isFav INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists myusers");

    }

    public Boolean insertRoute(String name, String dest_from, String dest_to, int ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("route_name",name);
        values.put("route_destination_from",dest_from);
        values.put("route_destination_to",dest_to);
        values.put("belongsToUser",ID);
        values.put("isFav",0);


        long result = db.insert("routes", null, values);

        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public Boolean insertBus(String busname, String busnumber, String busfare, String buscolor, int routeID, int ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("bus_name",busname);
        values.put("bus_number",busnumber);
        values.put("bus_fare",busfare);
        values.put("bus_color",buscolor);
        values.put("belongsToRoute",routeID);
        values.put("belongsToUser",ID);
        values.put("isFav",0);


        long result = db.insert("buses", null, values);

        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }



    public Boolean editRoute(String ID,String name, String dest_from, String dest_to)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("route_name",name);
        values.put("route_destination_from",dest_from);
        values.put("route_destination_to",dest_to);



        int result = db.update("routes", values,"route_id=?", new String[]{ID});

        if(result>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }



    public Boolean checkRouteName(String name, String ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from routes where route_name=? AND belongsToUser=?", new String[] {name,ID});

        if (cursor.getCount()>0) {
            return true;
        }
        else {
            return false;
        }
    }


     Cursor busOnlyRoutesList(int ID)
    {

        SQLiteDatabase db = this.getReadableDatabase();

         String query = "SELECT route_name FROM routes WHERE belongsToUser="+ID;

         Cursor cursor = null;
         if(db!=null)
         {
             cursor = db.rawQuery(query, null);
         }

         return cursor;



    }

    public int getRouteID(String routename, String userid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select route_id from routes where route_name=? and belongsToUser=?", new String[] {routename, userid});

        cursor.moveToFirst();
        int ID = cursor.getInt(0);
        return ID;

    }

    Cursor getAllRoutes(int ID)
    {

        String query = "SELECT * FROM routes WHERE belongsToUser="+ID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db!=null)
        {
            cursor = db.rawQuery(query,null);
        }

        return cursor;

    }

    Cursor getAllBuses(int ID, int RouteID)
    {

        String query = "SELECT * FROM buses WHERE belongsToUser="+ID+" AND belongsToRoute="+RouteID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db!=null)
        {
            cursor = db.rawQuery(query,null);
        }

        return cursor;

    }

    Cursor getFavRoutes(int ID)
    {
        String query = "SELECT * FROM routes WHERE belongsToUser="+ID+" AND isFav=1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db!=null)
        {
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }

    Cursor getFavBuses(int ID)
    {
        String query = "SELECT * FROM buses WHERE belongsToUser="+ID+" AND isFav=1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db!=null)
        {
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }

    public void updateFavRoute(String ID)
    {
        SQLiteDatabase dbread = this.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("SELECT isFav FROM routes WHERE route_id=?", new String[] {ID});



        cursor.moveToFirst();
        int favid = cursor.getInt(0);

        SQLiteDatabase dbwrite = this.getWritableDatabase();

        if(favid==0)
        {
            dbwrite.execSQL("update routes set isFav = 1 where route_id=?", new String[] {ID});
        }
        else if(favid==1)
        {
            dbwrite.execSQL("update routes set isFav = 0 where route_id=?", new String[] {ID});
        }
    }

    public void updateFavBus(String ID)
    {
        SQLiteDatabase dbread = this.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("SELECT isFav FROM buses WHERE bus_id=?", new String[] {ID});



        cursor.moveToFirst();
        int favid = cursor.getInt(0);

        SQLiteDatabase dbwrite = this.getWritableDatabase();

        if(favid==0)
        {
            dbwrite.execSQL("update buses set isFav = 1 where bus_id=?", new String[] {ID});
        }
        else if(favid==1)
        {
            dbwrite.execSQL("update buses set isFav = 0 where bus_id=?", new String[] {ID});
        }
    }
    
    public void deleteRoute(String ID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("routes", "route_id=?", new String[] {ID});
        db.delete("buses", "belongsToRoute=?", new String[] {ID});

    }


    public void deleteBus(String ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("buses", "bus_id=?", new String[] {ID});
    }

    public Boolean editBus(String ID, String my_busname, String my_busnumber, String my_busfare, String my_buscolor, String my_busroute) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("bus_name",my_busname);
        values.put("bus_number",my_busnumber);
        values.put("bus_fare",my_busfare);
        values.put("bus_color",my_buscolor);
        values.put("belongsToRoute",my_busroute);



        int result = db.update("buses", values,"bus_id=?", new String[]{ID});

        if(result>0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public String editBusGetRouteName(String ID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select route_name from routes where route_id=?", new String[] {ID});

        cursor.moveToFirst();
        String name = cursor.getString(0);
        return name;
    }

    Cursor searchOnlyRouteSource(int ID)
    {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT route_destination_from FROM routes WHERE belongsToUser="+ID;

        Cursor cursor = null;
        if(db!=null)
        {
            cursor = db.rawQuery(query, null);
        }

        return cursor;



    }


    Cursor searchOnlyRouteDestination(int ID)
    {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT route_destination_to FROM routes WHERE belongsToUser="+ID;

        Cursor cursor = null;
        if(db!=null)
        {
            cursor = db.rawQuery(query, null);
        }

        return cursor;



    }


    Cursor getSearchRouteID(String my_searchsource, String my_searchdestination, String id, Context context) {



        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("select route_id from routes where route_destination_from=? AND route_destination_to=? AND belongsToUser=?", new String[] {my_searchsource,my_searchdestination,id});

        if(cursor.getCount()==0)
        {
            cursor = db.rawQuery("select route_id from routes where route_destination_from=? AND route_destination_to=? AND belongsToUser=?", null);
            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
        }

            return cursor;




    }
}
