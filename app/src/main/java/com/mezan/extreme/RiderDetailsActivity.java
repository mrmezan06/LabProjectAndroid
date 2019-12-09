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

    DatabaseReference mUserDB,mRiderLocDB;
    EditText etName,etAddress,etBOD,etGender,etMobile,etNationality,etCategory,etCreated;
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
        etNationality = findViewById(R.id.detailsPasswordRider);
        etCreated = findViewById(R.id.detailsCreatedRider);
        etCategory = findViewById(R.id.detailsCategoryRider);


        etCreated.setEnabled(false);
        etMobile.setEnabled(false);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            etMobile.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }

        //btn
        btnEditInfo = findViewById(R.id.detailsEditInfoRider);
        btnSaveInfo = findViewById(R.id.detailsSaveRider);


        btnSaveInfo.setVisibility(View.INVISIBLE);
        btnEditInfo.setVisibility(View.VISIBLE);


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

                etBOD.setEnabled(true);
                etGender.setEnabled(true);
                etAddress.setEnabled(true);
                etName.setEnabled(true);
                etNationality.setEnabled(true);
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

        mRiderLocDB = FirebaseDatabase.getInstance().getReference().child("RiderLoc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mUserDB.child("name").setValue(etName.getText().toString());
        mUserDB.child("mobile").setValue(etMobile.getText().toString());
        mUserDB.child("nationality").setValue(etNationality.getText().toString());
        mUserDB.child("address").setValue(etAddress.getText().toString());
        mUserDB.child("gender").setValue(etGender.getText().toString());
        mUserDB.child("bod").setValue(etBOD.getText().toString());
        mUserDB.child("category").setValue(etCategory.getText().toString());

        mRiderLocDB.child("category").setValue(etCategory.getText().toString());


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        mUserDB.child("created").setValue(currentDateandTime);
        etCreated.setEnabled(false);
        etBOD.setEnabled(false);
        etGender.setEnabled(false);
        etAddress.setEnabled(false);
        etName.setEnabled(false);
        etNationality.setEnabled(false);
        etMobile.setEnabled(false);
        etCategory.setEnabled(false);
        btnSaveInfo.setVisibility(View.INVISIBLE);
        btnEditInfo.setVisibility(View.VISIBLE);

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
                    if (dataSnapshot.child("nationality").getValue() != null){
                        String password = ""+ dataSnapshot.child("nationality").getValue();
                        etNationality.setText(password);
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
                        String category = ""+dataSnapshot.child("category").getValue();
                        etCategory.setText(category);
                    }
                    if(dataSnapshot.child("created").getValue() != null){
                        String created = ""+dataSnapshot.child("created").getValue();
                        etCreated.setText(created);
                    }


                    etBOD.setEnabled(false);
                    etGender.setEnabled(false);
                    etAddress.setEnabled(false);
                    etName.setEnabled(false);
                    etNationality.setEnabled(false);
                    etCategory.setEnabled(false);
                    btnEditInfo.setVisibility(View.VISIBLE);
                    btnSaveInfo.setVisibility(View.INVISIBLE);
                }else {

                    etBOD.setEnabled(true);
                    etGender.setEnabled(true);
                    etAddress.setEnabled(true);
                    etName.setEnabled(true);
                    etNationality.setEnabled(true);
                    etCategory.setEnabled(true);
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
