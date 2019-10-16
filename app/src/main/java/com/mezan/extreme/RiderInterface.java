package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.mezan.extreme.UserInterface.RequestPermissionCode;

public class RiderInterface extends AppCompatActivity {


    Context context;
    Intent intent1 ;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    FusedLocationProviderClient fusedLocationClient;
    LinearLayout root;
    TextView riderAddressTXT;

    DatabaseReference mRiderLocDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_interface);

        root = findViewById(R.id.rootRI);
        riderAddressTXT = findViewById(R.id.addressRider);

        EnableRuntimePermission();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        mRiderLocDB = FirebaseDatabase.getInstance().getReference().child("RiderLoc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        CheckGpsStatus();
        if(isInternetConnection()) {
            gettingLocationLatLon();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_interface_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.menuSignOut) {

            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(), ModuleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


            //user don't want to use again again verification code for those sign out
            //so i am just back him in login page

           /* Intent intent=new Intent(getApplicationContext(), LoginForm.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/
            return true;
        }
        if (id == R.id.userDetails){
            Intent intent=new Intent(getApplicationContext(), RiderDetailsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.gpsSetting){
            settingGPS();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void gettingLocationLatLon(){
        CheckGpsStatus();

        if(GpsStatus) {
            if (Holder != null) {
                if (ActivityCompat.checkSelfPermission(
                        RiderInterface.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(RiderInterface.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                /*This line is main work in location and must include gradle of google play services and also coarse location and internet connection must be enable*/

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(RiderInterface.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    Log.d("LocationExtreme", String.valueOf(location.getLongitude()));
                                    Log.d("LocationExtreme",String.valueOf(location.getLatitude()));
                                    Snackbar.make(root,"(Lat,Lon)=("+location.getLatitude()+","+location.getLongitude()+")",Snackbar.LENGTH_LONG).show();
                                    if(!String.valueOf(location.getLatitude()).equals("") && !String.valueOf(location.getLongitude()).equals("")){
                                       /* Intent it = new Intent(RiderInterface.this,BikeMapsActivity.class);
                                        it.putExtra("lat",location.getLatitude());
                                        it.putExtra("lon",location.getLongitude());
                                        startActivity(it);*/


                                            mRiderLocDB.child("lat").setValue(String.valueOf(location.getLatitude()));
                                        mRiderLocDB.child("lon").setValue(String.valueOf(location.getLongitude()));
                                        mRiderLocDB.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        String riderAddress = setAddress(location.getLatitude(),location.getLongitude());
                                        Log.d("RiderAddress",riderAddress);
                                        riderAddressTXT.setText(riderAddress);

                                    }else {
                                        Snackbar.make(root,"Something went wrong!",Snackbar.LENGTH_LONG).show();
                                    }
                                }else {
                                    Log.d("LocationExtreme", "No Location");
                                    Snackbar.make(root,"Location not found!",Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });

                /*Location(Latitude,Longitude)*/


            }
        }else {

            Snackbar.make(root,"GPS Enable First.Then Try Again!",Snackbar.LENGTH_LONG).show();

        }
    }

    String category = "";
    private String checkCategory() {

        DatabaseReference mRiderDB = FirebaseDatabase.getInstance().getReference().child("Rider").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRiderDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if(dataSnapshot.child("category").getValue() != null){
                        category = dataSnapshot.child("category").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return category;
    }

    private void settingGPS(){
        intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent1);
    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(RiderInterface.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Snackbar.make(root,"GPS Permission must be allowed us!",Snackbar.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(RiderInterface.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(root,"GPS Permission Granted!",Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(root,"GPS Permission Cancelled!",Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }
    public boolean isInternetConnection() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;
        }


    }
    private String setAddress(Double latitude, Double longitude){

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses.size() > 0) {

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


            Log.d("Address","address:"+address);

            addresses.get(0).getAdminArea();

            return address;
        }

        return "No Local Address Found!";

    }


}
