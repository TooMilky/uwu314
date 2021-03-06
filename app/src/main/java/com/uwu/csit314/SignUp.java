package com.uwu.csit314;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SignUp extends AppCompatActivity {

    EditText edtPassword, edtUsername;
    Button btnSignUp;
    DBHandler DB;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signup);
        DB = new DBHandler(this);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
//                String repass = repassword.getText().toString();

                if (user.equals("") || pass.equals(""))
                    Toast.makeText(SignUp.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
//                    if(pass.equals(repass)){
                    Boolean checkuser = DB.checkusername(user);
                    if (checkuser == false) {
                        Boolean insert = DB.insertData(user, pass);
                        if (insert == true) {
                            Toast.makeText(SignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                                startActivity(intent);
                        } else {
                            Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                    }
//                    }else{
//                        Toast.makeText(SignUp.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });

    }
}