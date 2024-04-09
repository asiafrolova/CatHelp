package com.example.cathelp.ViewModal;

import android.app.Application;
import android.service.credentials.Action;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.cathelp.Repositories.HomeRepo;
import com.example.cathelp.model.Event;
import com.example.cathelp.Repositories.EventRepo;

import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class HomeViewModel extends ViewModel  {
    HomeRepo homeRepo = new HomeRepo();
    public LiveData<List<Event>> getEvents(){
        return homeRepo.getEvents();
    }

    
}
