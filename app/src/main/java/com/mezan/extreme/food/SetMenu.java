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

        return view;
    }
}
