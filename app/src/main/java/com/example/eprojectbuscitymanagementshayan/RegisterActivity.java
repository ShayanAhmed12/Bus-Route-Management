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

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password, passwordconfirm;
    TextView dontHaveAccount;
    Button RegisterBtn;

    UserDB DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register");

        username = findViewById(R.id.RegisterUsername);
        email = findViewById(R.id.RegisterEmail);
        password = findViewById(R.id.RegisterPassword);
        passwordconfirm = findViewById(R.id.RegisterPasswordConfirm);

        dontHaveAccount = findViewById(R.id.dontHaveAccount);
        RegisterBtn = findViewById(R.id.RegisterBtn);

        DB = new UserDB(this);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String my_username = username.getText().toString();
                String my_password = password.getText().toString();
                String my_passwordconfirm = passwordconfirm.getText().toString();
                String my_email = email.getText().toString();

                if(TextUtils.isEmpty(my_username) || TextUtils.isEmpty(my_password) || TextUtils.isEmpty(my_email) || TextUtils.isEmpty(my_passwordconfirm))
                {
                    Toast.makeText(RegisterActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(my_password.equals(my_passwordconfirm))
                    {
                        Boolean checkuser = DB.checkUsername(my_username);
                        if(checkuser==false)
                        {
                            Boolean insert = DB.insertData(my_username,my_password,my_email);
                            if(insert==true)
                            {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Failed To Register!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Username Already Exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Passwords Don't Match!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }

    public void alreadyHaveAccount(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}