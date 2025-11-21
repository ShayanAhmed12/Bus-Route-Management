package com.example.eprojectbuscitymanagementshayan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    MainDB DB = new MainDB(this);
    UserDB userdb = new UserDB(this);

    Button searchButton;

    ListView searchListView;


    Spinner searchSource, searchDestination;

    ArrayList<String> busID = new ArrayList<>();
    ArrayList<BusData> busList = new ArrayList<>();
    ArrayList<String> source_array, destination_array;
    ArrayAdapter<String> source_adapter, destination_adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchListView = findViewById(R.id.searchList);

        searchSource = findViewById(R.id.searchRouteDestFrom);
        searchDestination = findViewById(R.id.searchRouteDestTo);

        searchButton = findViewById(R.id.searchButton);


        source_array = new ArrayList<>();

        Cursor cursor_source = DB.searchOnlyRouteSource(userdb.getUserID());

        for (cursor_source.moveToFirst(); !cursor_source.isAfterLast(); cursor_source.moveToNext()) {
            source_array.add(cursor_source.getString(0));
        }


        source_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, source_array);
        searchSource.setAdapter(source_adapter);


        destination_array = new ArrayList<>();

        Cursor cursor_destination = DB.searchOnlyRouteDestination(userdb.getUserID());

        for (cursor_destination.moveToFirst(); !cursor_destination.isAfterLast(); cursor_destination.moveToNext()) {
            destination_array.add(cursor_destination.getString(0));
        }


        destination_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, destination_array);
        searchDestination.setAdapter(destination_adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(searchSource.getSelectedItem()!=null && searchDestination.getSelectedItem()!=null) {

                    String my_searchsource = searchSource.getSelectedItem().toString();
                    String my_searchdestination = searchDestination.getSelectedItem().toString();
                    String ID = String.valueOf(userdb.getUserID());
                    busList.clear();

                    Cursor routeids = DB.getSearchRouteID(my_searchsource, my_searchdestination, ID, SearchActivity.this);

                    for (routeids.moveToFirst(); !routeids.isAfterLast(); routeids.moveToNext()) {
                        int my_routeid = routeids.getInt(0);
                        displayBusData(my_routeid);
                        CustomBusWRouteAdapter busadapter = new CustomBusWRouteAdapter(SearchActivity.this, busList);
                        searchListView.setAdapter(busadapter);
                        busadapter.notifyDataSetChanged();
                        registerForContextMenu(searchListView);
                    }
                }
                else{
                    Toast.makeText(SearchActivity.this, "Failed to Search Buses!", Toast.LENGTH_SHORT).show();
                }







            }
        });


    }

    void displayBusData(int my_routeid) {
        Cursor cursor = DB.getAllBuses(userdb.getUserID(), my_routeid);

        if (cursor.getCount() == 0) {
            Toast.makeText(SearchActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                busList.add(new BusData(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6), cursor.getString(7)));
                busID.add(cursor.getString(0));
            }
        }
    }



}