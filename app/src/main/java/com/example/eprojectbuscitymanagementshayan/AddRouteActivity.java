package com.example.eprojectbuscitymanagementshayan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AddRouteActivity extends AppCompatActivity {

    MainDB DB = new MainDB(this);
    UserDB userdb = new UserDB(this);
    EditText routename, routedest_to, routedest_from;
    Button routeaddbutton;

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
        setContentView(R.layout.activity_add_route);

        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        routename = findViewById(R.id.routeName);
        routedest_to = findViewById(R.id.routeDestinationTo);
        routedest_from = findViewById(R.id.routeDestinationFrom);

        routeaddbutton = findViewById(R.id.routeAddBtn);

        routeaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String my_routename = routename.getText().toString();
                String my_routedest_to = routedest_to.getText().toString();
                String my_routedest_from = routedest_from.getText().toString();

                if(TextUtils.isEmpty(my_routename) || TextUtils.isEmpty(my_routedest_from) || TextUtils.isEmpty(my_routedest_to))
                {
                    Toast.makeText(AddRouteActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else{
                    int ID = userdb.getUserID();
                    String stringID = String.valueOf(userdb.getUserID());
                    Boolean checkroutename = DB.checkRouteName(my_routename,stringID);
                    if(checkroutename==false) {

                        Boolean insert = DB.insertRoute(my_routename, my_routedest_from, my_routedest_to, ID);
                        if (insert == true) {
                            Toast.makeText(AddRouteActivity.this, "Added Route Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddRouteActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddRouteActivity.this, "Failed To Add Route!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddRouteActivity.this, "Route Name Must be Unique!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });



    }

    public void routeGoBack(View view) {
        Intent intent = new Intent(AddRouteActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}