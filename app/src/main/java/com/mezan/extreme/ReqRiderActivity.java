package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mezan.extreme.UserInterface.RequestPermissionCode;

public class ReqRiderActivity extends AppCompatActivity {

    TextView nameTxt,distanceTxt,mobileTxt,availableTxt;
    Button btnReq;
    LinearLayout root;

    double rlat = 0.0;
    double rlon = 0.0;
    double ulat = 0.0;
    double ulon = 0.0;
    String ruid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_rider);

        root = findViewById(R.id.rootReqRider);

        EnableRuntimePermission();

        nameTxt = findViewById(R.id.driverName);
        distanceTxt = findViewById(R.id.distance);
        mobileTxt = findViewById(R.id.driverMobile);
        availableTxt = findViewById(R.id.driverAvailable);

        btnReq = findViewById(R.id.reqBtn);



        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if(bd != null){
            rlat = bd.getDouble("rlat",0.0);
            rlon = bd.getDouble("rlon",0.0);
            ulat = bd.getDouble("ulat",0.0);
            ulon = bd.getDouble("ulon",0.0);
            ruid = bd.getString("ruid");

        }



        fetchDataRider(ruid);


        final DatabaseReference reqDB = FirebaseDatabase.getInstance().getReference().child("Request").child(ruid).push();
        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mob = mobileTxt.getText().toString();
                if (!mob.equals("") || !mob.equals(null)){
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:"+mob));
                    if (ActivityCompat.checkSelfPermission(ReqRiderActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            EnableRuntimePermission();
                        return;
                    }


                    reqDB.child("userid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());

                    reqDB.child("reqtime").setValue(currentDateandTime);



                    LatLng latLngFrom = new LatLng(ulat,ulon);
                    LatLng latLngTo = new LatLng(rlat,rlon);
                    Double distance = SphericalUtil.computeDistanceBetween(latLngFrom, latLngTo);
                    DecimalFormat format = new DecimalFormat("##.00");
                    String dist = "";
                    if(distance>1000){
                        distance = distance/1000;
                        String val = format.format(distance);
                        dist = val+"Km";
                    }else {
                        String val = format.format(distance);
                        dist = val+"M";
                    }

                    reqDB.child("distance").setValue(dist);
                    reqDB.child("mobile").setValue(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

                    DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    userDb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.child("name").getValue() != null){
                                    reqDB.child("name").setValue(dataSnapshot.child("name").getValue());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference userLocDb = FirebaseDatabase.getInstance().getReference().child("UserLoc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    userLocDb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userLocDBSnap) {

                            String lat = "";
                            String lon = "";
                            if (userLocDBSnap.exists()){
                                if (userLocDBSnap.child("lat").getValue() != null){
                                    lat = userLocDBSnap.child("lat").getValue().toString();
                                    reqDB.child("lat").setValue(lat);
                                }
                                if (userLocDBSnap.child("lon").getValue() != null){
                                    lon = userLocDBSnap.child("lon").getValue().toString();
                                    reqDB.child("lon").setValue(lon);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    startActivity(phoneIntent);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(root,"Call Permission Granted!",Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(root,"Call Permission Cancelled!",Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }

    public  static final int RequestCode  = 2 ;

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ReqRiderActivity.this,
                Manifest.permission.CALL_PHONE))
        {

            ActivityCompat.requestPermissions(ReqRiderActivity.this,new String[]{
                    Manifest.permission.CALL_PHONE}, RequestCode);

        } else {

            Snackbar.make(root,"Call Permission must be allowed us!",Snackbar.LENGTH_LONG).show();
        }
    }

    private void fetchDataRider(String ruid) {

        DatabaseReference riderDB = FirebaseDatabase.getInstance().getReference().child("Rider").child(ruid);

        riderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String availabilty = "";
                    String driver = "";
                    String mobile = "";
                    if (dataSnapshot.child("name").getValue() != null){
                        driver = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("availability").getValue() != null){
                      availabilty = dataSnapshot.child("availability").getValue().toString();
                    }
                    if (dataSnapshot.child("mobile").getValue() != null){
                        mobile = dataSnapshot.child("mobile").getValue().toString();
                    }

                    LatLng latLngFrom = new LatLng(ulat,ulon);
                    LatLng latLngTo = new LatLng(rlat,rlon);

                    Double distance = SphericalUtil.computeDistanceBetween(latLngFrom, latLngTo);

                    DecimalFormat format = new DecimalFormat("##.00");
                    String dist = "";
                            if(distance>1000){
                                distance = distance/1000;
                                String val = format.format(distance);
                                dist = val+"Km";
                            }else {
                                String val = format.format(distance);
                                dist = val+"M";
                            }

                    nameTxt.setText(driver);
                    availableTxt.setText(availabilty);
                    distanceTxt.setText(dist);
                    mobileTxt.setText(mobile);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),UserInterface.class));
    }
}
