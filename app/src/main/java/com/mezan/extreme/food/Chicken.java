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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezan.extreme.R;


public class Chicken extends Fragment {

    String []name;
    String []description;
    String []price;
    public Chicken() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chicken, container, false);

        ListView appetizerList;
        appetizerList = view.findViewById(R.id.chickenList);

        name = getResources().getStringArray(R.array.CHICKEN);
        description = getResources().getStringArray(R.array.CHICKEN_description);
        price = getResources().getStringArray(R.array.CHICKEN_price);

        ItemListAdapter adapter = new ItemListAdapter(getContext(),name,description,price);
        appetizerList.setAdapter(adapter);

        appetizerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final DatabaseReference addCartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                /*addCartDB.child("food").setValue(name[i]);
                addCartDB.child("price").setValue(price[i]);
                addCartDB.child("quantity").setValue("1");*/

                addCartDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.child(name[i]).exists()) {

                                String qtyStr = dataSnapshot.child(name[i]).child("Quantity").getValue().toString();
                                int qty = Integer.parseInt(qtyStr) + 1;

                                String priceStr = dataSnapshot.child(name[i]).child("Price").getValue().toString();
                                double price = Double.parseDouble(priceStr);

                                double tprice = price * qty;

                                dataSnapshot.child(name[i]).child("totalPrice").getRef().setValue(String.valueOf(tprice));
                                dataSnapshot.child(name[i]).child("Quantity").getRef().setValue(String.valueOf(qty));
                            } else {
                                dataSnapshot.getRef().child(name[i]).child("Name").setValue(name[i]);
                                dataSnapshot.getRef().child(name[i]).child("Name").setValue(name[i]);
                                dataSnapshot.getRef().child(name[i]).child("Price").setValue(price[i]);
                                dataSnapshot.getRef().child(name[i]).child("totalPrice").setValue(price[i]);
                                dataSnapshot.getRef().child(name[i]).child("Quantity").setValue("1");
                            }

                        } else {
                            DatabaseReference newUserAddCartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            newUserAddCartDB.child(name[i]).child("Name").setValue(name[i]);
                            newUserAddCartDB.child(name[i]).child("Name").setValue(name[i]);
                            newUserAddCartDB.child(name[i]).child("Price").setValue(price[i]);
                            newUserAddCartDB.child(name[i]).child("totalPrice").setValue(price[i]);
                            newUserAddCartDB.child(name[i]).child("Quantity").setValue("1");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }});

        return view;
    }
}
