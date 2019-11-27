package com.mezan.extreme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mezan.extreme.food.FoodMenu;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserInterface extends AppCompatActivity {


    public  static final int RequestPermissionCode  = 1 ;

    Context context;
    Intent intent1 ;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    FusedLocationProviderClient fusedLocationClient;

    CardView bikeCard,carCard,foodCard,parcelCard;
    LinearLayout root;

    DatabaseReference mUserLocDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);



        EnableRuntimePermission();

       //Card
        bikeCard = findViewById(R.id.bikeCard);
        carCard = findViewById(R.id.carCard);
        foodCard = findViewById(R.id.foodCard);
        parcelCard = findViewById(R.id.parcelCard);
        root = findViewById(R.id.rootUserInterface);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();

        mUserLocDB = FirebaseDatabase.getInstance().getReference().child("UserLoc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Card OnClick Listener
        bikeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetConnection())
                gettingLocationLatLon("bike");
                else
                    Snackbar.make(root,"No Internet Available",Snackbar.LENGTH_LONG).show();

            }
        });
        carCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetConnection())
                    gettingLocationLatLon("car");
                else
                    Snackbar.make(root,"No Internet Available",Snackbar.LENGTH_LONG).show();

            }
        });
        foodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetConnection())
                    gettingLocationLatLon("food");
                else
                    Snackbar.make(root,"No Internet Available",Snackbar.LENGTH_LONG).show();

            }
        });
        parcelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetConnection())
                    gettingLocationLatLon("parcel");
                else
                    Snackbar.make(root,"No Internet Available",Snackbar.LENGTH_LONG).show();

            }
        });


    }
    private void gettingLocationLatLon(final String type){
        CheckGpsStatus();

        if(GpsStatus) {
            if (Holder != null) {
                if (ActivityCompat.checkSelfPermission(
                        UserInterface.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(UserInterface.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                /*This line is main work in location and must include gradle of google play services and also coarse location and internet connection must be enable*/

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(UserInterface.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){

                                    Snackbar.make(root,"(Lat,Lon)=("+location.getLatitude()+","+location.getLongitude()+")",Snackbar.LENGTH_LONG).show();
                                    if(!String.valueOf(location.getLatitude()).equals("") && !String.valueOf(location.getLongitude()).equals("")){

                                        mUserLocDB.child("lat").setValue(String.valueOf(location.getLatitude()));
                                        mUserLocDB.child("lon").setValue(String.valueOf(location.getLongitude()));
                                        mUserLocDB.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        String riderAddress = setAddress(location.getLatitude(),location.getLongitude());
                                        Log.d("RiderAddress",riderAddress);
                                        if(type.equals("bike")){
                                            //Bike Map
                                            Intent it = new Intent(UserInterface.this,BikeMapsActivity.class);
                                            it.putExtra("lat",location.getLatitude());
                                            it.putExtra("lon",location.getLongitude());
                                            startActivity(it);
                                        }else if (type.equals("food")){
                                            //food
                                            Intent it = new Intent(UserInterface.this, FoodMenu.class);
                                            it.putExtra("lat",location.getLatitude());
                                            it.putExtra("lon",location.getLongitude());
                                            startActivity(it);
                                        }else if (type.equals("car")){
                                            //car

                                            Intent it = new Intent(UserInterface.this,CarMapsActivity.class);
                                            it.putExtra("lat",location.getLatitude());
                                            it.putExtra("lon",location.getLongitude());
                                            startActivity(it);
                                        }else if (type.equals("parcel")){
                                            //parcel
                                        }else {
                                            //something went wrong
                                        }

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
    private void settingGPS(){
        intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent1);
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
            Intent intent=new Intent(getApplicationContext(), UserDetails.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.gpsSetting){
            settingGPS();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(UserInterface.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Snackbar.make(root,"GPS Permission must be allowed us!",Snackbar.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(UserInterface.this,new String[]{
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
