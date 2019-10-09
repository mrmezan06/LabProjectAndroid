package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    boolean found = fetchUserDataFromFirebase(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (found){
                        startActivity(new Intent(ModuleActivity.this,LoginActivity.class));
                    }else {
                        startActivity(new Intent(ModuleActivity.this,LoginForm.class));
                    }
                }
                else
                    startActivity(new Intent(ModuleActivity.this,LoginActivity.class));
            }
        });

        riderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check need to rider or user
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                {

                    //Login of rider
                    Log.d("RiderID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    boolean found = fetchRiderDataFromFirebase(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (found){
                        //rider already logged in
                        startActivity(new Intent(ModuleActivity.this,RiderRegister.class));

                    }else {
                        //not registered
                        //register of rider
                        startActivity(new Intent(ModuleActivity.this, RiderLogin.class));

                    }
                }
                else
                {
                    //register of rider
                    startActivity(new Intent(ModuleActivity.this, RiderRegister.class));
                }
            }
        });
    }

    boolean acceptedRider = false;
    private boolean fetchRiderDataFromFirebase(final String key) {


        DatabaseReference mriderDB = FirebaseDatabase.getInstance().getReference().child("rider");
        mriderDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    if(dataSnapshot.getKey() != null){
                        if (dataSnapshot.getKey() == key){
                            acceptedRider = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return acceptedRider;

    }
    boolean acceptedUser = false;
    private boolean fetchUserDataFromFirebase(final String key) {


        DatabaseReference mriderDB = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        mriderDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    if(dataSnapshot.getKey() != null){
                        if (dataSnapshot.getKey() == key){
                            acceptedUser = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return acceptedUser;

    }
}
