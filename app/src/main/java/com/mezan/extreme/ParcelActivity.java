package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ParcelActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String driverID = "";

    EditText pickAddressE,dAddressE;
    Spinner typeSpinner;
    TextView drivername;
    Button sendBtn;
    LinearLayout rootPA;
    DatabaseReference mRiderDB;
    List<String> categories = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);

        rootPA = findViewById(R.id.rootPA);
        pickAddressE = findViewById(R.id.pAddressE);
        dAddressE = findViewById(R.id.dAddressE);
        typeSpinner = findViewById(R.id.typeSpinner);
        drivername = findViewById(R.id.PdriverText);
        sendBtn = findViewById(R.id.sentParcelOrder);

        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if (bd != null){
            driverID = bd.getString("driver");
            if (!driverID.equals("") || driverID != null){
                mRiderDB = FirebaseDatabase.getInstance().getReference().child("Rider").child(driverID);
                fetchBiker();
                checkPendingRequest();
            }else {
                sendBtn.setEnabled(false);
                Snackbar.make(rootPA,"Something went wrong!",Snackbar.LENGTH_LONG).show();
            }
        }else {
            sendBtn.setEnabled(false);
            Snackbar.make(rootPA,"Something Went wrong!",Snackbar.LENGTH_LONG).show();
        }

        initiliazeSpinner();
        typeSpinner.setOnItemSelectedListener(this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBsetValue();
            }
        });
    }

    private void checkPendingRequest() {
        DatabaseReference dbParcel = FirebaseDatabase.getInstance().getReference().child("Parcel").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbParcel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("status").exists()){
                        try {
                            String status = dataSnapshot.child("status").getValue().toString();
                            String  pick = dataSnapshot.child("pick").getValue().toString();
                            String delivery = dataSnapshot.child("delivery").getValue().toString();


                            if (status.equals("pending")){

                                sendBtn.setEnabled(false);
                                sendBtn.setBackgroundColor(Color.parseColor("#780244"));

                                pickAddressE.setText(pick);
                                dAddressE.setText(delivery);

                                pickAddressE.setEnabled(false);
                                dAddressE.setEnabled(false);
                                typeSpinner.setEnabled(false);

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initiliazeSpinner() {

        categories.clear();
        categories.add("Accessories");
        categories.add("Documents");
        categories.add("Gift");
        categories.add("Electronics");
        categories.add("Package");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.type_layout, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        typeSpinner.setAdapter(dataAdapter);



    }



    private void DBsetValue() {
        try {
            String pick = pickAddressE.getText().toString();
            String delivery = dAddressE.getText().toString();

            String type = String.valueOf(typeSpinner.getSelectedItem());

            Log.d("gettingValue",pick+delivery+type);

            if (pick.isEmpty() || delivery.isEmpty() || type.isEmpty() || driverID.isEmpty()){
                Snackbar.make(rootPA,"Need Pick and Delivery Address!",Snackbar.LENGTH_LONG).show();
            }else {
                DatabaseReference parcelDB = FirebaseDatabase.getInstance().getReference().child("Parcel").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                parcelDB.child("status").setValue("pending");
                parcelDB.child("pick").setValue(pick);
                parcelDB.child("delivery").setValue(delivery);
                parcelDB.child("type").setValue(type);
                parcelDB.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                parcelDB.child("did").setValue(driverID);

                DatabaseReference dbParcelOrder = FirebaseDatabase.getInstance().getReference().child("ParcelOrder").child(driverID);
                String key = dbParcelOrder.push().getKey();
                if (key != null){
                    dbParcelOrder.child(key).child("status").setValue("pending");
                    dbParcelOrder.child(key).child("pick").setValue(pick);
                    dbParcelOrder.child(key).child("delivery").setValue(delivery);
                    dbParcelOrder.child(key).child("type").setValue(type);
                    dbParcelOrder.child(key).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    parcelDB.child("key").setValue(key);
                    DatabaseReference dbRider = FirebaseDatabase.getInstance().getReference().child("Rider").child(driverID);
                    dbRider.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.child("notificationKey").exists()){
                                    try {
                                        String notifyID = dataSnapshot.child("notificationKey").getValue().toString();
                                        new SendNotification("Your parcel request has been sent!","Info",notifyID);

                                        sendBtn.setEnabled(false);
                                        sendBtn.setBackgroundColor(Color.parseColor("#780244"));
                                        pickAddressE.setEnabled(false);
                                        dAddressE.setEnabled(false);
                                        typeSpinner.setEnabled(false);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }




            }

        }catch (Exception e){
            e.printStackTrace();
        }








    }
    private void fetchBiker() {
        mRiderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String name = "";

                    if (dataSnapshot.child("name").getValue() != null){
                        // riderInfoMap.put("uid",dataSnapshot.child("uid").getValue().toString());
                        name = dataSnapshot.child("name").getValue().toString();
                        try {
                            drivername.setText(name);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
