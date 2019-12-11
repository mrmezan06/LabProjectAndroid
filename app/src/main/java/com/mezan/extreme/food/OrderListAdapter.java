package com.mezan.extreme.food;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mezan.extreme.R;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {


    Double estimatedPrice;
    Context context;
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> priceSingleList = new ArrayList<>();
    ArrayList<String> priceTotalList = new ArrayList<>();
    ArrayList<String> quantityList = new ArrayList<>();

    public OrderListAdapter(){

    }
    public OrderListAdapter(Context context,ArrayList<String> nameList, ArrayList<String> priceSingleList, ArrayList<String> priceTotalList, ArrayList<String> quantityList,Double totalprice){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {


        view = LayoutInflater.from(context).inflate(R.layout.order_list_layout,viewGroup,false);
        final TextView txtName,txtPrice,txtQty,txtTotalPrice;
        TextView qtyplux,qtyminus;
        final LinearLayout root;

        qtyminus = view.findViewById(R.id.qtyminus);
        qtyplux = view.findViewById(R.id.qtyplus);

        root = view.findViewById(R.id.root_orderList);




        txtName = view.findViewById(R.id.nameOrder);
        txtPrice = view.findViewById(R.id.priceOrder);
        txtQty = view.findViewById(R.id.qtyOrder);
        txtTotalPrice = view.findViewById(R.id.tPriceOrder);

        if (i < nameList.size()){
            txtName.setText(nameList.get(i));
            txtPrice.setText(priceSingleList.get(i)+" ৳");
            qtyminus.setText("-");
            qtyplux.setText("+");
            txtQty.setText(quantityList.get(i)+" Pcs");
            txtTotalPrice.setText(priceTotalList.get(i)+" ৳");
            /*Double tPrice = Double.parseDouble(priceTotalList.get(i));
            estimatedPrice += tPrice;*/



        }else if (i== nameList.size()){
            if (nameList.size()>0){
                txtName.setText("Total Price :");
                txtPrice.setText("");
                txtQty.setText("");
                qtyminus.setText("");
                qtyplux.setText("");
                qtyplux.setEnabled(false);
                qtyminus.setEnabled(false);

                Double mp = 0.0;
                try {

                    for (int j=0;j<priceTotalList.size();j++){
                        mp += Double.parseDouble(priceTotalList.get(j));
                    }
                    txtTotalPrice.setText(String.valueOf(mp)+ " ৳");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }

        qtyplux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference addcartDB = FirebaseDatabase.getInstance().getReference().child("addcart")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(nameList.get(i));

                int cqty = getINTPlus(quantityList.get(i));

                addcartDB.child("Quantity").setValue(String.valueOf(cqty));
                txtQty.setText(String.valueOf(cqty)+"Pcs");
                addcartDB.child("totalPrice").setValue(String.valueOf(cqty*Double.parseDouble(priceSingleList.get(i))));
                txtTotalPrice.setText(String.valueOf(cqty*Double.parseDouble(priceSingleList.get(i)))+" ৳");
            }
        });

        qtyminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference addcartDB = FirebaseDatabase.getInstance().getReference().child("addcart")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(nameList.get(i));

                int cqty = getINTMinus(quantityList.get(i));

                addcartDB.child("Quantity").setValue(String.valueOf(cqty));
                txtQty.setText(String.valueOf(cqty)+"Pcs");
                addcartDB.child("totalPrice").setValue(String.valueOf(cqty*Double.parseDouble(priceSingleList.get(i)))+" ৳");
                txtTotalPrice.setText(String.valueOf(cqty*Double.parseDouble(priceSingleList.get(i))));
                if (cqty == 0){
                    Snackbar.make(root,"No More Decrease item available",Snackbar.LENGTH_LONG).show();
                    if(nameList.isEmpty()){
                        DatabaseReference addUserCartDB = FirebaseDatabase.getInstance().getReference().child("addcart")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        addUserCartDB.setValue(null);
                        nameList.clear();
                        priceSingleList.clear();
                        priceTotalList.clear();
                        quantityList.clear();
                    }else {
                        addcartDB.setValue(null);
                    }

                }
            }
        });


        return view;
    }
    private int getINTPlus(String qty){
        int x = 0;
        try{
            x = Integer.parseInt(qty);
        }catch (Exception e){
            Log.d("Parse Integer error",e.toString());
        }


        return x+1;
    }
    private int getINTMinus(String qty){
        int x = 0;
        try{
            x = Integer.parseInt(qty);
        }catch (Exception e){
            Log.d("Parse Integer error",e.toString());
        }

        if (x>0){
            return x-1;
        }else {

            return 0;
        }


    }
}
