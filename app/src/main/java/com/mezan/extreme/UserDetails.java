package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {


    DatabaseReference mUserDB;
    EditText etName,etAddress,etBOD,etGender,etMobile,etPassword,etCreated;
    Button btnEditInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        etName = findViewById(R.id.detailsName);
        etAddress = findViewById(R.id.detailsAddress);
        etBOD = findViewById(R.id.detailsBOD);
        etGender = findViewById(R.id.detailsGender);
        etMobile = findViewById(R.id.detailsMobile);
        etPassword = findViewById(R.id.detailsPassword);
        etCreated = findViewById(R.id.detailsCreated);
        //btn
        btnEditInfo = findViewById(R.id.detailsEditInfo);



        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserDB = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            fetchDataFromFirebase();


        }
        else {
            startActivity(new Intent(UserDetails.this,LoginActivity.class));
        }

    }
    private void fetchDataFromFirebase() {


        // mChatMessagesDB = FirebaseDatabase.getInstance().getReference().child("chat").child(mChatObject.getChatId()).child("messages");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.child("name").getValue() != null){
                        String name= ""+dataSnapshot.child("name").getValue();
                        etName.setText(name);
                    }
                    if(dataSnapshot.child("mobile").getValue() != null){
                        String mobile = ""+dataSnapshot.child("mobile").getValue();
                        etMobile.setText(mobile);
                        // Log.d("Mobile",fMobile);

                    }
                    if (dataSnapshot.child("password").getValue() != null){
                        String password = ""+ dataSnapshot.child("password").getValue();
                        etPassword.setText(password);
                        //Log.d("Password",fPassword);
                    }
                    if(dataSnapshot.child("address").getValue() != null){
                        String address = ""+dataSnapshot.child("address").getValue();
                        etAddress.setText(address);
                    }

                    if(dataSnapshot.child("gender").getValue() != null){
                        String Gender = ""+dataSnapshot.child("gender").getValue();
                        etGender.setText(Gender);
                    }
                    if(dataSnapshot.child("bod").getValue() != null){
                        String bod = ""+dataSnapshot.child("bod").getValue();
                        etBOD.setText(bod);
                    }
                    if(dataSnapshot.child("created").getValue() != null){
                       String created = ""+dataSnapshot.child("created").getValue();
                       etCreated.setText(created);
                    }

                    etCreated.setEnabled(false);
                    etBOD.setEnabled(false);
                    etGender.setEnabled(false);
                    etAddress.setEnabled(false);
                    etName.setEnabled(false);
                    etPassword.setEnabled(false);
                    etMobile.setEnabled(false);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
