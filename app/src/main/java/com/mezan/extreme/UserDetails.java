package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDetails extends AppCompatActivity {


    DatabaseReference mUserDB,mUserLocDB;
    EditText etName,etAddress,etBOD,etGender,etMobile,etPassword,etCreated;
    Button btnEditInfo,btnSaveInfo;
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
        btnSaveInfo = findViewById(R.id.detailsSave);


        btnSaveInfo.setVisibility(View.INVISIBLE);

        etCreated.setEnabled(false);
        etMobile.setEnabled(false);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserDB = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            fetchDataFromFirebase();

        }
        else {
            startActivity(new Intent(UserDetails.this, UserLoginActivity.class));
        }
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etBOD.setEnabled(true);
                etGender.setEnabled(true);
                etAddress.setEnabled(true);
                etName.setEnabled(true);
                etPassword.setEnabled(true);

                btnSaveInfo.setVisibility(View.VISIBLE);

            }
        });


        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditData();
            }
        });

    }
    private void EditData() {



        mUserDB.child("name").setValue(etName.getText().toString());
        mUserDB.child("mobile").setValue(etMobile.getText().toString());
        mUserDB.child("password").setValue(etPassword.getText().toString());
        mUserDB.child("address").setValue(etAddress.getText().toString());
        mUserDB.child("gender").setValue(etGender.getText().toString());
        mUserDB.child("bod").setValue(etBOD.getText().toString());



        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        mUserDB.child("created").setValue(currentDateandTime);

        etBOD.setEnabled(false);
        etGender.setEnabled(false);
        etAddress.setEnabled(false);
        etName.setEnabled(false);
        etPassword.setEnabled(false);

        btnSaveInfo.setVisibility(View.INVISIBLE);

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
                else {

                    etBOD.setEnabled(true);
                    etGender.setEnabled(true);
                    etAddress.setEnabled(true);
                    etName.setEnabled(true);
                    etPassword.setEnabled(true);

                    btnEditInfo.setVisibility(View.INVISIBLE);
                    btnSaveInfo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
