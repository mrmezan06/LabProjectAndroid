package com.mezan.extreme.food;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mezan.extreme.R;


public class Drinks extends Fragment {

    String []name;
    String []description;
    String []price;
    public Drinks() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);

        ListView appetizerList;
        appetizerList = view.findViewById(R.id.drinksList);

        name = getResources().getStringArray(R.array.Drinks);
        description = getResources().getStringArray(R.array.Drinks_description);
        price = getResources().getStringArray(R.array.Drinks_price);

        ItemListAdapter adapter = new ItemListAdapter(getContext(),name,description,price);
        appetizerList.setAdapter(adapter);

        return view;
    }
}
