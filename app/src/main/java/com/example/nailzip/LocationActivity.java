package com.example.nailzip;

import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;


public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String TAG = "LocationActivity";

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    private String shopLocation;
    private String shopName;
    private Geocoder geocoder;
    private Address address;
    private List<Address> addressList;
    private GeoPoint geoPoint;

    private Double lat;
    private Double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        shopName = getIntent().getStringExtra("shopName");
        shopLocation = getIntent().getStringExtra("shopLocation");
        geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(shopLocation,5);
            if(addressList.size() > 0){
                address = addressList.get(0);
//                geoPoint = new GeoPoint(
//                        (int) (address.getLongitude()),
//                        (int) (address.getLatitude())
//                );
                lat = address.getLatitude();
                lon = address.getLongitude();

                Log.d(TAG, "Longitude : " + lat + "/ Latitude : " + lon);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(lat, lon); //구로디지털단지역
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(shopName);
        markerOptions.snippet("네일샵");   // 세부 콘텐츠 내용
        markerOptions.position(location);
        googleMap.addMarker(markerOptions);

//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));  // 줌 설정, 가까이서 보고싶을수록 숫자 크게
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }
}