package com.mezan.extreme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodOrderRiderAdapter extends BaseAdapter {

    Double estimatedPrice;
    Context context;
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> priceSingleList = new ArrayList<>();
    ArrayList<String> priceTotalList = new ArrayList<>();
    ArrayList<String> quantityList = new ArrayList<>();

    public FoodOrderRiderAdapter(){

    }
    public FoodOrderRiderAdapter(Context context,ArrayList<String> nameList, ArrayList<String> priceSingleList, ArrayList<String> priceTotalList, ArrayList<String> quantityList,Double totalprice){
        this.context =context;
        this.nameList = nameList;
        this.priceSingleList = priceSingleList;
        this.priceTotalList = priceTotalList;
        this.quantityList = quantityList;
        this.estimatedPrice = totalprice;
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
    public View getView(int i, View view, ViewGroup root) {

        view = LayoutInflater.from(context).inflate(R.layout.rider_food_req_item,root,false);
        final TextView txtName,txtPrice,txtQty,txtTotalPrice;

        txtName = view.findViewById(R.id.nameFLRO);
        txtPrice = view.findViewById(R.id.priceFLRO);
        txtQty = view.findViewById(R.id.qtyFLRO);
        txtTotalPrice = view.findViewById(R.id.tPriceFLRO);

        if (i < nameList.size()){
            txtName.setText(nameList.get(i));
            txtPrice.setText(priceSingleList.get(i) +" ৳");
            txtQty.setText(quantityList.get(i)+" Pcs");
            txtTotalPrice.setText(priceTotalList.get(i)+" ৳");
            /*Double tPrice = Double.parseDouble(priceTotalList.get(i));
            estimatedPrice += tPrice;*/
        }else{
            if (nameList.size()>0){
                txtName.setText("Total Price :");
                txtPrice.setText("");
                txtQty.setText("");

                double mp = 0.0;
                try {

                    for (int j=0;j<priceTotalList.size();j++){
                        mp += Double.parseDouble(priceTotalList.get(j));
                    }
                    txtTotalPrice.setText(mp +" ৳");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }

        return view;
    }
}
