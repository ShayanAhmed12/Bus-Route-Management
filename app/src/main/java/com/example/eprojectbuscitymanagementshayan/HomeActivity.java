package com.example.eprojectbuscitymanagementshayan;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    UserDB userdb = new UserDB(this);
    MainDB DB = new MainDB(this);
    ArrayList<RouteData> routeList = new ArrayList<>();

    ArrayList<String> routeID = new ArrayList<>();
    ArrayList<String> routeName = new ArrayList<>();

    ListView routesListView;


    Toolbar toolbar;

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to Logout of this account?");
                builder.setIcon(R.drawable.ic_logout_red);


                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        userdb.logout();
                        Intent logout = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(logout);
                        finish();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle(userdb.getUsername());
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.Home);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.Add:
                        startActivity(new Intent(getApplicationContext(),AllAddActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.Home:
                        return true;
                    case R.id.Favourite:
                        startActivity(new Intent(getApplicationContext(),AllFavActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });








        routesListView = findViewById(R.id.routesList);
        displayRouteData();

        CustomRouteAdapter routeadapter = new CustomRouteAdapter(this,routeList);
        routesListView.setAdapter(routeadapter);
        routeadapter.notifyDataSetChanged();
        registerForContextMenu(routesListView);

        routesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String route_id = routeID.get(i);
                String route_name = routeName.get(i);

                Intent intent = new Intent(HomeActivity.this, ShowBusActivity.class);

                intent.putExtra("route_id", route_id);
                intent.putExtra("route_name", route_name);

                startActivity(intent);


            }
        });



    }


    void displayRouteData(){

        Cursor cursor = DB.getAllRoutes(userdb.getUserID());

        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        else
        {

            while(cursor.moveToNext()){
                routeList.add(new RouteData(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(5)));
                routeID.add(cursor.getString(0));
                routeName.add(cursor.getString(1));
            }
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu,v,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.route_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.contextMenuRouteRemove)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

            builder.setTitle("Delete");
            builder.setMessage("Are you sure you want to remove this Route? Removing this Route will also remove all Buses associated with this Route!");
            builder.setIcon(R.drawable.ic_delete);


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    String ID = String.valueOf(routeID.get(menuInfo.position));

                    DB.deleteRoute(ID);
                    routeList.clear();
                    displayRouteData();
                    CustomRouteAdapter routeadapter = new CustomRouteAdapter(HomeActivity.this,routeList);
                    routesListView.setAdapter(routeadapter);
                    routeadapter.notifyDataSetChanged();

                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();


            return true;
        }

        if(item.getItemId() == R.id.contextMenuRouteEdit)
        {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Intent intent = new Intent(HomeActivity.this, EditRouteActivity.class);

            ArrayList<RouteData> datalist = routeList;
            RouteData data = datalist.get(menuInfo.position);



            intent.putExtra("route_id", data.Rid);
            intent.putExtra("route_name", data.Rname);
            intent.putExtra("route_destination_from", data.Rdestfrom);
            intent.putExtra("route_destination_to", data.Rdestto);


            startActivity(intent);


        }
        return false;
    }





}