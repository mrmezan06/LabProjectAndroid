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

public class RiderDetailsActivity extends AppCompatActivity {

    DatabaseReference mUserDB;
    EditText etName,etAddress,etBOD,etGender,etMobile,etPassword,etCategory,etCreated;
    Button btnEditInfo,btnSaveInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_details);

        etName = findViewById(R.id.detailsNameRider);
        etAddress = findViewById(R.id.detailsAddressRider);
        etBOD = findViewById(R.id.detailsBODRider);
        etGender = findViewById(R.id.detailsGenderRider);
        etMobile = findViewById(R.id.detailsMobileRider);
        etPassword = findViewById(R.id.detailsPasswordRider);
        etCreated = findViewById(R.id.detailsCreatedRider);
        etCategory = findViewById(R.id.detailsCategoryRider);

        //btn
        btnEditInfo = findViewById(R.id.detailsEditInfoRider);
        btnSaveInfo = findViewById(R.id.detailsSaveRider);


        btnSaveInfo.setVisibility(View.INVISIBLE);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserDB = FirebaseDatabase.getInstance().getReference().child("Rider").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            fetchDataFromFirebase();

        }
        else {
            startActivity(new Intent(getApplicationContext(),RiderRegister.class));
        }
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCreated.setEnabled(true);
                etBOD.setEnabled(true);
                etGender.setEnabled(true);
                etAddress.setEnabled(true);
                etName.setEnabled(true);
                etPassword.setEnabled(true);
                etMobile.setEnabled(true);
                etCategory.setEnabled(true);
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
        mUserDB.child("category").setValue(etCategory.getText().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        mUserDB.child("created").setValue(currentDateandTime);
        etCreated.setEnabled(false);
        etBOD.setEnabled(false);
        etGender.setEnabled(false);
        etAddress.setEnabled(false);
        etName.setEnabled(false);
        etPassword.setEnabled(false);
        etMobile.setEnabled(false);
        etCategory.setEnabled(false);
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
                    if(dataSnapshot.child("category").getValue() != null){
                        String bod = ""+dataSnapshot.child("category").getValue();
                        etCategory.setText(bod);
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
                    etCategory.setEnabled(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
