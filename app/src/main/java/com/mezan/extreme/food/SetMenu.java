package com.mezan.extreme.food;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezan.extreme.R;


public class SetMenu extends Fragment {

    String []name;
    String []description;
    String []price;
    public SetMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_menu, container, false);

        ListView appetizerList;
        appetizerList = view.findViewById(R.id.setMenuList);

        name = getResources().getStringArray(R.array.SET_MENU);
        description = getResources().getStringArray(R.array.SET_MENU_description);
        price = getResources().getStringArray(R.array.SET_MENU_price);

        ItemListAdapter adapter = new ItemListAdapter(getContext(),name,description,price);
        appetizerList.setAdapter(adapter);

        appetizerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                //already pending order has or not
                final DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference().child("Order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                orderDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            if (dataSnapshot.child("status").exists()){
                                String status = dataSnapshot.child("status").getValue().toString();
                                if (status.equals("pending")){
                                    Toast.makeText(getContext(),"Already have a pending order",Toast.LENGTH_LONG).show();
                                }else {
                                    addtocart(i);
                                }
                            }

                        }else {
                            addtocart(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







            }});

        return view;
    }
    public void addtocart(final int i){
        final DatabaseReference addCartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                /*addCartDB.child("food").setValue(name[i]);
                addCartDB.child("price").setValue(price[i]);
                addCartDB.child("quantity").setValue("1");*/

        addCartDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {

                        if (dataSnapshot.child(name[i]).exists()){
                            String qtyStr = dataSnapshot.child(name[i]).child("Quantity").getValue().toString();
                            int qty = Integer.parseInt(qtyStr) + 1;

                            String priceStr = dataSnapshot.child(name[i]).child("Price").getValue().toString();
                            double price = Double.parseDouble(priceStr);

                            double tprice = price * qty;

                            dataSnapshot.child(name[i]).child("totalPrice").getRef().setValue(String.valueOf(tprice));
                            dataSnapshot.child(name[i]).child("Quantity").getRef().setValue(String.valueOf(qty));
                        }else {
                            dataSnapshot.getRef().child(name[i]).child("Name").setValue(name[i]);
                            dataSnapshot.getRef().child(name[i]).child("Name").setValue(name[i]);
                            dataSnapshot.getRef().child(name[i]).child("Price").setValue(price[i]);
                            dataSnapshot.getRef().child(name[i]).child("totalPrice").setValue(price[i]);
                            dataSnapshot.getRef().child(name[i]).child("Quantity").setValue("1");
                        }
                    }else {
                        try {
                            DatabaseReference newUserAddCartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            newUserAddCartDB.child(name[i]).child("Name").setValue(name[i]);
                            newUserAddCartDB.child(name[i]).child("Name").setValue(name[i]);
                            newUserAddCartDB.child(name[i]).child("Price").setValue(price[i]);
                            newUserAddCartDB.child(name[i]).child("totalPrice").setValue(price[i]);
                            newUserAddCartDB.child(name[i]).child("Quantity").setValue("1");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getContext(),"Item added to the cart",Toast.LENGTH_SHORT).show();
    }
}
