package com.example.cathelp.view.fragments;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cathelp.R;
import com.example.cathelp.services.MapService;
import com.example.cathelp.repositories.HomeRepo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsFragment extends Fragment {
    private double latitude = 0, longitude = 0;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;


    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        new HomeRepo().searchEvents(" ");
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

    }
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                latitude = location.getLatitude();
                                                longitude = location.getLongitude();
                                                Log.d("MapService","start");
                                                mapFragment.getMapAsync(new MapService(getActivity(),latitude,longitude));

                                            }
                                        }
                                    });



                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                latitude = location.getLatitude();
                                                longitude = location.getLongitude();
                                                Log.d("MapService","start");
                                                mapFragment.getMapAsync(new MapService(getActivity(),latitude,longitude));

                                            }
                                        }
                                    });


                        } else {


                        }
                    }
            );

   /* @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

    }*/
}