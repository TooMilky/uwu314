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

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword, edtUsername;
    Button btnSignIn;
    CheckBox ckbRemember;
    TextView txtForgotPwd;

    FirebaseDatabase database;
    DatabaseReference table_user;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

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

//        Paper.init(this);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

//        txtForgotPwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showForgotPwdDialog();
//            }
//        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(SignIn.this, "AAAAAAAAA!", Toast.LENGTH_SHORT).show();

//                if (Common.isConnectedToInternet(getBaseContext())) {

//                    if (ckbRemember.isChecked())
//                    {
//                        Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
//                        Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
//
//                    }
                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Loading...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtUsername.getText().toString()).exists()) {
                                //Get User Information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtUsername.getText().toString()).getValue(User.class);
                                user.setName(edtUsername.getText().toString());
                                // check if password in db is same as input
                                if (user.getPassword().equals(edtPassword.getText().toString())) {

                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                    table_user.removeEventListener(this);
                                } else {
                                    //displaypageerror?
                                    Toast.makeText(SignIn.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User does not exist in Database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                }
//                else{
//                    Toast.makeText(SignIn.this, "Please check your connection", Toast.LENGTH_SHORT).show();
//                    return;
//                }
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