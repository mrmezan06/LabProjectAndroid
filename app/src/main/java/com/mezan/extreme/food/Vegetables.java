package com.mezan.extreme.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.mezan.extreme.R;


public class Vegetables extends Fragment {

    String []name;
    String []description;
    String []price;
    public Vegetables() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vegetables, container, false);

        ListView appetizerList;
        appetizerList = view.findViewById(R.id.vegetableList);

        name = getResources().getStringArray(R.array.VEGETABLE);
        description = getResources().getStringArray(R.array.VEGETABLE_description);
        price = getResources().getStringArray(R.array.VEGETABLE_price);

        ItemListAdapter adapter = new ItemListAdapter(getContext(),name,description,price);
        appetizerList.setAdapter(adapter);

        return view;
    }
}
