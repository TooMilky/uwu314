package com.uwu.csit314;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uwu.csit314.Model.User;

import androidx.appcompat.app.AppCompatActivity;


public class SignUp extends AppCompatActivity {

    EditText edtPassword, edtUsername;
    Button btnSignUp;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signup);

        edtUsername =findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please Waiting...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtUsername.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone Number already register", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtUsername.getText().toString(), edtPassword.getText().toString() );
                                table_user.child(edtUsername.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign Up successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                }
//                else {
//                    Toast.makeText(SignUp.this, "Please check your connection", Toast.LENGTH_SHORT).show();
//                    return;
//                }
            }
        });
    }
}