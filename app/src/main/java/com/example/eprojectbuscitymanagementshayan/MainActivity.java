package com.example.eprojectbuscitymanagementshayan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    UserDB DB = new UserDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean isloggedin = DB.isLoggedIn();
        Boolean isOldUser = DB.isOldUser();

        getSupportActionBar().hide();
        
        new Handler().postDelayed(new Runnable()
        {

            @Override
                    public void run() {
                if (isloggedin == true) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (isOldUser == true) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }
            }
        },SPLASH_TIME_OUT);




    }


}