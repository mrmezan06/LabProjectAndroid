package com.mezan.extreme.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mezan.extreme.R;


public class MyAdapter extends BaseAdapter {
    String name[] = new String[20],open[]= new String[20],Address[]=new String[20];
    Context context;

    MyAdapter(){}
    MyAdapter(Context context,String name[],String open[],String Address[]){
        this.name = name;
        this.open = open;
        this.Address = Address;
        this.context = context;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row,parent,false);

        TextView nametxt,opentxt,addresstxt;
        nametxt = convertView.findViewById(R.id.textview1);
        opentxt = convertView.findViewById(R.id.textview2);
        addresstxt = convertView.findViewById(R.id.textview3);

        nametxt.setText(name[position]);
        opentxt.setText(open[position]);
        addresstxt.setText(Address[position]);
        return convertView;
    }
}

