package com.example.cathelp.services;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cathelp.repositories.App;
import com.example.cathelp.repositories.DataBaseMarks;
import com.example.cathelp.R;
import com.example.cathelp.repositories.EventRepo;
import com.example.cathelp.repositories.HomeRepo;
import com.example.cathelp.model.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.app.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private final Context context;

    private HomeRepo homeRepo = new HomeRepo();
    private DataBaseMarks instance = App.getInstance().getDatabase();
    private ArrayList<Event> listMarksEvents = EventRepo.listEvents;

    private double latitude,longitude;



    public MapService(final Context context,double latitude,double longitude) {
        this.context = context;
        this.latitude =  latitude;
        this.longitude = longitude;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude,longitude))
                .zoom(13)
                .bearing(45)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.map_style));


        ArrayList<Event> events =new ArrayList<>( HomeRepo.eventList);

        for (Event event : events) {
            for (Event event1: listMarksEvents) {

                if(event1.getName().equals(event.getName())){

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(),event.getLongitude())).title(event.getName()))
                            .setIcon(getIcon(context, R.drawable.map_pin));
                }
            }
        }
        /*for (Event event : events) {
            for (LinkMark linkMark : listMutable) {

                if(linkMark.getNameEvent().equals(event.getName())){

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(),event.getLongitude())).title(event.getName()))
                            .setIcon(getIcon(context, R.drawable.map_pin));
                }
            }
        }*/

        /*ArrayList<Event> events = (ArrayList<Event>) homeRepo.getEvents().getValue();
        List<Event> eventList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot eventSnapshot : snapshot.getChildren()){


                    Log.d("homeRepo",eventSnapshot+" is snapshot.getChildren()");
                    String eventName = eventSnapshot.child("eventName").getValue().toString();
                    String eventDescription = eventSnapshot.child("description").getValue().toString();
                    String stringPrice = eventSnapshot.child("price").getValue().toString();
                    Double eventPrice = Double.parseDouble(stringPrice);
                    String eventImage = eventSnapshot.child(AccountFragment.PATH_IMAGE).getValue().toString();
                    String eventEid = eventSnapshot.child("eid").getValue().toString();
                    String author  =eventSnapshot.child("author").getValue().toString();
                    String imageName  =eventSnapshot.child("imageName").getValue().toString();
                    String animalType  =eventSnapshot.child("animalType").getValue().toString();
                    String address=eventSnapshot.child("address").getValue().toString();
                    String connection = eventSnapshot.child("connection").getValue().toString();
                    double latitude = Double.parseDouble(eventSnapshot.child("latitude").getValue().toString());
                    double longitude = Double.parseDouble(eventSnapshot.child("longitude").getValue().toString());



                    eventList.add(new Event(eventName,eventDescription,eventEid,author,eventImage,imageName,address,animalType,latitude,longitude,eventPrice,connection));

                    for (LinkMark linkMark : listMutable) {
                        if(linkMark.getNameEvent().equals(eventName)){

                            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(eventName))
                                    .setIcon(getIcon(context, R.drawable.map_pin));
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/





        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                homeRepo.searchEvents(marker.getTitle());
                Event event = homeRepo.searchEvents(marker.getTitle()).get(0);
                BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.getWindow().setContentView(R.layout.dialog_map_layout);
                Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.BOTTOM);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(MATCH_PARENT,WRAP_CONTENT);
                dialog.show();



                TextView textName = dialog.getWindow().findViewById(R.id.nameTextView);
                TextView textAddress = dialog.getWindow().findViewById(R.id.addressTextView);
                TextView textInfo = dialog.getWindow().findViewById(R.id.infoTextView);
                ImageView imageView = dialog.getWindow().findViewById(R.id.imageEvent);

                textName.setText(event.getName());
                textAddress.setText(event.getAddres());
                textInfo.setText(event.getDescription());

                Glide.with(imageView).load(event.getImageUri())
                        .fitCenter()
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(imageView);

                return false;
            }
        });
    }


    /*public List<Event> linkToEvent(List<LinkMark> linkMarks){
        if(linktEventMutable != null){
            linktEventMutable.clear();
        }
        allEventsFromHome = homeRepo.getEvents();
        for (Event event : allEventsFromHome.getValue()) {
            for (LinkMark linkMark : linkMarks) {
                if(event.getName().equals(linkMark.getNameEvent())){
                    linktEventMutable.add(event);
                }
            }

        }

        return linktEventMutable;


    }*/
    /*public List<LinkMark> initMark() {

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<List<LinkMark>> result = es.submit(new Callable<List<LinkMark>>() {
            @Override
            public List<LinkMark> call() throws Exception {
                listMutable = (ArrayList<LinkMark>) instance.marksDao().getAll();

                while (true) {
                    if (listMutable != null) {

                        return listMutable;


                    }
                }


            }
        });

        try {

            return result.get();


        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/
    public BitmapDescriptor getIcon(Context context, int drawableId){
        Drawable drawable = ActivityCompat.getDrawable(context,drawableId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas  =new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
