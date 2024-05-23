package com.example.cathelp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cathelp.view.fragments.AccountFragment;
import com.example.cathelp.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HomeRepo {
    private static DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");
    public static MutableLiveData<List<Event>> mutableEventList = new MutableLiveData<>();
    public static ArrayList<Event> eventList = new ArrayList<>();
    public static ArrayList<Event> searchList = new ArrayList<>();

    private static MutableLiveData<List<Event>> filterEventList = new MutableLiveData<>();



    public LiveData<List<Event>> getEvents(){


        /*if (mutableEventList == null){
            mutableEventList = new MutableLiveData<>();
            loadEvents();
        }
        return mutableEventList;*/
        if(filterEventList == null){
            loadEvents();

        }
        mutableEventList.setValue(new ArrayList<>(eventList));

        return filterEventList;
    }

    public List<Event> filterEvents(String category){
        Log.d("TAG categ",category);
        List<Event> filterList = new ArrayList<>();
        List<Event> allEvents = new ArrayList<>(eventList);


        if(!category.equals("no")){

            for (Event event : allEvents) {

                if(event.getAnimalType().equals(category)){

                    filterList.add(event);
                }
            }

            filterEventList.setValue(filterList);
            return filterList;
        }else {
            filterEventList.setValue(allEvents);
            return allEvents;
        }

    }

    public List<Event> searchEvents(String search){
        searchList.clear();
        //List<Event> eventList = new ArrayList<>();
        if(search.isEmpty() || search.equals(" ")){
            filterEventList.setValue(new ArrayList<>(eventList));
            return new ArrayList<>(searchList);
        }

        if (eventList.size()!=0){
            for (Event event : eventList) {
                if(event.getName().toLowerCase().contains(search.toLowerCase()) || event.getDescription().toLowerCase().contains(search.toLowerCase())) {

                    searchList.add(event);

                }

            }
            filterEventList.setValue(new ArrayList<>(searchList));
        }else{
            filterEventList.setValue(new ArrayList<>(eventList));
        /*eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot eventSnapshot : snapshot.getChildren()){


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

                    if(eventName.toLowerCase().contains(search.toLowerCase()) || eventDescription.toLowerCase().contains(search.toLowerCase())) {


                        eventList.add(new Event(eventName, eventDescription, eventEid, author, eventImage, imageName, address, animalType, latitude, longitude, eventPrice,connection));
                        Log.d("HomeRepo","eventList is "+eventList);
                        filterEventList.postValue(new ArrayList<>(eventList));
                    }



                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/}
        //mutableEventList.setValue(eventList);


        return new ArrayList<>(searchList);



    }


    public static synchronized void loadEvents() {



        //eventList = new ArrayList<>();
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                Log.d("loadEvents","clear"+eventList+" ");
                Log.d("loadEvents",snapshot+" is snapshot");
                for(DataSnapshot eventSnapshot : snapshot.getChildren()){


                    //Log.d("homeRepo",eventSnapshot+" is snapshot.getChildren()");
                    String eventName = eventSnapshot.child("eventName").getValue().toString();
                    String eventDescription = eventSnapshot.child("description").getValue().toString();
                    String stringPrice = eventSnapshot.child("price").getValue().toString();
                    Double eventPrice = Double.parseDouble(stringPrice);
                    String eventImage = eventSnapshot.child(AccountFragment.PATH_IMAGE).getValue().toString();
                    String eventEid = eventSnapshot.child("eid").getValue().toString();
                    String author  =eventSnapshot.child("author").getValue().toString();
                    String imageName  =eventSnapshot.child("imageName").getValue().toString();
                    String address=eventSnapshot.child("address").getValue().toString();
                    String animalType=eventSnapshot.child("animalType").getValue().toString();
                    double latitude = Double.parseDouble(eventSnapshot.child("latitude").getValue().toString());
                    double longitude = Double.parseDouble(eventSnapshot.child("longitude").getValue().toString());
                    String connection = eventSnapshot.child("connection").getValue().toString();



                    eventList.add(new Event(eventName,eventDescription,eventEid,author,eventImage,imageName,address,animalType,latitude,longitude,eventPrice,connection));

                    mutableEventList.setValue(new ArrayList<>(eventList));

                    filterEventList.setValue(new ArrayList<>(eventList));
                    Log.d("LoadEvents",eventList+" home ");




                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}

