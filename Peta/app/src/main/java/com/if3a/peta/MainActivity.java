package com.if3a.peta;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.if3a.peta.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityMainBinding binding;
    private GoogleMap mMap;

    private List<Lokasi> restaurantList = new ArrayList<>();
    private List<Lokasi> hospitalList = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    private ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        Boolean fineLocationGranted = result.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION, false
        );
        Boolean coarsetLocationGranted = result.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION, false
        );
        if (fineLocationGranted != null && fineLocationGranted) {

        } else if (coarsetLocationGranted != null && coarsetLocationGranted) {

        } else {

        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        restaurantList.add(new Lokasi("Martabak Bangka Teddi Ariodillah 3", new LatLng(-2.964581635322697, 104.73986038586563)));
        restaurantList.add(new Lokasi("Gado Gado Hj.Asih Muslimah", new LatLng(-2.9624570899613425, 104.7383239786941)));
        restaurantList.add(new Lokasi("Pecel Lele Pak Jo", new LatLng(-2.961789442591493, 104.73752852519526)));
        restaurantList.add(new Lokasi("Nasi Putih Kangen", new LatLng(-2.9616156667052063, 104.73853535940319)));
        restaurantList.add(new Lokasi("Pempek Candy", new LatLng(-2.9613531612741606, 104.73883040238098)));
        restaurantList.add(new Lokasi("Warung Makan Semarang", new LatLng(-2.95988527258651, 104.73942585286359)));
        restaurantList.add(new Lokasi("Restoran Sederhana Basuki Rahmat", new LatLng(-2.9595909579881092, 104.73896618922875)));
        restaurantList.add(new Lokasi("Angkringan Mas Sigit", new LatLng(-2.959355238397014, 104.74010344584553)));
        restaurantList.add(new Lokasi("Pecel Lele & Seafood Apollo", new LatLng(-2.957805648678597, 104.74180966602198)));
        restaurantList.add(new Lokasi("Pecel Lele & Seafood Bang Andri", new LatLng(-2.9566163337524825, 104.743097126327)));

        hospitalList.add(new Lokasi("RSU Hermina Palembang", new LatLng(-2.9555727348266685, 104.74849235251304)));
        hospitalList.add(new Lokasi("RS Bhayangkara Mohamad Hasan Palembang", new LatLng(-2.9581196665830536, 104.737394640056)));
        hospitalList.add(new Lokasi("Central General Hospital Dr.Mohammad Hoesin", new LatLng(-2.9657692041722408, 104.75028544651775)));
        hospitalList.add(new Lokasi("Rumah Sakit Bunda Palembang", new LatLng(-2.9675277146192127, 104.73433300833011)));
        hospitalList.add(new Lokasi("RSU Bunda", new LatLng(-2.966584842714868, 104.73431155067955)));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLngLast = new LatLng(latitude, longitude);

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(latLngLast)
                            .title("Im Here")
                            .snippet("Help Me!"))
                            .showInfoWindow();

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngLast, 17));

                    mMap.addCircle(new CircleOptions()
                            .center(latLngLast)
                            .radius(100)
                            .strokeColor(Color.TRANSPARENT)
                            .fillColor(R.color.purple_500));
                }
            }
        });
        binding.fabHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();

                for (int i = 0; i < hospitalList.size(); i++){
                    mMap.addMarker(new MarkerOptions()
                            .position(hospitalList.get(i).getLatLng())
                            .title(hospitalList.get(i).getNama())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

                            .showInfoWindow();
                }
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalList.get(4).getLatLng(), 4));
            }
        });

        binding.fabRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();

                for (int i = 0; i < restaurantList.size(); i++){
                    mMap.addMarker(new MarkerOptions()
                            .position(restaurantList.get(i).getLatLng())
                            .title(restaurantList.get(i).getNama())
                                    .snippet("Enak dan Wahhhh.. Murah Banget !!!"))
                            .showInfoWindow();
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(restaurantList.get(4).getLatLng(), 17));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        LatLng latLngUser = new LatLng(-2.9483130928394026, 104.77538045835556);
        mMap.addMarker(new MarkerOptions().position(latLngUser).title("My Home"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, 20));

        mMap.addCircle(new CircleOptions()
                .center(latLngUser)
                .radius(100)
                .strokeColor(Color.TRANSPARENT)
                .fillColor(R.color.purple_500));
    }
}