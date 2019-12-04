package com.mezan.extreme.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mezan.extreme.R;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {


    Double estimatedPrice = 0.0;
    Context context;
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> priceSingleList = new ArrayList<>();
    ArrayList<String> priceTotalList = new ArrayList<>();
    ArrayList<String> quantityList = new ArrayList<>();

    public OrderListAdapter(){

    }
    public OrderListAdapter(Context context,ArrayList<String> nameList, ArrayList<String> priceSingleList, ArrayList<String> priceTotalList, ArrayList<String> quantityList){
        this.context =context;
        this.nameList = nameList;
        this.priceSingleList = priceSingleList;
        this.priceTotalList = priceTotalList;
        this.quantityList = quantityList;
    }

    @Override
    public int getCount() {
        return nameList.size()+1;
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
    public View getView(int i, View view, ViewGroup viewGroup) {


        view = LayoutInflater.from(context).inflate(R.layout.order_list_layout,viewGroup,false);
        TextView txtName,txtPrice,txtQty,txtTotalPrice;
        txtName = view.findViewById(R.id.nameOrder);
        txtPrice = view.findViewById(R.id.priceOrder);
        txtQty = view.findViewById(R.id.qtyOrder);
        txtTotalPrice = view.findViewById(R.id.tPriceOrder);

        if (i < nameList.size()){
            txtName.setText(nameList.get(i));
            txtPrice.setText(priceSingleList.get(i));
            txtQty.setText(quantityList.get(i)+" Pcs");
            txtTotalPrice.setText(priceTotalList.get(i));
            Double tPrice = Double.parseDouble(priceTotalList.get(i));
            estimatedPrice += tPrice;

        }else if (i== nameList.size()){
            txtName.setText("Total Price :");
            txtPrice.setText("");
            txtQty.setText("");
            txtTotalPrice.setText(String.valueOf(estimatedPrice));
        }


        return view;
    }
}
