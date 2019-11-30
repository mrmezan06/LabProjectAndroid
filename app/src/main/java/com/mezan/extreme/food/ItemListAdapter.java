package com.mezan.extreme.food;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mezan.extreme.R;

public class ItemListAdapter extends BaseAdapter {

    //List<CountryObject> Country = new ArrayList<>();
    String []name;
    String []description;
    String []price;
    Context context;
    public  ItemListAdapter(){

    }
    public ItemListAdapter(Context context,String []name ,String []description ,String []price){
        this.name = name;
        this.description = description;
        this.price = price;
        this.context = context;
    }

    @Override
    public int getCount() {
        return name.length;
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
        view= LayoutInflater.from(context).inflate(R.layout.food_item_layout,root,false);
        TextView nameTxt,descriptionTxt,priceTxt;
        nameTxt = view.findViewById(R.id.nameFood);
        descriptionTxt = view.findViewById(R.id.descriptionFood);
        priceTxt = view.findViewById(R.id.priceFood);

        try {
            nameTxt.setText(name[i]);
            descriptionTxt.setText(description[i]);
            priceTxt.setText(price[i]+" ৳");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Out Bound",e.toString());
        }

        return view;
    }
}

