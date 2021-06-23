package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DriverMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 20f;
    private static final float DEFAULT_LATITUDE = 7.254398f;
    private static final float DEFAULT_LONGITUDE = 80.591114f;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static boolean LocationPermissionGranted = false;
    private GoogleMap mMap;
    String busno;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_driver_map);
        busno=getIntent().getStringExtra("busno");
        getLocationPermission();
    }
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Requesting permissions");
        String [] permissions = {FINE_LOCATION,COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void initMap(){
        Log.d(TAG, "initMap: Initializing the map");
        SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(DriverMapActivity.this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        LocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length>0){
                    for (int i=0;i<grantResults.length;i++) {
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "onRequestPermissionsResult: Permission denied");
                            LocationPermissionGranted = false;
                            return;
                        }
                    }
                    LocationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    initMap();
                }
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map ready");
        Toast.makeText(this, "Map is ready", Toast.LENGTH_LONG).show();
        mMap = googleMap;
        if (LocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: Getting location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(LocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Found current location");
                            if(location==null){
                                HashMap<String,String> hm=new HashMap<>();
                                hm.put("lat",String.valueOf(DEFAULT_LATITUDE));
                                hm.put("lon",String.valueOf(DEFAULT_LONGITUDE));
                                db.collection("bustracking").document(busno).set(hm);
                                moveCamera(new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE), DEFAULT_ZOOM);
                                Toast.makeText(DriverMapActivity.this, "Default location shown", Toast.LENGTH_SHORT).show();
                            }else {
                                Location currentLocation = (Location) task.getResult();
                                Log.d("test", "onComplete: " + currentLocation);
                                HashMap<String,String> hm=new HashMap<>();
                                hm.put("lat",String.valueOf(currentLocation.getLatitude()));
                                hm.put("lon",String.valueOf(currentLocation.getLongitude()));
                                hm.put("rno",String.valueOf(busno));
                                db.collection("bustracking").document(busno).set(hm);
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }
                        }else{
                            Log.d(TAG, "onComplete: Couldn't find current location");
                            Toast.makeText(DriverMapActivity.this, "Couldn't find current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: SecurityException: "+e.getMessage());
        }

    }
    private void moveCamera(LatLng latLng,float zoom){
        Log.d(TAG, "moveCamera: Moving camera to lat :"+latLng.latitude+"lng :"+latLng.longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
    @Override
    public void onLocationChanged(Location location) {
        HashMap<String,String> hm=new HashMap<>();
        hm.put("lat",String.valueOf(location.getLatitude()));
        hm.put("lon",String.valueOf(location.getLongitude()));
        db.collection("bustracking").add(hm);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}