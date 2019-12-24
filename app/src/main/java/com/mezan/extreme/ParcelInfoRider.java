package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParcelInfoRider extends AppCompatActivity {

    TextView nametxt,picktxt,deliverytxt,statustxt,typetxt,mobiletext;
    Button btnAccept,btnReject;
    String name = "";
    String key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_info_rider);

        nametxt = findViewById(R.id.UserName);
        picktxt = findViewById(R.id.pickRiderParcel);
        deliverytxt = findViewById(R.id.deliveryRiderParcel);
        statustxt = findViewById(R.id.statusRiderParcel);
        typetxt = findViewById(R.id.typeOfParcel);
        mobiletext = findViewById(R.id.UserPhone);

        btnAccept = findViewById(R.id.btnReqParcelAccept);
        btnReject = findViewById(R.id.btnReqParcelReject);

        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if (bd != null){
            try {
                name = bd.getString("name");
                key  = bd.getString("orderID");
                nametxt.setText(name);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if (key != null){
            FindInfoOfParcel(key);
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReqPerform("accepted");
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReqPerform("rejected");
            }
        });

    }

    private void ReqPerform(final String accepted) {


            btnAccept.setEnabled(false);
            btnReject.setEnabled(false);
            if (accepted.equals("accepted")){
                btnAccept.setBackgroundColor(Color.parseColor("#096E22"));

            }else {
                btnReject.setBackgroundColor(Color.parseColor("#5D1203"));
            }

            statustxt.setText(accepted);

        DatabaseReference dbParcelOrder = FirebaseDatabase.getInstance().getReference().child("ParcelOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key);

        dbParcelOrder.child("status").setValue(accepted);
        dbParcelOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("uid").exists()){
                        try {
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            DatabaseReference dbParcel = FirebaseDatabase.getInstance().getReference().child("Parcel").child(uid);
                            dbParcel.child("status").setValue(accepted);

                            DatabaseReference dbUserInfo = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid);
                            dbUserInfo.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot data) {

                                    if (data.exists()){
                                        if (data.child("notificationKey").exists()){
                                            try {
                                                String nkey = data.child("notificationKey").getValue().toString();
                                                new SendNotification("Your parcel request has been "+accepted,"Info",nkey);
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

    private void FindInfoOfParcel(String key) {

        DatabaseReference dbParcelOrder = FirebaseDatabase.getInstance().getReference().child("ParcelOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key);
        dbParcelOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("delivery").exists()){
                        try {
                            String dl = dataSnapshot.child("delivery").getValue().toString();
                            deliverytxt.setText(dl);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (dataSnapshot.child("pick").exists()){
                        try {
                            String dl = dataSnapshot.child("pick").getValue().toString();
                            picktxt.setText(dl);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (dataSnapshot.child("status").exists()){
                        try {
                            String dl = dataSnapshot.child("status").getValue().toString();
                            statustxt.setText(dl);
                            if (dl.equals("pending")){
                                btnAccept.setEnabled(true);
                                btnReject.setEnabled(true);
                            }else {
                                btnAccept.setEnabled(false);
                                btnReject.setEnabled(false);
                                if (dl.equals("accepted")){
                                    btnAccept.setBackgroundColor(Color.parseColor("#096E22"));

                                }else {
                                    btnReject.setBackgroundColor(Color.parseColor("#5D1203"));
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (dataSnapshot.child("type").exists()){
                        try {
                            String dl = dataSnapshot.child("type").getValue().toString();
                            typetxt.setText(dl);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    //uid of user
                    if (dataSnapshot.child("uid").exists()){
                        try {
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            //typetxt.setText(dl);

                            SettingUserPhone(uid);

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

    private void SettingUserPhone(String uid) {
        DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("mobile").exists()){
                        try {
                            String mob = dataSnapshot.child("mobile").getValue().toString();
                                mobiletext.setText(mob);
                        }catch (Exception e)
                        {
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
