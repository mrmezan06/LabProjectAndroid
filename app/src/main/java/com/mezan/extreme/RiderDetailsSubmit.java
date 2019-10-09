package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RiderDetailsSubmit extends AppCompatActivity {
    TextInputEditText etName,etAddress,etPassword,etConfirmPassword,etBOD;
    RadioGroup rgGender;
    RadioButton genderButton;
    Button btnSignUp;
    TextView txtLoginHere;

    ConstraintLayout root;
    MaterialSpinner catergory ;
    MaterialSpinnerAdapter adapter;

    ArrayList<String> list = new ArrayList<>();

    int selectedCategory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_rider_details_submit);





        etName = findViewById(R.id.RDetName);
        etAddress = findViewById(R.id.RDetAddress);
        etPassword = findViewById(R.id.RDetPassword);
        etConfirmPassword = findViewById(R.id.RDetConfirmPassword);
        etBOD = findViewById(R.id.RDetBirthday);
        rgGender = findViewById(R.id.RDrgGender);

        btnSignUp = findViewById(R.id.RDsignupbtn);
        txtLoginHere = findViewById(R.id.RDlogintxt);

        catergory = findViewById(R.id.rdCategory);



        root = findViewById(R.id.rdRoot);

        list.add("Bike");
        list.add("Car");
        list.add("Food");
        list.add("Parcel");

        adapter = new MaterialSpinnerAdapter(this,list);

        catergory.setAdapter(adapter);

        catergory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedCategory = position;
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserDetails();
            }
        });

        txtLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //Login Form
             startActivity(new Intent(RiderDetailsSubmit.this,RiderLogin.class));
            }
        });


    }

    private void getUserDetails() {
        final String name = String.valueOf(etName.getText());
        final String Address = String.valueOf(etAddress.getText());
        final String Password = String.valueOf(etPassword.getText());
        String ConfirmPassword = String.valueOf(etConfirmPassword.getText());
        final String BOD = String.valueOf(etBOD.getText());
        String genderName = "";
        boolean validation = true;

        //true = female || false = male



        if (name.equals("")){
            validation = false;
            Snackbar.make(root,"Name can't be empty!",Snackbar.LENGTH_LONG).show();
        }
        if (Address.equals("")){
            validation = false;
            Snackbar.make(root,"Address can't be empty!",Snackbar.LENGTH_LONG).show();
        }
        if (Password.equals("")){
            validation = false;
            Snackbar.make(root,"Password can't be empty!",Snackbar.LENGTH_LONG).show();
        }
        if (ConfirmPassword.equals("")){
            validation = false;
            Snackbar.make(root,"Confirm Password can't be empty!",Snackbar.LENGTH_LONG).show();
        }
        if (BOD.equals("")){
            validation = false;
            Snackbar.make(root,"Birth of Date can't be empty!",Snackbar.LENGTH_LONG).show();
        }
        if (!Password.equals(ConfirmPassword)){
            validation = false;
            Snackbar.make(root,"Password doesn't match!",Snackbar.LENGTH_LONG).show();
        }


        int selectID = rgGender.getCheckedRadioButtonId();
        genderButton = findViewById(selectID);
        genderName = genderButton.getText().toString();


        if (validation){



            //All checking done now time to set the data for fire base database
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("Rider").child(user.getUid());
            final String finalGenderName = genderName;
            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("mobile", user.getPhoneNumber());
                        userMap.put("name", name);
                        userMap.put("address", Address);
                        userMap.put("password", Password);
                        userMap.put("bod",BOD);
                        userMap.put("gender", finalGenderName);
                        userMap.put("category",list.get(selectedCategory));

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        userMap.put("created",currentDateandTime);

                        mUserDB.updateChildren(userMap);
                        Snackbar.make(root,"All Data Updated!",Snackbar.LENGTH_LONG).show();
                        startActivity(new Intent(RiderDetailsSubmit.this,RiderInterface.class));
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // mUserDB.updateChildren(mUserDB.updateChildren(userMap))


        }


    }
}
