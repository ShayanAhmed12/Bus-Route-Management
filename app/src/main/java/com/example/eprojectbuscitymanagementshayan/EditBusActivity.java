package com.example.eprojectbuscitymanagementshayan;

import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditBusActivity extends AppCompatActivity {

    MainDB DB = new MainDB(this);
    UserDB userdb = new UserDB(this);
    EditText busname, busnumber, busfare;
    Spinner buscolor, busroute;
    Button buseditbutton;

    ArrayList<String> color_array, route_array;
    ArrayAdapter<String> color_adapter, route_adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bus);

        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        busname = findViewById(R.id.busName);
        busnumber = findViewById(R.id.busNumber);
        busfare = findViewById(R.id.busFare);
        buscolor = findViewById(R.id.busColor);
        busroute = findViewById(R.id.busRoute);

        buseditbutton = findViewById(R.id.busEditBtn);



        color_array = new ArrayList<>();

        color_array.add("Red");
        color_array.add("Green");
        color_array.add("Blue");
        color_array.add("Orange");
        color_array.add("Yellow");
        color_array.add("Pink");
        color_array.add("White");
        color_array.add("Other");

        color_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, color_array);
        buscolor.setAdapter(color_adapter);


        route_array = new ArrayList<>();

        Cursor cursor = DB.busOnlyRoutesList(userdb.getUserID());

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            route_array.add(cursor.getString(0));
        }


        route_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, route_array);
        busroute.setAdapter(route_adapter);






        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        String myid = b.getString("bus_id");
        String myname = b.getString("bus_name");
        String mynumber = b.getString("bus_number");
        String myfare = b.getString("bus_fare");
        String mycolor = b.getString("bus_color");
        String myroute = b.getString("belongsToRoute");

        busname.setText(myname);
        busnumber.setText(mynumber);
        busfare.setText(myfare);

        buscolor.setSelection(((ArrayAdapter<String>)buscolor.getAdapter()).getPosition(mycolor));
        busroute.setSelection(((ArrayAdapter<String>)busroute.getAdapter()).getPosition(DB.editBusGetRouteName(myroute)));





        buseditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String my_busname = busname.getText().toString();
                String my_busnumber = busnumber.getText().toString();
                String my_busfare = busfare.getText().toString();
                String my_buscolor = buscolor.getSelectedItem().toString();
                String my_busroute = busroute.getSelectedItem().toString();

                if(TextUtils.isEmpty(my_busname) || TextUtils.isEmpty(my_busnumber) || TextUtils.isEmpty(my_busfare))
                {
                    Toast.makeText(EditBusActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else{
                        int ID = userdb.getUserID();
                        String Route_ID = String.valueOf(DB.getRouteID(my_busroute,String.valueOf(ID)));

                        Boolean insert = DB.editBus(myid, my_busname, my_busnumber, my_busfare, my_buscolor, Route_ID);
                        if (insert == true) {
                            Toast.makeText(EditBusActivity.this, "Edited Bus Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditBusActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditBusActivity.this, "Failed To Edit Bus!", Toast.LENGTH_SHORT).show();
                        }




                }

            }
        });



    }

    public void busGoBack(View view) {
        Intent intent = new Intent(EditBusActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}