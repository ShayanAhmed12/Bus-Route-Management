package com.example.eprojectbuscitymanagementshayan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextView alreadyHaveAccount;
    Button LoginBtn;

    UserDB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        username = findViewById(R.id.LoginUsername);
        password = findViewById(R.id.LoginPassword);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        LoginBtn = findViewById(R.id.LoginBtn);

        DB = new UserDB(this);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String my_username = username.getText().toString();
                String my_password = password.getText().toString();

                if(TextUtils.isEmpty(my_username) ||TextUtils.isEmpty(my_password))
                {
                    Toast.makeText(LoginActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkuserpass = DB.checkUsernamePassword(my_username,my_password);
                    if(checkuserpass==true)
                    {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Failed To Login!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }

    public void alreadyHaveAccount(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}