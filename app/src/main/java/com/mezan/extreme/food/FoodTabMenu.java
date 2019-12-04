package com.mezan.extreme.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.mezan.extreme.R;

public class FoodTabMenu extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    Button btnOrderList;

    int []image = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,
            R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,
            R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,
            R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s,R.drawable.t};
    float []Rating = {3.5f,4f,5f,4.5f,5f,
            4f,4.5f,5f,4.5f,5f,
            3.5f,4f,5f,4.5f,5f,
            4f,4.5f,5f,4.5f,5f
    };
    String name[] = new String[25];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Full Screen Feature
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_food_tab_menu);

        int positionHotel=0;
        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if (bd != null){
            positionHotel = bd.getInt("index",0);
        }

        name = getResources().getStringArray(R.array.hotelName);

        TextView nameTxt,deliveryTimeTxt;
        RatingBar hotelRating;
        nameTxt = findViewById(R.id.foodHotelName);
        deliveryTimeTxt = findViewById(R.id.foodHotelDeliveryTime);
        hotelRating = findViewById(R.id.foodHotelRating);
        LinearLayout hotelImage;
        hotelImage = findViewById(R.id.foodHotelImage);

        btnOrderList = findViewById(R.id.btnOrderList);

        btnOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodTabMenu.this,OrderList.class);
                startActivity(intent);
                finish();
            }
        });

        //hotel identification
        nameTxt.setText(name[positionHotel]);
        deliveryTimeTxt.setText("45\nMIN");

        hotelRating.setRating(Rating[positionHotel]);
        //hotelRating.setEnabled(false);
        hotelRating.setFocusable(false);
        hotelRating.setIsIndicator(true);

        hotelImage.setBackgroundResource(image[positionHotel]);


        tabLayout= findViewById(R.id.tabLayout);
        viewPager= findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Appetizer"));
        tabLayout.addTab(tabLayout.newTab().setText("Salad"));
        tabLayout.addTab(tabLayout.newTab().setText("Pasta"));
        tabLayout.addTab(tabLayout.newTab().setText("Pizza"));
        tabLayout.addTab(tabLayout.newTab().setText("Burger"));
        tabLayout.addTab(tabLayout.newTab().setText("Chicken"));
        tabLayout.addTab(tabLayout.newTab().setText("Beef"));
        tabLayout.addTab(tabLayout.newTab().setText("Vegetable"));
        tabLayout.addTab(tabLayout.newTab().setText("Rice"));
        tabLayout.addTab(tabLayout.newTab().setText("Set Menu"));
        tabLayout.addTab(tabLayout.newTab().setText("Soup"));
        tabLayout.addTab(tabLayout.newTab().setText("Drinks"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabAdapter adapter = new TabAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
