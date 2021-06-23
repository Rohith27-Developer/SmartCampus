package com.example.smartservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String TAG = "TrackAct";
    Busesdb buses;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 20f;
    private static final float DEFAULT_LATITUDE = 7.254398f;
    private static final float DEFAULT_LONGITUDE = 80.591114f;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String lat,lon;
    Marker marker;
    String rno;
    private static boolean LocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_track);
        rno=getIntent().getStringExtra("rno");
        getLocationPermission();
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

        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: Getting location");
        //  mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(LocationPermissionGranted){
                db.collection("bustracking").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot:value)
                        {

                            buses= queryDocumentSnapshot.toObject(Busesdb.class);
                            Toast.makeText(TrackActivity.this, ""+buses.getRno(), Toast.LENGTH_SHORT).show();
                            if(buses.getRno().equals(rno)) {
                                String lat = buses.getLat();
                                String lon = buses.getLon();
                                double latitude = Double.parseDouble(lat);
                                double longitude = Double.parseDouble(lon);
                                Toast.makeText(TrackActivity.this, "Now : " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                                moveCamera(new LatLng(latitude, longitude), DEFAULT_ZOOM);
                            }
                        }
                    }
                });


            }
        }catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: SecurityException: "+e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: Moving camera to lat :"+latLng.latitude+"lng :"+latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        if(marker!=null)
        {
            marker.remove();
        }
        marker=mMap.addMarker(markerOptions);
    }

    private void initMap(){
        Log.d(TAG, "initMap: Initializing the map");
        SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(TrackActivity.this);
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
    public void onLocationChanged(@NonNull Location location) {

        Toast.makeText(this, ""+location, Toast.LENGTH_SHORT).show();
        db.collection("bustracking").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:value)
                {
                    buses= queryDocumentSnapshot.toObject(Busesdb.class);
                    if(buses.getRno().equals(rno)) {
                        String lat = buses.getLat();
                        String lon = buses.getLon();
                        double latitude = Double.parseDouble(lat);
                        double longitude = Double.parseDouble(lon);
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(latitude, longitude);
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.addMarker(markerOptions);
                        Toast.makeText(TrackActivity.this, "Now : " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                        moveCamera(new LatLng(latitude, longitude), DEFAULT_ZOOM);
                    }
                }
            }
        });



        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
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
