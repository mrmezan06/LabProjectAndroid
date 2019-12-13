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
    ArrayList<String>nameList = new ArrayList<>();

    ListView refList;

    FoodReqRefAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_food_order);

        refList = findViewById(R.id.orderIDFood);


        adapter = new FoodReqRefAdapter(this,nameList);

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
                    nameList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        Log.d("OrderKEY",key);
                        String uid = "";
                        if (ds.child("userid").exists()){


                            uid = ds.child("userid").getValue().toString();
                            Log.d("OrderUID",uid);
                            keyList.add(key);
                            DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid);
                            dbUser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot data) {

                                    if (data.exists()){
                                        if (data.child("name").exists()){

                                            String name = data.child("name").getValue().toString();
                                            Log.d("Ordername",name);

                                            nameList.add(name);

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


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
