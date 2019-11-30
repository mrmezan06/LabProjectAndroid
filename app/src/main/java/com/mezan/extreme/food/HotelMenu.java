package com.mezan.extreme.food;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import com.mezan.extreme.R;

public class HotelMenu extends AppCompatActivity {

    ListView hotelList;
    String name[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_hotel_menu);

        hotelList=findViewById(R.id.hotelList);

        name = getResources().getStringArray(R.array.hotelName);

        hotelList = findViewById(R.id.hotelList);
        Hotel_Adapter adapter = new Hotel_Adapter(this,name);

        hotelList.setAdapter(adapter);






    }
}
