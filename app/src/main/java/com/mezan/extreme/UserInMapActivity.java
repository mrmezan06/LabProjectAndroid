package com.mezan.extreme;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserInMapActivity extends AppCompatActivity implements OnMapReadyCallback , TaskLoadedCallback{

    private GoogleMap mMap;


    double ulat=0.0;
    double ulon=0.0;

    double rlat = 0.0;
    double rlon = 0.0;

    DatabaseReference mRiderLocDB;

    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_in_map);

        getDirection = findViewById(R.id.btnGetDirection);


        place1 = new MarkerOptions().position(new LatLng(22.3039, 70.8022)).title(setAddress(22.3039, 70.8022));
        place2 = new MarkerOptions().position(new LatLng(23.0225, 72.5714)).title(setAddress(23.0225, 72.5714));

        Intent it = getIntent();
        Bundle bd = it.getExtras();
        if(bd != null){
            ulat = Double.parseDouble(bd.getString("slat"));
            ulon = Double.parseDouble(bd.getString("slon"));
        }

        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new FetchURL(UserInMapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

               /* if (ulat != 0.0 || ulon != 0.0 || rlat != 0.0 || rlon != 0.0){
                    place1 = new MarkerOptions().position(new LatLng(ulat, ulon)).title(setAddress(ulat,ulon));
                    place2 = new MarkerOptions().position(new LatLng(rlat, rlon)).title(setAddress(rlat,rlon));

                    new FetchURL(UserInMapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

                }*/


            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*String  address = setAddress(ulat,ulon);
        LatLng userLocation = new LatLng(ulat,ulon);
        Marker marker =  mMap.addMarker(new MarkerOptions().position(userLocation).title(address).icon(BitmapDescriptorFactory.fromResource(R.drawable.markeruser)));
        marker.setTag("User Position");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,6f));
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            mRiderLocDB = FirebaseDatabase.getInstance().getReference().child("RiderLoc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            fetchBikerLocation();
        }*/

        mMap.addMarker(place1);
        mMap.addMarker(place2);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(22.7739,71.6673))
                .zoom(7)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);


    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyCC40nAoHLW9EsF3SLMbMXNQoa9NFlvHcU";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    private void fetchBikerLocation() {
        mRiderLocDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){



                    String lat = "",lon = "";

                    if (dataSnapshot.child("lat").getValue() != null){
                        // riderInfoMap.put("lat",dataSnapshot.child("lat").getValue().toString());
                        lat = dataSnapshot.child("lat").getValue().toString();
                    }
                    if (dataSnapshot.child("lon").getValue() != null){
                        // riderInfoMap.put("lon",dataSnapshot.child("lon").getValue().toString());
                        lon = dataSnapshot.child("lon").getValue().toString();
                    }

                        if (!lat.equals("") || !lon.equals("")) {
                            //  Log.d("RiderInfo",lat+lon+uid);
                            riderMarker(lat, lon);
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void riderMarker(String latitude,  String longitude) {

        //  Log.d("RiderInfo",latitude+longitude+UID);

        Double lat = Double.valueOf(latitude);
        Double lon = Double.valueOf(longitude);

        rlat = lat;
        rlon = lon;

        LatLng mLatLng = new LatLng(lat,lon);
        mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(setAddress(lat,lon))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bikemarke)));







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
