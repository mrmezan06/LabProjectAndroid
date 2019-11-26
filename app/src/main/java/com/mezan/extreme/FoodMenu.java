package com.mezan.extreme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class FoodMenu extends AppCompatActivity {

    ListView foodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        foodList = findViewById(R.id.foodItemList);


    }
}
