package com.mezan.extreme.food;

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


    TextView driverNameText;

    String driverUID = "";

    EditText pickAddressET;
    Button btnSOrder;
    LinearLayout root;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_request_for_food);



        driverNameText = findViewById(R.id.driverText);
        pickAddressET = findViewById(R.id.addressET);
        btnSOrder = findViewById(R.id.sentOrder);

        root = findViewById(R.id.rootSRF);







        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if (bd != null){
            driverUID = bd.getString("duid");
            if (!driverUID.equals("")){
                mRiderDB = FirebaseDatabase.getInstance().getReference().child("Rider").child(driverUID);
                fetchBiker();
            }else {
                btnSOrder.setEnabled(false);
                Snackbar.make(root,"Something Went wrong!",Snackbar.LENGTH_LONG).show();
            }
        }else {
            btnSOrder.setEnabled(false);
            Snackbar.make(root,"Something Went wrong!",Snackbar.LENGTH_LONG).show();
        }





        btnSOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = pickAddressET.getText().toString();

                if (address.equals("") || address.equals(null)) {
                    Snackbar.make(root, "Need an Address!", Snackbar.LENGTH_LONG).show();
                }else {
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


        final DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference().child("Order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        orderDB.child("driver").setValue(driverUID);
        orderDB.child("status").setValue("pending");
        orderDB.child("pick").setValue(pickAddressET.getText().toString());

        final String orderid = orderDB.child("order").push().getKey();

        orderDB.child("orderid").setValue(orderid);

        FoodOrderDB.child(orderid).child("userid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FoodOrderDB.child(orderid).child("status").setValue("pending");
        FoodOrderDB.child(orderid).child("orderid").setValue(orderid);

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
                            orderDB.child("order").child(orderid).child(ds.child("Name").getValue().toString()).child("Price").setValue(ds.child("Price").getValue().toString());
                            orderDB.child("order").child(orderid).child(ds.child("Name").getValue().toString()).child("totalPrice").setValue(ds.child("totalPrice").getValue().toString());
                            orderDB.child("order").child(orderid).child(ds.child("Name").getValue().toString()).child("Quantity").setValue(ds.child("Quantity").getValue().toString());




                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }


                    dataSnapshot.getRef().setValue(null);
                    btnSOrder.setBackgroundColor(Color.rgb(0,0,0));


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

                                    String name = "";

                                    if (dataSnapshot.child("name").getValue() != null){
                                        // riderInfoMap.put("uid",dataSnapshot.child("uid").getValue().toString());
                                        name = dataSnapshot.child("name").getValue().toString();
                                        try {
                                            driverNameText.setText(name);
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
