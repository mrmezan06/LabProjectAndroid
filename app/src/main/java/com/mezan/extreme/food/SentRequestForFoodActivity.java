package com.mezan.extreme.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezan.extreme.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SentRequestForFoodActivity extends AppCompatActivity {

    DatabaseReference mRiderDB;
    ArrayList<String> uidList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();

    Spinner driverSpinner;
    ArrayAdapter adapter;
    String driverName = "";
    String driverUID = "";

    EditText pickAddressET;
    Button btnSOrder;
    LinearLayout root;

    infoObject object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_request_for_food);

        driverSpinner = findViewById(R.id.spinner);

        pickAddressET = findViewById(R.id.addressET);
        btnSOrder = findViewById(R.id.sentOrder);

        root = findViewById(R.id.rootSRF);



        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        driverSpinner.setAdapter(adapter);

        mRiderDB = FirebaseDatabase.getInstance().getReference().child("Rider");

        fetchBiker();



        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                driverName = nameList.get(i);
                driverUID = uidList.get(i);
               // object = new infoObject(nameList.get(i),uidList.get(i));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = pickAddressET.getText().toString();

                if (address.equals("") || address.equals(null)){
                    Snackbar.make(root,"Need an Address!",Snackbar.LENGTH_LONG).show();
                }/*else if (driverUID.equals("")){
                    driverUID = uidList.get(0);
                    //Snackbar.make(root,"Please select a driver!",Snackbar.LENGTH_LONG).show();
                }*/else {
                    driverUID = uidList.get(0);
                    MakeOrder();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Rider").child(driverUID);
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.child("notificationKey").getValue() != null){
                                    new FoodNotification(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),"You have a food request!"+"Pick Address :"+pickAddressET.getText(),dataSnapshot.child("notificationKey").getValue().toString());
                                    Snackbar.make(root,"Request has been sent!",Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


    }

    private void MakeOrder() {

        btnSOrder.setEnabled(false);
        DatabaseReference FoodOrderDB = FirebaseDatabase.getInstance().getReference().child("FoodOrder").child(driverUID);

        //order theke set korte hobe
        //FoodOrderDB.child("orderid").setValue("");
        FoodOrderDB.child("userid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FoodOrderDB.child("status").setValue("pending");

        final DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference().child("Order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        orderDB.child("driver").setValue(driverUID);
        orderDB.child("status").setValue("pending");
        orderDB.child("pick").setValue(pickAddressET.getText().toString());

        final String orderid = orderDB.child("order").push().getKey();

        orderDB.child("orderid").setValue(orderid);
        FoodOrderDB.child("orderid").setValue(orderid);

        //orderDB.child("order").child(orderid);
        DatabaseReference addcartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        addcartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){


                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        try {
                            Log.d("Name",ds.child("Name").getValue().toString());
                            Log.d("Name",ds.child("Price").getValue().toString());
                            Log.d("Name",ds.child("totalPrice").getValue().toString());
                            Log.d("Name",ds.child("Quantity").getValue().toString());

                            orderDB.child("order").child(orderid).child(ds.child("Name").getValue().toString()).child("Name").setValue(ds.child("Name").getValue().toString());
                            orderDB.child("order").child(orderid).child(ds.child("Name").getValue().toString()).child("Name").setValue(ds.child("Price").getValue().toString());
                            orderDB.child("order").child(orderid).child(ds.child("Name").getValue().toString()).child("Name").setValue(ds.child("totalPrice").getValue().toString());
                            orderDB.child("order").child(orderid).child(ds.child("Name").getValue().toString()).child("Name").setValue(ds.child("Quantity").getValue().toString());




                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }


                    dataSnapshot.getRef().setValue(null);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void fetchBiker() {
        mRiderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds :  dataSnapshot.getChildren()){
                        final String key = ds.getKey();
                        Log.d("RiderID",key);
                        DatabaseReference riderRef =FirebaseDatabase.getInstance().getReference().child("Rider").child(key);
                        riderRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot ds) {
                                if (ds.exists()){

                                    //HashMap<String,String> riderInfoMap = new HashMap<>();

                                    String name = "";

                                    if (ds.child("name").getValue() != null){
                                        // riderInfoMap.put("uid",dataSnapshot.child("uid").getValue().toString());
                                        name = ds.child("name").getValue().toString();

                                    }
                                    if (ds.child("category").getValue().toString().equals("bike") || ds.child("category").getValue().toString().equals("Bike")){
                                        // Log.d("RiderInfo",riderInfoMap.toString());
                                        //bikerLocation.add(riderInfoMap);

                                            //key and name
                                            Log.d("UID",key);
                                            Log.d("NAME",name);
                                            nameList.add(name);
                                            uidList.add(key);

                                    }



                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
