package com.mezan.extreme.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.mezan.extreme.BikeMapsActivity;
import com.mezan.extreme.CarMapsActivity;
import com.mezan.extreme.R;
import com.mezan.extreme.UserInterface;

import java.util.ArrayList;

import static com.mezan.extreme.UserInterface.RequestPermissionCode;

public class OrderList extends AppCompatActivity {


    /* GPS Status */
    Context context;
    Intent intent1 ;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    FusedLocationProviderClient fusedLocationClient;
    LinearLayout root;

    /*Normal for this view*/

    ListView orderList;
    Button btnOrderConfirm;

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> priceSingleList = new ArrayList<>();
    ArrayList<String> priceTotalList = new ArrayList<>();
    ArrayList<String> quantityList = new ArrayList<>();

    OrderListAdapter adapter;
    DatabaseReference addcartDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        root = findViewById(R.id.orderListRoot);

        EnableRuntimePermission();



        context = getApplicationContext();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();

        orderList = findViewById(R.id.orderList);
        btnOrderConfirm = findViewById(R.id.btnConfirmOrder);

        adapter = new OrderListAdapter(this,nameList,priceSingleList,priceTotalList,quantityList,0.0);

        orderList.setAdapter(adapter);

        addcartDB = FirebaseDatabase.getInstance().getReference().child("addcart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        final DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference().child("Order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        orderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if (dataSnapshot.child("status").exists()){
                        String status = dataSnapshot.child("status").getValue().toString();
                        if (status.equals("pending")){
                            btnOrderConfirm.setEnabled(false);
                        }else {
                            btnOrderConfirm.setEnabled(true);
                        }
                    }

                }else {
                    btnOrderConfirm.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order();
                //adapter.notifyDataSetChanged();
            }
        });




        addcartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    nameList.clear();
                    priceTotalList.clear();
                    priceSingleList.clear();
                    quantityList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        try {
                            Log.d("Name",child.child("Name").getValue().toString());
                            Log.d("Name",child.child("Price").getValue().toString());
                            Log.d("Name",child.child("totalPrice").getValue().toString());
                            Log.d("Name",child.child("Quantity").getValue().toString());

                            nameList.add(child.child("Name").getValue().toString());
                            priceSingleList.add(child.child("Price").getValue().toString());
                            priceTotalList.add(child.child("totalPrice").getValue().toString());
                            quantityList.add(child.child("Quantity").getValue().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    adapter.notifyDataSetChanged();


                }else {
                    btnOrderConfirm.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Log.d("nameList",nameList.toString());
        Log.d("PriceList",priceSingleList.toString());
        Log.d("PriceList",priceTotalList.toString());
        Log.d("QtyList",quantityList.toString());


    }

    private void order() {
        //Order Process
        btnOrderConfirm.setEnabled(false);
        //Order Pick Address for new Activity
        //startActivity(new Intent(getApplicationContext(),SentRequestForFoodActivity.class));

        //Intent it = new Intent(getApplicationContext(), BikeMapsActivity.class);
        if (isInternetConnection()){
            gettingLocationLatLon();
        }else {
            Snackbar.make(root,"No internet connection!",Snackbar.LENGTH_LONG).show();
        }



    }

    private void gettingLocationLatLon(){
        CheckGpsStatus();

        if(GpsStatus) {
            if (Holder != null) {
                if (ActivityCompat.checkSelfPermission(
                        OrderList.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(OrderList.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                /*This line is main work in location and must include gradle of google play services and also coarse location and internet connection must be enable*/

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(OrderList.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){

                                   // Snackbar.make(root,"(Lat,Lon)=("+location.getLatitude()+","+location.getLongitude()+")",Snackbar.LENGTH_LONG).show();
                                    if(!String.valueOf(location.getLatitude()).equals("") && !String.valueOf(location.getLongitude()).equals("")){




                                            Intent it = new Intent(OrderList.this, BikeMapsActivity.class);
                                            it.putExtra("lat",location.getLatitude());
                                            it.putExtra("lon",location.getLongitude());
                                            it.putExtra("order","food");

                                            startActivity(it);


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
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(OrderList.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Snackbar.make(root,"GPS Permission must be allowed us!",Snackbar.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(OrderList.this,new String[]{
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


}
