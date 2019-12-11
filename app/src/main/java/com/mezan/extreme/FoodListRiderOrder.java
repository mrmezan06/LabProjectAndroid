package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodListRiderOrder extends AppCompatActivity {

    String key = "";
    DatabaseReference RiderFoodOrderDB;



    ArrayList<String> foodName = new ArrayList<>();
    ArrayList<String> foodSinglePrice = new ArrayList<>();
    ArrayList<String> foodTotalPrice = new ArrayList<>();
    ArrayList<String> foodQuantity = new ArrayList<>();


    Button btnAccept,btnReject;
    ListView orderList;

    FoodOrderRiderAdapter adapter;

    LinearLayout root;
    TextView pickAddressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_rider_order);

        root = findViewById(R.id.rootFLRO);
        btnAccept = findViewById(R.id.btnReqOrderAccept);
        btnReject = findViewById(R.id.btnReqOrderReject);
        pickAddressText = findViewById(R.id.FLROPick);

        orderList = findViewById(R.id.orderItemList);

        adapter = new FoodOrderRiderAdapter(this,foodName,foodSinglePrice,foodTotalPrice,foodQuantity,0.0);

        orderList.setAdapter(adapter);






        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if (bd != null){
            key = bd.getString("orderID");
            RiderFoodOrderDB = FirebaseDatabase.getInstance().getReference().child("FoodOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key);

            findUserID(RiderFoodOrderDB);

        }else {
            Snackbar.make(root,"Something Went Wrong!",Snackbar.LENGTH_LONG).show();
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RiderFoodOrderDB.child("status").setValue("accepted");
                btnAccept.setEnabled(false);
                btnReject.setEnabled(false);
                btnAccept.setBackgroundColor(Color.rgb(0,0,0));

                setOrderDBStatus("accepted");


            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RiderFoodOrderDB.child("status").setValue("rejected");
                btnAccept.setEnabled(false);
                btnReject.setEnabled(false);
                btnReject.setBackgroundColor(Color.rgb(0,0,0));

                setOrderDBStatus("rejected");
            }
        });


    }

    private void setOrderDBStatus(final String val){
        RiderFoodOrderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){



                    if (dataSnapshot.child("userid").exists()){
                        try {
                            String userID = dataSnapshot.child("userid").getValue().toString();

                            sendNotificationToUser(userID,val);

                            Log.d("FLRO UID",userID);


                            FirebaseDatabase.getInstance().getReference().child("Order").child(userID).child("status").setValue(val);

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

    private void sendNotificationToUser(String userID, final String result) {
        DatabaseReference UDB = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(userID);
        UDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("notificationKey").getValue() != null){
                        try {
                            String notificationKey = dataSnapshot.child("notificationKey").getValue().toString();
                            new SendNotification("Your request has been "+result,"Info",notificationKey);
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

    private void findUserID(DatabaseReference riderFoodOrderDB) {

        riderFoodOrderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if (dataSnapshot.child("status").exists()){
                        try {
                            String status = dataSnapshot.child("status").getValue().toString();
                            if (status.equals("pending")){
                                btnAccept.setEnabled(true);
                                btnReject.setEnabled(true);
                            }else {
                                btnAccept.setEnabled(false);
                                btnReject.setEnabled(false);
                                if (status.equals("accepted")){
                                    btnAccept.setBackgroundColor(Color.rgb(0,0,0));
                                }else {
                                    btnReject.setBackgroundColor(Color.rgb(0,0,0));
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    if (dataSnapshot.child("userid").exists()){
                        try {
                            String userID = dataSnapshot.child("userid").getValue().toString();

                            Log.d("FLRO UID",userID);

                            DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference().child("Order").child(userID);
                            orderDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot orderDS) {
                                    String pickAddress = "";
                                    if (orderDS.exists()){
                                        if (orderDS.child("pick").exists()){
                                            try {
                                                pickAddress = orderDS.child("pick").getValue().toString();
                                                Log.d("FLRO Pick",pickAddress);
                                                pickAddressText.setText(pickAddress);

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }

                                        if (orderDS.child("order").child(key).exists()){


                                                foodName.clear();
                                                foodSinglePrice.clear();
                                                foodTotalPrice.clear();
                                                foodQuantity.clear();
                                              //  DatabaseReference foodItemDB = orderDS.child("order").child(key).getRef();

                                                for (DataSnapshot child : orderDS.child("order").child(key).getChildren()){
                                                    try {
                                                        Log.d("Name",child.child("Name").getValue().toString());
                                                        Log.d("Name",child.child("Price").getValue().toString());
                                                        Log.d("Name",child.child("totalPrice").getValue().toString());
                                                        Log.d("Name",child.child("Quantity").getValue().toString());

                                                        foodName.add(child.child("Name").getValue().toString());
                                                        foodSinglePrice.add(child.child("Price").getValue().toString());
                                                        foodTotalPrice.add(child.child("totalPrice").getValue().toString());
                                                        foodQuantity.add(child.child("Quantity").getValue().toString());
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                }
                                                Log.d("FLRO",foodName.toString());

                                                adapter.notifyDataSetChanged();

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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
