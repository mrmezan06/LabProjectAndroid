package com.mezan.extreme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodReqRefAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> idList = new ArrayList<>();

    FoodReqRefAdapter(){}
    FoodReqRefAdapter(Context context,ArrayList<String> idList){
        this.context = context;
        this.idList = idList;
    }

    @Override
    public int getCount() {
        return idList.size();
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
    public View getView(int i, View view, ViewGroup root) {

        view = LayoutInflater.from(context).inflate(R.layout.food_id_layout,root,false);

        TextView idText = view.findViewById(R.id.refIDFood);

        idText.setText(idList.get(i));


        return view;
    }
}
