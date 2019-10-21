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
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class reqListAdapter extends BaseAdapter {

    //List<CountryObject> Country = new ArrayList<>();
    ArrayList<reqDataObj> dataObj = new ArrayList<>();
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

        TextView Name,Mobile,Distance,Loc,ReqTime;
        Name = view.findViewById(R.id.userName);
        Mobile = view.findViewById(R.id.userMobile);
        Distance = view.findViewById(R.id.userDistance);
        Loc = view.findViewById(R.id.userLatLng);
        ReqTime = view.findViewById(R.id.userReqTime);
        //data setup
        Name.setText(dataObj.get(i).getName());
        Mobile.setText(dataObj.get(i).getMobile());
        Distance.setText(dataObj.get(i).getDistance());

        String address = setAddress(dataObj.get(i).getLat(),dataObj.get(i).getLon());

        Loc.setText(address);
        ReqTime.setText(dataObj.get(i).getReqTime());


        Loc.setTextColor(Color.BLUE);

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
}

