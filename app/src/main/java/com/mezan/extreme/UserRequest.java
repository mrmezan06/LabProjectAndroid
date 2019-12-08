package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserRequest extends AppCompatActivity {

    ListView reqList;
    ArrayList<reqDataObj> infoData = new ArrayList<>();
    reqListAdapter adapter;
    LinearLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);

        reqList = findViewById(R.id.reqList);
        root = findViewById(R.id.userRequestRoot);

        adapter = new reqListAdapter(infoData,this);
        reqList.setAdapter(adapter);

        if (isInternetConnection()){
            fetchReqData();
        }else {
            Snackbar.make(root,"No Internet Connection!",Snackbar.LENGTH_LONG).show();
        }


    }
    private void fetchReqData() {

        final DatabaseReference reqDB = FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reqDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    infoData.clear();

                    for (DataSnapshot ds :  dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        //
                        Log.d("MyUser",key);
                        DatabaseReference infoRef =reqDB.child(key);
                        infoRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = "",lat = "",lon = "";
                                String mobile = "",distance = "",uid = "",reqTime = "",reqid="",category = "",reqStatus = "",pick = "";

                                if (dataSnapshot.child("distance").getValue() != null){
                                    distance = dataSnapshot.child("distance").getValue().toString();
                                }
                                if (dataSnapshot.child("mobile").getValue() != null){
                                    mobile = dataSnapshot.child("mobile").getValue().toString();
                                }
                                if (dataSnapshot.child("reqtime").getValue() != null){
                                    reqTime = dataSnapshot.child("reqtime").getValue().toString();
                                }
                                if (dataSnapshot.child("name").getValue() != null){
                                    name = dataSnapshot.child("name").getValue().toString();
                                }
                                if (dataSnapshot.child("lat").getValue() != null){
                                    lat = dataSnapshot.child("lat").getValue().toString();
                                }
                                if (dataSnapshot.child("lon").getValue() != null){
                                    lon = dataSnapshot.child("lon").getValue().toString();
                                }

                                if (dataSnapshot.child("reqid").getValue() != null){
                                    reqid = dataSnapshot.child("reqid").getValue().toString();
                                }

                                if (dataSnapshot.child("category").getValue() != null){
                                    category = dataSnapshot.child("category").getValue().toString();
                                }

                                if (dataSnapshot.child("request").getValue() != null){
                                    reqStatus = dataSnapshot.child("request").getValue().toString();
                                }
                                if (dataSnapshot.child("pick").getValue() != null){
                                    pick = dataSnapshot.child("pick").getValue().toString();
                                }

                                reqDataObj obj = new reqDataObj(name, mobile, distance, lat, lon, reqTime, uid, reqid, category,reqStatus,pick);
                                infoData.add(obj);

                                Log.d("MyUser",name+mobile+distance+lat+lon+reqTime+uid+reqid+category+pick);
                                adapter.notifyDataSetChanged();

                               /*adapter = new reqListAdapter(infoData,getApplicationContext());
                                reqList.setAdapter(adapter);*/
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean isInternetConnection() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;
        }


    }
}
