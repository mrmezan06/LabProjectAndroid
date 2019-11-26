package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CarMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double ulat=0.0;
    double ulon=0.0;

    DatabaseReference mRiderLocDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_maps);

        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if(bd != null){
            ulat = bd.getDouble("lat",0.0);
            ulon = bd.getDouble("lon",0.0);
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.carmap);
        mapFragment.getMapAsync(this);
    }

    private void fetchBikerLocation() {
        mRiderLocDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds :  dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        Log.d("RiderID",key);
                        DatabaseReference riderRef =mRiderLocDB.child(key);
                        riderRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    //HashMap<String,String> riderInfoMap = new HashMap<>();

                                    String lat = "",lon = "",uid = "";

                                    if (dataSnapshot.child("lat").getValue() != null){
                                        // riderInfoMap.put("lat",dataSnapshot.child("lat").getValue().toString());
                                        lat = dataSnapshot.child("lat").getValue().toString();
                                    }
                                    if (dataSnapshot.child("lon").getValue() != null){
                                        // riderInfoMap.put("lon",dataSnapshot.child("lon").getValue().toString());
                                        lon = dataSnapshot.child("lon").getValue().toString();
                                    }
                                    if (dataSnapshot.child("uid").getValue() != null){
                                        // riderInfoMap.put("uid",dataSnapshot.child("uid").getValue().toString());
                                        uid = dataSnapshot.child("uid").getValue().toString();
                                    }
                                    if (dataSnapshot.child("category").getValue().toString().equals("car") || dataSnapshot.child("category").getValue().toString().equals("Car")){
                                        // Log.d("RiderInfo",riderInfoMap.toString());
                                        //bikerLocation.add(riderInfoMap);
                                        if (!lat.equals("") || !lon.equals("") || !uid.equals("")) {
                                            //  Log.d("RiderInfo",lat+lon+uid);
                                            riderMarker(lat, lon, uid);
                                        }
                                    }



                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void riderMarker(String latitude,  String longitude, String UID) {

        //  Log.d("RiderInfo",latitude+longitude+UID);

        Double lat = Double.valueOf(latitude);
        Double lon = Double.valueOf(longitude);

        LatLng mLatLng = new LatLng(lat,lon);
        Marker markers = mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(setAddress(lat,lon))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.carmarker)));


        markers.setTag(UID);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

               /* Toast.makeText(getApplicationContext(),marker.getTag().toString(),Toast.LENGTH_LONG).show();
                Log.d("MarkerClick",marker.getTag().toString());
*/
                Intent reqIntent = new Intent(getApplicationContext(),ReqRiderActivity.class);

                //user latlng
                reqIntent.putExtra("ulat",ulat);
                reqIntent.putExtra("ulon",ulon);

                //rider latlng
                reqIntent.putExtra("rlat",marker.getPosition().latitude);
                reqIntent.putExtra("rlon",marker.getPosition().longitude);

                //rider uid
                reqIntent.putExtra("ruid",marker.getTag().toString());
                reqIntent.putExtra("category","Car");
                startActivity(reqIntent);
                finish();

                return false;
            }
        });

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        String  address = setAddress(ulat,ulon);
        LatLng userLocation = new LatLng(ulat,ulon);
        Marker marker =  mMap.addMarker(new MarkerOptions().position(userLocation).title(address).icon(BitmapDescriptorFactory.fromResource(R.drawable.markeruser)));
        marker.setTag("Your Position");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,6f));
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            mRiderLocDB = FirebaseDatabase.getInstance().getReference().child("RiderLoc");

            fetchBikerLocation();
        }

    }
}
