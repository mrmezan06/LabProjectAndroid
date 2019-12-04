package com.mezan.extreme;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class reqListAdapter extends BaseAdapter {

    //List<CountryObject> Country = new ArrayList<>();
    ArrayList<reqDataObj> dataObj = new ArrayList<>();
    Button reqAccept,reqReject;
    Context context;
    public  reqListAdapter(){

    }
    public reqListAdapter(ArrayList<reqDataObj> dataObj ,Context context){
        this.dataObj = dataObj;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataObj.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup root) {
        view= LayoutInflater.from(context).inflate(R.layout.reqlistlayout,root,false);

        TextView Name,Mobile,Distance,Loc,ReqTime,ReqCategory;

        reqAccept = view.findViewById(R.id.reqAccept);
        reqReject = view.findViewById(R.id.reqReject);

        ReqCategory = view.findViewById(R.id.userReqCategory);
        Name = view.findViewById(R.id.userName);
        Mobile = view.findViewById(R.id.userMobile);
        Distance = view.findViewById(R.id.userDistance);
        Loc = view.findViewById(R.id.userLatLng);
        ReqTime = view.findViewById(R.id.userReqTime);
        //data setup
        Name.setText(dataObj.get(i).getName());
        Mobile.setText(dataObj.get(i).getMobile());
        Distance.setText(dataObj.get(i).getDistance());
        ReqCategory.setText(dataObj.get(i).getCategory());

        String address = setAddress(dataObj.get(i).getLat(),dataObj.get(i).getLon());

        Loc.setText(address);
        ReqTime.setText(dataObj.get(i).getReqTime());

        Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context,"("+dataObj.get(i).getLat()+","+dataObj.get(i).getLon()+")",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context,UserInMapActivity.class);
                intent.putExtra("slat",dataObj.get(i).getLat());
                intent.putExtra("slon",dataObj.get(i).getLon());
                context.startActivity(intent);
            }
        });

        Mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:"+dataObj.get(i).getMobile()));
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(phoneIntent);
                }else {
                    Toast.makeText(context,"Call Permission Required",Toast.LENGTH_LONG).show();
                }

            }
        });

        if (dataObj.get(i).getStatus().equals("Accepted")){
            reqAccept.setBackgroundColor(Color.rgb(0,0,0));
            reqReject.setEnabled(false);
            reqAccept.setEnabled(false);
        }else if (dataObj.get(i).getStatus().equals("Rejected")){
            reqReject.setBackgroundColor(Color.rgb(0,0,0));
            reqReject.setEnabled(false);
            reqAccept.setEnabled(false);
        }
        reqAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestManage(true,i);
            }
        });
        reqReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestManage(false,i);
            }
        });


        return view;
    }
    private String setAddress(String lat, String lon){

        Double latitude = Double.parseDouble(lat);
        Double longitude= Double.parseDouble(lon);

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses.size() > 0) {

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


            Log.d("Address","address:"+address);

            addresses.get(0).getAdminArea();

            return address;
        }

        return "No Local Address Found!";

    }
    private void RequestManage(final boolean accept, int i){
        if (dataObj.get(i).getStatus().equals("Pending")){
        final DatabaseReference reqDB = FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dataObj.get(i).reqID);

        reqAccept.setVisibility(View.GONE);
        reqReject.setVisibility(View.GONE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        reqDB.child("responsetime").setValue(currentDateandTime);

            reqDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        if (dataSnapshot.child("userid").getValue() != null){
                            String UID = dataSnapshot.child("userid").getValue().toString();
                            DatabaseReference UDB = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(UID);
                            UDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        if (dataSnapshot.child("notificationKey").getValue() != null){
                                            String notificationKey = dataSnapshot.child("notificationKey").getValue().toString();
                                            String acceptStr = "";
                                            if (accept){
                                                acceptStr = "accepted!";
                                            }else {
                                                acceptStr = "rejected!";
                                            }
                                            new SendNotification("Your request has been "+acceptStr,"Info",notificationKey);
                                        }
                                    }
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

            if (accept){
               //database accept
                dataObj.get(i).setStatus("Accepted");
                reqDB.child("request").setValue("Accepted");
                dataObj.clear();
            }else {
                //database reject
                dataObj.get(i).setStatus("Rejected");
                reqDB.child("request").setValue("Rejected");
                dataObj.clear();

            }

      }else if (dataObj.get(i).getStatus().equals("Accepted")){
            reqAccept.setBackgroundColor(Color.rgb(0,0,0));
            reqReject.setEnabled(false);
            reqAccept.setEnabled(false);
        }else if (dataObj.get(i).getStatus().equals("Rejected")){
            reqReject.setBackgroundColor(Color.rgb(0,0,0));
            reqReject.setEnabled(false);
            reqAccept.setEnabled(false);
        }

    }

}

