package com.example.lokalizator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.lokalizator.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList <LocationClass> locationsMap;
    ArrayList <LocationClass> locationsMapIntent;
    ArrayList <LocationClass> locationsFiltered;
    ArrayList <LocationClass> locationsFilteredIntent;
    int ratingValue;
    boolean bar,plener,restauracja,inne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationsMap = (ArrayList<LocationClass>) PrefConfig.readListFromPref(this,"LISTA");
        locationsFiltered = (ArrayList<LocationClass>) PrefConfig.readListFromPref(this, "FILTERED");
        if (locationsMap == null)
            locationsMap = new ArrayList<>();
        if (locationsFiltered == null)
            locationsFiltered = locationsMap;
        Button switchToFilter = findViewById(R.id.filterButton);
        switchToFilter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switchActivityToFilter();
            }
        });
        Button switchToForms = findViewById(R.id.addLocationButton);
        switchToForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivityToForm(null);
            }
        });


        Button saveData = findViewById(R.id.saveButton);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefConfig.writeListInPref(getApplicationContext(), locationsMap, "LISTA");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                switchActivityToForm(latLng);
            }
        });
        //locationsMap = new ArrayList<>();

        Intent i = getIntent();

        locationsMapIntent = (ArrayList<LocationClass>) i.getSerializableExtra("lokalizacjeIntent");
        locationsFilteredIntent = (ArrayList<LocationClass>) i.getSerializableExtra("filteredIntent");
        System.out.println("Map z intenta");
        System.out.println(locationsMapIntent);
        System.out.println("Filtered z intenta");
        System.out.println(locationsFilteredIntent);
        boolean deleted = false;
        ratingValue = 0;
        plener = true;
        bar = true;
        restauracja = true;
        inne = true;
        if(i.getSerializableExtra("rating") != null)
            ratingValue = (int) i.getSerializableExtra("rating");
        if(i.getSerializableExtra("plener") != null)
            plener = (boolean) i.getSerializableExtra("plener");
        if(i.getSerializableExtra("bar") != null)
            bar = (boolean) i.getSerializableExtra("bar");
        if(i.getSerializableExtra("restauracja") != null)
            restauracja = (boolean) i.getSerializableExtra("restauracja");
        if(i.getSerializableExtra("inne") != null)
            inne = (boolean) i.getSerializableExtra("inne");
        if (i.getSerializableExtra("deleted") != null)
            deleted = true;
        if(locationsFilteredIntent != null && locationsFilteredIntent.size() > 0) {
            if (deleted){
                locationsFiltered = locationsFilteredIntent;
                System.out.println("LocationsFiltered po zastapieniu przez locationsfilteredintent");
            } else {
//                locationsFiltered.removeAll(locationsFilteredIntent);
//                locationsFiltered.addAll(locationsFilteredIntent);
                locationsFiltered = locationsFilteredIntent;
            }
        }
        System.out.println(locationsFiltered);
        mMap.setMinZoomPreference(11.0f);
        LatLngBounds polandBounds = new LatLngBounds(
                new LatLng(49, 14), // SW bounds
                new LatLng(55, 24)  // NE bounds
        );
        LatLngBounds warsawBounds = new LatLngBounds(
                new LatLng(52.15, 20.85), // SW bounds
                new LatLng(52.35, 21.15)  // NE bounds
        );
        if (locationsMapIntent!=null) {
            if(deleted){
                locationsMap = locationsMapIntent;
                System.out.println("Maps po zastapieniu przez intenta");

            } else {
                locationsMap = locationsMapIntent;
            }
        }
        System.out.println(locationsMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warsawBounds.getCenter(), 10));
        mMap.setLatLngBoundsForCameraTarget(warsawBounds);
        if (locationsFiltered == null || locationsFiltered.size() == 0){
            if (locationsMap != null) {
                for (LocationClass locationClass : locationsMap) {

                    float markerColor = BitmapDescriptorFactory.HUE_RED;
                    switch (locationClass.getType()) {
                        case "PLENER":
                            markerColor = BitmapDescriptorFactory.HUE_GREEN;
                            break;
                        case "RESTAURACJA":
                            markerColor = BitmapDescriptorFactory.HUE_AZURE;
                            break;
                        case "BAR":
                            markerColor = BitmapDescriptorFactory.HUE_CYAN;
                            break;
                        case "INNE":
                            markerColor = BitmapDescriptorFactory.HUE_ROSE;
                            break;
                    }
                    LatLng latLng = new LatLng(locationClass.getxCoordinate(), locationClass.getyCoordinate());
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(locationClass.getTitle())
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                    );
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                }
            }
            else
                locationsMap = new ArrayList<>();
            locationsFiltered = new ArrayList<>();
        }
        else {
            for (LocationClass locationClass : locationsFiltered) {

                float markerColor = BitmapDescriptorFactory.HUE_RED;
                switch (locationClass.getType()) {
                    case "PLENER":
                        markerColor = BitmapDescriptorFactory.HUE_GREEN;
                        break;
                    case "RESTAURACJA":
                        markerColor = BitmapDescriptorFactory.HUE_AZURE;
                        break;
                    case "BAR":
                        markerColor = BitmapDescriptorFactory.HUE_CYAN;
                        break;
                    case "INNE":
                        markerColor = BitmapDescriptorFactory.HUE_ROSE;
                        break;
                }
                LatLng latLng = new LatLng(locationClass.getxCoordinate(), locationClass.getyCoordinate());
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(locationClass.getTitle())
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng latlng = marker.getPosition();
                switchActivityToDetails(latlng);
                return false;
            }
        });
    }



    private void switchActivityToForm(LatLng latlng){

        Intent switchActivitiesIntent = new Intent(this, FormActivity.class);
        switchActivitiesIntent.putExtra("lokalizacjeIntent", locationsMap);
        switchActivitiesIntent.putExtra("filteredIntent",locationsFiltered);
        switchActivitiesIntent.putExtra("editingIntent", false);
        switchActivitiesIntent.putExtra("rating",ratingValue);
        switchActivitiesIntent.putExtra("plener",plener);
        switchActivitiesIntent.putExtra("bar",bar);
        switchActivitiesIntent.putExtra("restauracja",restauracja);
        switchActivitiesIntent.putExtra("inne",inne);
        if (latlng!=null){
            switchActivitiesIntent.putExtra("pojedynczaLokalizacjaIntent",new XYpoint(latlng.latitude, latlng.longitude));

        }
        startActivity(switchActivitiesIntent);
    }


    private void switchActivityToDetails(LatLng latlng){

        Intent switchActivitiesIntent = new Intent(this, DetailsActivity.class);
        switchActivitiesIntent.putExtra("lokalizacjeIntent", locationsMap);
        switchActivitiesIntent.putExtra("filteredIntent",locationsFiltered);
        switchActivitiesIntent.putExtra("rating",ratingValue);
        switchActivitiesIntent.putExtra("plener",plener);
        switchActivitiesIntent.putExtra("bar",bar);
        switchActivitiesIntent.putExtra("restauracja",restauracja);
        switchActivitiesIntent.putExtra("inne",inne);
        if (latlng!=null){
            switchActivitiesIntent.putExtra("pojedynczaLokalizacjaIntent",new XYpoint(latlng.latitude, latlng.longitude));
        }
        startActivity(switchActivitiesIntent);
    }

    private void switchActivityToFilter(){
        Intent switchActivitiesIntent = new Intent(this, FilterActivity.class);
        switchActivitiesIntent.putExtra("lokalizacjeIntent", locationsMap);
        switchActivitiesIntent.putExtra("filteredIntent",locationsFiltered);
        switchActivitiesIntent.putExtra("rating",ratingValue);
        switchActivitiesIntent.putExtra("plener",plener);
        switchActivitiesIntent.putExtra("bar",bar);
        switchActivitiesIntent.putExtra("restauracja",restauracja);
        switchActivitiesIntent.putExtra("inne",inne);
        startActivity(switchActivitiesIntent);
    }
}