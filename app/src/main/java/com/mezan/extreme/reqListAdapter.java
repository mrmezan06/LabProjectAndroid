package com.mezan.extreme;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
        Loc.setText("("+dataObj.get(i).getLat()+","+dataObj.get(i).getLon()+")");
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


        return view;
    }
}

