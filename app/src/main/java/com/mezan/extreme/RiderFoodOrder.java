package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RiderFoodOrder extends AppCompatActivity {

    DatabaseReference mFoodOrderDB;
    ArrayList<String>keyList = new ArrayList<>();

    ListView refList;

    FoodReqRefAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_food_order);

        refList = findViewById(R.id.orderIDFood);


        adapter = new FoodReqRefAdapter(this,keyList);

        refList.setAdapter(adapter);

        refList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getApplicationContext(),FoodListRiderOrder.class);
                it.putExtra("orderID",keyList.get(i));
                startActivity(it);
            }
        });



        mFoodOrderDB = FirebaseDatabase.getInstance().getReference().child("FoodOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mFoodOrderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    keyList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        Log.d("OrderKEY",key);
                        keyList.add(key);
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
