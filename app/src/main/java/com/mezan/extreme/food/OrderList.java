package com.mezan.extreme.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderList = findViewById(R.id.orderList);
        btnOrderConfirm = findViewById(R.id.btnConfirmOrder);

        adapter = new OrderListAdapter(this,nameList,priceSingleList,priceTotalList,quantityList);

        orderList.setAdapter(adapter);



        DatabaseReference addcartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        addcartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Log.d("Name",child.child("Name").getValue().toString());
                        Log.d("Name",child.child("Price").getValue().toString());
                        Log.d("Name",child.child("totalPrice").getValue().toString());
                        Log.d("Name",child.child("Quantity").getValue().toString());

                        nameList.add(child.child("Name").getValue().toString());
                        priceSingleList.add(child.child("Price").getValue().toString());
                        priceTotalList.add(child.child("totalPrice").getValue().toString());
                        quantityList.add(child.child("Quantity").getValue().toString());
                    }

                    adapter.notifyDataSetChanged();


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


}
