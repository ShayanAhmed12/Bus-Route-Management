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

public class AddBusActivity extends AppCompatActivity {

    MainDB DB = new MainDB(this);
    UserDB userdb = new UserDB(this);

    EditText busname, busfare, busnumber;
    Button busaddbutton;

    Spinner buscolor, busroute;

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
        setContentView(R.layout.activity_add_bus);

        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        busname = findViewById(R.id.busName);
        busnumber = findViewById(R.id.busNumber);
        busfare = findViewById(R.id.busFare);
        buscolor = findViewById(R.id.busColor);
        busroute = findViewById(R.id.busRoute);

        busaddbutton = findViewById(R.id.busAddBtn);

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



        busaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String my_busname = busname.getText().toString();
                String my_busnumber = busnumber.getText().toString();
                String my_busfare = busfare.getText().toString();


                if(buscolor.getSelectedItem()!=null && busroute.getSelectedItem()!=null)
                {
                    String my_buscolor = buscolor.getSelectedItem().toString();
                    String my_busroute = busroute.getSelectedItem().toString();


                    if(TextUtils.isEmpty(my_busname) || TextUtils.isEmpty(my_busnumber) || TextUtils.isEmpty(my_busfare) || TextUtils.isEmpty(my_buscolor) || TextUtils.isEmpty(my_busroute))
                    {
                    Toast.makeText(AddBusActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int ID = userdb.getUserID();
                        int Route_ID = DB.getRouteID(my_busroute,String.valueOf(ID));

                        Boolean insert = DB.insertBus(my_busname,my_busnumber ,my_busfare,my_buscolor,Route_ID,ID);
                        if(insert==true)
                        {
                        Toast.makeText(AddBusActivity.this, "Added Bus Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddBusActivity.this, HomeActivity.class);
                        startActivity(intent);
                        }
                        else {
                        Toast.makeText(AddBusActivity.this, "Failed To Add Bus!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else{
                    Toast.makeText(AddBusActivity.this, "Failed To Add Bus!", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    public void busGoBack(View view) {
        Intent intent = new Intent(AddBusActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}