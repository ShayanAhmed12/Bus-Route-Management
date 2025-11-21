package com.example.eprojectbuscitymanagementshayan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AllFavActivity extends AppCompatActivity {

    Toolbar toolbar;
    UserDB userdb = new UserDB(this);

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
                Intent intent = new Intent(AllFavActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(AllFavActivity.this);

                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to Logout of this account?");
                builder.setIcon(R.drawable.ic_logout_red);


                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        userdb.logout();
                        Intent logout = new Intent(AllFavActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_all_fav);

        setTitle(userdb.getUsername());
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);



        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.Favourite);


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
                    case R.id.Favourite:
                        return true;
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }


    public void favRoute(View view) {
        Intent intent = new Intent(AllFavActivity.this, FavRouteActivity.class);
        startActivity(intent);
    }

    public void favBus(View view) {
        Intent intent = new Intent(AllFavActivity.this, FavBusActivity.class);
        startActivity(intent);
    }

}