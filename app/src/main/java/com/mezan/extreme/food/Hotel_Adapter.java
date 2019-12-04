package com.mezan.extreme.food;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mezan.extreme.R;

public class Hotel_Adapter extends BaseAdapter {

    Context context;
    String name[] = new String[25];
    int []image = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,
            R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,
            R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,
            R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s,R.drawable.t};
    float []Rating = {3.5f,4f,5f,4.5f,5f,
            4f,4.5f,5f,4.5f,5f,
            3.5f,4f,5f,4.5f,5f,
            4f,4.5f,5f,4.5f,5f
    };
    Hotel_Adapter(){}
    Hotel_Adapter(Context context,String []name){
        this.context = context;
        this.name = name;
    }

    @Override
    public int getCount() {
        return name.length;
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
    public View getView(final int i, View view, ViewGroup root) {
        view= LayoutInflater.from(context).inflate(R.layout.hotel_list_layout,root,false);
        final TextView nameTxt,deliveryTimeTxt;
        RatingBar hotelRating;
        nameTxt = view.findViewById(R.id.hotelName);
        deliveryTimeTxt = view.findViewById(R.id.hotelDeliveryTime);
        hotelRating = view.findViewById(R.id.hotelRating);
        LinearLayout hotelImage;
        hotelImage = view.findViewById(R.id.hotelImage);



        nameTxt.setText(name[i]);
        deliveryTimeTxt.setText("45\nMIN");
        hotelRating.setRating(Rating[i]);
        hotelRating.setFocusable(false);
        hotelRating.setIsIndicator(true);
        //hotelRating.setEnabled(false);

        hotelImage.setBackgroundResource(image[i]);

        hotelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context.getApplicationContext(), FoodTabMenu.class);
                it.putExtra("index",i);
                context.startActivity(it);
                Toast.makeText(context.getApplicationContext(),name[i],Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

