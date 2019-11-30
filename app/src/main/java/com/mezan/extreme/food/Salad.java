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


public class Salad extends Fragment {

    String []name;
    String []description;
    String []price;
    public Salad() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_salad, container, false);

        ListView appetizerList;
        appetizerList = view.findViewById(R.id.saladList);

        name = getResources().getStringArray(R.array.SALAD);
        description = getResources().getStringArray(R.array.SALAD_description);
        price = getResources().getStringArray(R.array.SALAD_price);

        ItemListAdapter adapter = new ItemListAdapter(getContext(),name,description,price);
        appetizerList.setAdapter(adapter);

        return view;
    }
}
