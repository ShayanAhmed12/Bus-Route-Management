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

public class EditRouteActivity extends AppCompatActivity {

    MainDB DB = new MainDB(this);
    UserDB userdb = new UserDB(this);
    EditText routename, routedest_to, routedest_from;
    Button routeeditbutton;

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
        setContentView(R.layout.activity_edit_route);

        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        routename = findViewById(R.id.routeName);
        routedest_to = findViewById(R.id.routeDestinationTo);
        routedest_from = findViewById(R.id.routeDestinationFrom);

        routeeditbutton = findViewById(R.id.routeEditBtn);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        String myid = b.getString("route_id");
        String myname = b.getString("route_name");
        String mydestfrom = b.getString("route_destination_from");
        String mymydestto = b.getString("route_destination_to");

        routename.setText(myname);
        routedest_from.setText(mydestfrom);
        routedest_to.setText(mymydestto);

        routeeditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String my_routename = routename.getText().toString();
                String my_routedest_to = routedest_to.getText().toString();
                String my_routedest_from = routedest_from.getText().toString();

                if(TextUtils.isEmpty(my_routename) || TextUtils.isEmpty(my_routedest_from) || TextUtils.isEmpty(my_routedest_to))
                {
                    Toast.makeText(EditRouteActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(my_routename.equals(myname)){

                        Boolean insert = DB.editRoute(myid, my_routename, my_routedest_from, my_routedest_to);
                        if (insert == true) {
                            Toast.makeText(EditRouteActivity.this, "Edited Route Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditRouteActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditRouteActivity.this, "Failed To Edit Route!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        String stringID = String.valueOf(userdb.getUserID());
                        Boolean checkroutename = DB.checkRouteName(my_routename,stringID);

                        if(checkroutename==false) {

                            Boolean insert = DB.editRoute(myid,my_routename, my_routedest_from, my_routedest_to);
                            if (insert == true) {
                                Toast.makeText(EditRouteActivity.this, "Edited Route Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditRouteActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EditRouteActivity.this, "Failed To Edit Route!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(EditRouteActivity.this, "A different Route already has that name!", Toast.LENGTH_SHORT).show();
                        }

                    }


                }

            }
        });



    }

    public void routeGoBack(View view) {
        Intent intent = new Intent(EditRouteActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}