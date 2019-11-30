package com.mezan.extreme.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.mezan.extreme.R;


public class Beef extends Fragment {

    String []name;
    String []description;
    String []price;
    public Beef() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beef, container, false);

        ListView appetizerList;
        appetizerList = view.findViewById(R.id.beefList);

        name = getResources().getStringArray(R.array.BEEF);
        description = getResources().getStringArray(R.array.BEEF_description);
        price = getResources().getStringArray(R.array.BEEF_price);

        ItemListAdapter adapter = new ItemListAdapter(getContext(),name,description,price);
        appetizerList.setAdapter(adapter);

        return view;
    }


}
