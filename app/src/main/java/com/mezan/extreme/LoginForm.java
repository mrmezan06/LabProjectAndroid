package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginForm extends AppCompatActivity {

    TextInputEditText etMobile;
    TextInputEditText etPassword;
    Button btnLogin;

    TextView txtSignUp;

    DatabaseReference mUserDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        etMobile = findViewById(R.id.etLoginMobile);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignUp = findViewById(R.id.signupTxt);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginForm.this,HomePageActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromFirebase();
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        mUserDB = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        else {
            startActivity(new Intent(LoginForm.this,LoginActivity.class));
        }
        etMobile.setSelection(etMobile.getText().length());
    }

    private void fetchDataFromFirebase() {

        final String mobile = String.valueOf(etMobile.getText());
        final String password = String.valueOf(etPassword.getText());


       // mChatMessagesDB = FirebaseDatabase.getInstance().getReference().child("chat").child(mChatObject.getChatId()).child("messages");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String fMobile = "",fPassword = "";
                    if (dataSnapshot.child("password").getValue() != ""){
                        fPassword = (String) dataSnapshot.child("password").getValue();
                        Log.d("Password",fPassword);
                    }
                    if(dataSnapshot.child("mobile").getValue() != ""){
                        fMobile = dataSnapshot.child("mobile").getValue().toString();
                        Log.d("Mobile",fMobile);

                    }

                    if (mobile.equals(fMobile) && password.equals(fPassword)){
                        startActivity(new Intent(LoginForm.this,UserInterface.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
