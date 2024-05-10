package com.example.cathelp.viewModals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.cathelp.repositories.DataBaseMarks;
import com.example.cathelp.repositories.EventRepo;
import com.example.cathelp.repositories.HomeRepo;
import com.example.cathelp.model.Event;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    HomeRepo homeRepo = new HomeRepo();
    EventRepo eventRepo = new EventRepo();
    MutableLiveData<Event> mutableEvent = new MutableLiveData<>();
    DataBaseMarks baseMarks = eventRepo.getInstance();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Event>> getEvents(){
        return homeRepo.getEvents();


    }



    public void setEvent(Event event){
        mutableEvent.setValue(event);
    }

    public LiveData<Event> getEvent(){
        return  mutableEvent;
    }


    public LiveData<List<Event>> getMark(){
        return eventRepo.getMark();
    }


    public boolean addEventToMarks(Event event){

        return eventRepo.addItemToMarks(event);

        

    }


    public LiveData<List<Event>> getMyEvent() {
        return eventRepo.getMyEvent();
    }


}
