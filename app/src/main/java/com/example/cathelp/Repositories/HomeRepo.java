package com.example.cathelp.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cathelp.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeRepo {
    private MutableLiveData<List<Event>> mutableEventList;
    public LiveData<List<Event>> getEvents(){
        if (mutableEventList == null){
            mutableEventList = new MutableLiveData<>();
            loadEvents();
        }
        return mutableEventList;
    }

    private void loadEvents() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event("Lola","description","123","https://s0.rbk.ru/v6_top_pics/media/img/5/89/756723921758895.webp",true,2000));
        eventList.add(new Event("Jack","description","123","https://hips.hearstapps.com/ghk.h-cdn.co/assets/16/08/gettyimages-530330473.jpg?crop=0.659xw:0.990xh;0.123xw,0.00779xh&resize=980:*",false,2000));
        eventList.add(new Event("Saimon","description","123","https://s0.rbk.ru/v6_top_pics/media/img/3/65/756723923455653.webp",false,2000));
        eventList.add(new Event("Fei","description","123","https://cdn.outsideonline.com/wp-content/uploads/2021/04/17/dog_head_square.jpg?crop=1:1&width=300&enable=upscale",true,2000));

        mutableEventList.setValue(eventList);
    }


}
