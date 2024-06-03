package com.example.cathelp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.cathelp.model.Event;
import com.example.cathelp.model.LinkMark;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EventRepo {
    private DataBaseMarks instance = App.getInstance().getDatabase();
    private List<Event> allEventsFromHome = new ArrayList<>();
    private MutableLiveData<List<Event>> mutableMarksEvents = new MutableLiveData<>();
    private ArrayList<LinkMark> listLink = new ArrayList<>();
    public static ArrayList<Event> listEvents = new ArrayList<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private MutableLiveData<List<Event>> mutableMyEvents = new MutableLiveData<>();
    private ArrayList<Event> myEventsList  =new ArrayList<>();



    public EventRepo() {

    }

    public DataBaseMarks getInstance() {
        return instance;
    }

    public LiveData<List<Event>> getMark() {
        initMark();
        linkToEvent(listLink);
        return mutableMarksEvents;




    }

    public List<LinkMark> initMark() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<List<LinkMark>> result = es.submit(new Callable<List<LinkMark>>() {
            @Override
            public List<LinkMark> call() throws Exception {
                listLink = (ArrayList<LinkMark>) instance.marksDao().getAll(mAuth.getCurrentUser().getUid());

                while (true) {
                    if (listLink != null) {
                        return listLink;


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
    }



   public List<Event> linkToEvent(List<LinkMark> linkMarks){
       if(listEvents != null){
           listEvents.clear();
       }
       allEventsFromHome = new ArrayList<>(HomeRepo.eventList);
       for (Event event : allEventsFromHome) {
           for (LinkMark linkMark : linkMarks) {
               if(event.getName().equals(linkMark.getNameEvent())){
                   listEvents.add(event);
               }
           }

       }
       mutableMarksEvents.setValue(listEvents);
       return listEvents;


   }









    public synchronized boolean addItemToMarks(Event event){
        if(listLink == null){
            initMark();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(instance.marksDao().getById(event.getName()) != null){
                    instance.marksDao().delete(new LinkMark(event.getName(),mAuth.getCurrentUser().getUid()));
                    listLink = (ArrayList<LinkMark>) instance.marksDao().getAll(mAuth.getCurrentUser().getUid());

                }else{
                    instance.marksDao().insert(new LinkMark(event.getName(),mAuth.getCurrentUser().getUid()));
                    listLink = (ArrayList<LinkMark>) instance.marksDao().getAll(mAuth.getCurrentUser().getUid());

                }

            }
        }).start();

        return true;
    }
    public synchronized boolean deleteItemToMarks(Event event){
        if(listLink == null){
            initMark();
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                if(instance.marksDao().getById(event.getName()) != null){
                    instance.marksDao().delete(new LinkMark(event.getName(),mAuth.getCurrentUser().getUid()));
                    listLink = (ArrayList<LinkMark>) instance.marksDao().getAll(mAuth.getCurrentUser().getUid());
                }


            }
        }).start();

        return true;
    }

    public synchronized boolean isLike(Event event) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Boolean> result = es.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (instance.marksDao().getById(event.getName()) == null) {

                    return false;


                } else {

                    return true;


                }
            }
        });
        return result.get();


    }

    public LiveData<List<Event>> getMyEvent() {
        if(myEventsList != null){
            myEventsList.clear();
        }
        allEventsFromHome = new ArrayList<>(HomeRepo.eventList);
        for (Event event : allEventsFromHome) {
            if(event.getAuthor().equals(mAuth.getCurrentUser().getUid())){
                myEventsList.add(event);
            }


        }
        mutableMyEvents.setValue(myEventsList);
        return mutableMyEvents;
    }
}

