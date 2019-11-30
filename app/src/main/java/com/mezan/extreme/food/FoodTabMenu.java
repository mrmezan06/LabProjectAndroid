package com.mezan.extreme.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.mezan.extreme.R;

public class FoodTabMenu extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_food_tab_menu);

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
