package com.mezan.extreme;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;



import com.google.firebase.FirebaseApp;


public class ModuleActivity extends AppCompatActivity {

    CardView userCard,riderCard;
    LinearLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();



        setContentView(R.layout.activity_module);

        FirebaseApp.initializeApp(this);


        userCard = findViewById(R.id.userArea);
        riderCard = findViewById(R.id.riderArea);
        root = findViewById(R.id.rootModule);



        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User Card
                    startActivity(new Intent(ModuleActivity.this, UserLoginActivity.class));
                    finish();
            }
        });

        riderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //register of rider
                    startActivity(new Intent(ModuleActivity.this, RiderRegister.class));
                    finish();

            }
        });
    }


}
