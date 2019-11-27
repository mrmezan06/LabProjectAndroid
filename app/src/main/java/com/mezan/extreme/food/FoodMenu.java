package com.mezan.extreme.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mezan.extreme.R;

public class FoodMenu extends AppCompatActivity {

    ListView listview;
    String rName[]={"The Lake View Restaurant","Bismillah Food","Shushan Food Corner","Food Festival","Rajkachuri","Muzahid Biryani House","Bonefish Restaurant","Foodsta Restaurant","The Kitchen 1","The Kitchen Kacchi","Fuchka Corner","Dolce Vita","Brit Cafe","Wendy's - Khulna","Kitchen Arabia","Vorta Bari","Rijul Biryani House","Eatery","Kashmiri Biryani and kabab","Ebong Khichuri"};
    String rOpen[]={"Open 11:30 AM - 9:30 PM","Open 11:00 AM - 9:00 PM","Open 11:00 AM - 10:00 PM","Open 11:00 AM - 9:30 PM","Open 11:30 AM - 9:30 PM","Open 11:00 AM - 8:30 PM","Open 12:00 PM - 10:30 PM","Open 11:30 AM - 10:30 PM","Open 12:00 PM - 9:00 PM","Open 12:00 PM - 9:30 PM","Open 1:00 PM - 11:00 PM","Open 1:00 PM - 10:00 PM","Open 12:00 PM - 10:30 PM","Open 1:00 PM - 10:00 PM","Open 12:15 PM - 10:30 PM","Open 10:30 AM - 9:30 PM","Open 11:00 AM - 10:00 PM","Open 11:15 AM - 10:30 PM","Open 10:00 AM - 10:29 PM","Open 10:00 AM - 10:29 PM"};
    String rAddress[]={"38 / C - BR, Shop - 01, Mohsin Mor, Close To Mohsin Mohila College And Daulatpur Railway Station, 9000 Khulna","SHOP 01, PYRAMID CLINIC MARKET GROUND FLOOR, BESIDE DAULATPUR SAHEED MINAR, DAULATPUR, 9100 Khulna","G-19, Navy Welfare Complex Market, Khulna, 9000 Khulna","Shop No. G-13, Navy Foundation Welfare Complex Market, 9000 Khulna","7/5 Rupom Trader, Opposite of Safe n Save, Daulatpur, Khulna, 9100 Khulna","Shop 03, G-17/22, Ground Floor of Mou Market, 9000 Khulna","Gollamari - Sonadanga Bypass Rd, Khulna 9208, Opposite of Khulna University Main Gate., 9208 Khulna","Annah City, 101, M A Bari Street, Boyra Bazar, Khulna, 9000 Khulna","Shaj Shopping Complex, Boyra Bazar, 9200 Khulna","Shaj Shopping Complex (2nd floor),Boyar Bazar, 9100 Khulna","Shonadanga Bypass, 9100 Khulna","150/151, MA Bari RD, Khulna, 9100 Khulna","La Fiesta Food Court, 150-151, M,A Bari Road, Gallamari Road, 9100 Khulna","MA Bari Sharak Gollamari Bypass Road, chill out food court, 9100 Khulna","Shop No. 01, Chillout Food Court, 138 M. A. Bari Road, Sonadanga, 9100 Khulna","A 39 Mojid Soroni, Sonadanga, 9000 Khulna","06 / 01, Gobor Chaka Cross Road (Behind Hamdard Showroom), 9100 Khulna","4th Floor, Fardous Plaza, Majid Sarani Road, Shibbari (Opposite of Khulna Museum), 9100 Khulna","Nirala Residential Area, Khulna, 9100 Khulna","House-115, Road-16, Nirala Residential Area, Khulna, 9100 Khulna"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_food_menu2);

        listview=findViewById(R.id.listview);

        Log.d("Size of Array",String.valueOf(rName.length));
        Log.d("Size of Array",String.valueOf(rOpen.length));
        Log.d("Size of Array",String.valueOf(rAddress.length));
        MyAdapter adapter = new MyAdapter(this,rName,rOpen,rAddress);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("OnClicked",rName[position]);
                if(position==0){
                    startActivity(new Intent(FoodMenu.this, TheLakeview.class));
                }else if (position == 1){
                    startActivity(new Intent(FoodMenu.this, Bismillah.class));
                }else if (position == 3){
                    startActivity(new Intent(FoodMenu.this, Food_Festival.class));
                }else if (position == 12){
                    startActivity(new Intent(FoodMenu.this, Brit_Cafe.class));
                }else if (position == 11){
                    startActivity(new Intent(FoodMenu.this, Dolce_Vita.class));
                }else if (position == 17){
                    startActivity(new Intent(FoodMenu.this, Eatery.class));
                }else if (position == 6){
                    startActivity(new Intent(FoodMenu.this, Bonefish_Restaurant.class));
                }else if (position == 7){
                    startActivity(new Intent(FoodMenu.this, Foodsta_Restaurant.class));
                }else if (position == 8){
                    startActivity(new Intent(FoodMenu.this, The_Kitchen.class));
                }else if (position == 9){
                    startActivity(new Intent(FoodMenu.this, The_Kitchen_Kacchi.class));
                }else if (position == 10){
                    startActivity(new Intent(FoodMenu.this, Fuchka_Corner.class));
                }else if (position == 2){
                    startActivity(new Intent(FoodMenu.this, ShushanFood.class));
                }else if (position == 4){
                    startActivity(new Intent(FoodMenu.this, Rajkachuri.class));
                }else if (position == 5){
                    startActivity(new Intent(FoodMenu.this, Muzahid_Biryani.class));
                }else if (position == 2){
                    startActivity(new Intent(FoodMenu.this, ShushanFood.class));
                }else if (position == 13){
                    startActivity(new Intent(FoodMenu.this, Wendy_Khulna.class));
                }else if (position == 14){
                    startActivity(new Intent(FoodMenu.this, Kitchen_Arabia.class));
                }else if (position == 15){
                    startActivity(new Intent(FoodMenu.this, Vorta_Bari.class));
                }else if (position == 16){
                    startActivity(new Intent(FoodMenu.this, Rijul_Biryani_House.class));
                }else if (position == 18){
                    startActivity(new Intent(FoodMenu.this, Kashmiri_Biryani_kabab.class));
                }else if (position == 19){
                    startActivity(new Intent(FoodMenu.this, Ebong_Khichuri.class));
                }

            }
        });

    }
}
