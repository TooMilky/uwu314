package com.uwu.csit314;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uwu.csit314.Common.Common;
import com.uwu.csit314.Model.User;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword, edtUsername;
    Button btnSignIn;
    CheckBox ckbRemember;
    TextView txtForgotPwd;
    DBHandler DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_cart);

        setContentView(R.layout.activity_signin);
        edtUsername =findViewById(R.id.edtUsername);
        edtPassword =findViewById(R.id.edtPassword);
//        edtPhone =findViewById(R.id.edtPhone);
        btnSignIn = findViewById(R.id.btnSignIn);
        ckbRemember =findViewById(R.id.ckbRemember);
        txtForgotPwd = findViewById(R.id.txtForgotPwd);
        DB = new DBHandler(this);

//        Paper.init(this);



//        txtForgotPwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showForgotPwdDialog();
//            }
//        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(SignIn.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if(checkuserpass==true){
                        Toast.makeText(SignIn.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SignIn.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

    }

//    private void showForgotPwdDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Forgot Password");
//        builder.setMessage("Enter your secure code");
//
//        LayoutInflater inflater  = this.getLayoutInflater();
//        View forgot_view =  inflater.inflate(R.layout.forgot_password_layout, null);
//        builder.setView(forgot_view);
//        builder.setIcon(R.drawable.ic_security_black_24dp);
//
//        final MaterialEditText edtPhone = (MaterialEditText)forgot_view.findViewById(R.id.edtPhone);
//        final MaterialEditText edtSecureCode = (MaterialEditText)forgot_view.findViewById(R.id.edtSecureCode);
//
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                table_user.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        User user = dataSnapshot.child(edtPhone.getText()   .toString())
//                                .getValue(User.class);
//
//                        if(user.getSecureCode().equals(edtSecureCode.getText().toString()))
//                            Toast.makeText(SignIn.this, "Your password "+user.getPassword(), Toast.LENGTH_LONG).show();
//                        else {
//                            Toast.makeText(SignIn.this, "Wrong secure code", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        builder.show();
//
//    }
}
