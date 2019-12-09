package com.mezan.extreme.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezan.extreme.R;

import java.util.ArrayList;

public class OrderList extends AppCompatActivity {

    ListView orderList;
    Button btnOrderConfirm;

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> priceSingleList = new ArrayList<>();
    ArrayList<String> priceTotalList = new ArrayList<>();
    ArrayList<String> quantityList = new ArrayList<>();

    OrderListAdapter adapter;
    DatabaseReference addcartDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderList = findViewById(R.id.orderList);
        btnOrderConfirm = findViewById(R.id.btnConfirmOrder);

        adapter = new OrderListAdapter(this,nameList,priceSingleList,priceTotalList,quantityList,0.0);

        orderList.setAdapter(adapter);

        addcartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        final DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference().child("Order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        orderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if (dataSnapshot.child("status").exists()){
                        String status = dataSnapshot.child("status").getValue().toString();
                        if (status.equals("pending")){
                            btnOrderConfirm.setEnabled(false);
                        }else {
                            btnOrderConfirm.setEnabled(true);
                        }
                    }

                }else {
                    btnOrderConfirm.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order();
                //adapter.notifyDataSetChanged();
            }
        });




        addcartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    nameList.clear();
                    priceTotalList.clear();
                    priceSingleList.clear();
                    quantityList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        try {
                            Log.d("Name",child.child("Name").getValue().toString());
                            Log.d("Name",child.child("Price").getValue().toString());
                            Log.d("Name",child.child("totalPrice").getValue().toString());
                            Log.d("Name",child.child("Quantity").getValue().toString());

                            nameList.add(child.child("Name").getValue().toString());
                            priceSingleList.add(child.child("Price").getValue().toString());
                            priceTotalList.add(child.child("totalPrice").getValue().toString());
                            quantityList.add(child.child("Quantity").getValue().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    adapter.notifyDataSetChanged();


                }else {
                    btnOrderConfirm.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Log.d("nameList",nameList.toString());
        Log.d("PriceList",priceSingleList.toString());
        Log.d("PriceList",priceTotalList.toString());
        Log.d("QtyList",quantityList.toString());


    }

    private void order() {
        //Order Process
        btnOrderConfirm.setEnabled(false);
        //Order Pick Address for new Activity
        startActivity(new Intent(getApplicationContext(),SentRequestForFoodActivity.class));


    }


}
