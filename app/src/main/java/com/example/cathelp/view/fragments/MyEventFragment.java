package com.example.cathelp.view.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cathelp.R;
import com.example.cathelp.repositories.EventRepo;
import com.example.cathelp.repositories.HomeRepo;
import com.example.cathelp.adapters.MyEventAdapter;
import com.example.cathelp.databinding.FragmentMyEventBinding;
import com.example.cathelp.model.Event;
import com.example.cathelp.viewModals.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.util.List;

public class MyEventFragment extends Fragment implements MyEventAdapter.MyEventInterface {

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private HomeViewModel homeViewModel;
    private FragmentMyEventBinding myEventBinding;
    private MyEventAdapter eventAdapter;
    private NavController navController;
    private EventRepo eventRepo = new EventRepo();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public MyEventFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myEventBinding = FragmentMyEventBinding.inflate(inflater,container,false);
        return myEventBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataObserve();
        navController = Navigation.findNavController(view);
    }
    public void dataObserve(){
        eventAdapter = new MyEventAdapter(this);
        myEventBinding.myEventsRecycleView.setAdapter(eventAdapter);
        myEventBinding.myEventsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel = new ViewModelProvider((requireActivity())).get(HomeViewModel.class);
        homeViewModel.getMyEvent().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                eventAdapter.submitList(events);

            }
        });


    }
    private void deleteFromDatabase(Event event){


        if(event.getImages().isEmpty()){
            StorageReference storageRef = mStorage.getReference().child("EventImages/"+event.getImageName());
            DatabaseReference mRef  = mDatabase.getReference().child("Events");
            Query query = mRef.orderByKey().equalTo(event.getName());

            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot eventSnapshot: snapshot.getChildren()) {

                                HomeRepo.eventList.remove(event);
                                eventRepo.deleteItemToMarks(event);



                                eventSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            myEventBinding.loadingBarMyEvents.setVisibility(View.GONE);

                                            dataObserve();
                                            eventAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            });
        }else{
            StorageReference storageRef = mStorage.getReference().child("EventImages/"+event.getName());
            DatabaseReference mRef  = mDatabase.getReference().child("Events");
            Query query = mRef.orderByKey().equalTo(event.getName());
            storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    for (int i = 0; i < listResult.getItems().size(); i++) {
                        if(i+1==listResult.getItems().size()){
                            listResult.getItems().get(i).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot eventSnapshot: snapshot.getChildren()) {

                                                HomeRepo.eventList.remove(event);
                                                eventRepo.deleteItemToMarks(event);


                                                eventSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            myEventBinding.loadingBarMyEvents.setVisibility(View.GONE);

                                                            dataObserve();
                                                            eventAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }else{
                            listResult.getItems().get(i).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                        }
                    }
                }
            });
        }


    }


    @Override
    public void deleteItem(Event event) {
        PopupDialog.getInstance(getContext())
                .standardDialogBuilder()
                .createStandardDialog()
                .setHeading(getString(R.string.delete))
                .setDescription(getString(R.string.are_you_sure_you_want_to_delete))
                .setNegativeButtonText(getString(R.string.cancel))
                .setPositiveButtonText(getString(R.string.submit))
                .setPositiveButtonBackgroundColor(R.color.new_yellow)

                .setPositiveButtonRippleColor(R.color.yellow_white)
                .setIcon(R.drawable.remove_icon)
                .setIconColor(R.color.grey)
                .build(new StandardDialogActionListener() {
                    @Override
                    public void onPositiveButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                        myEventBinding.loadingBarMyEvents.setVisibility(View.VISIBLE);
                        deleteFromDatabase(event);
                    }

                    @Override
                    public void onNegativeButtonClicked(Dialog dialog) {
                        dialog.dismiss();

                    }
                })
                .show();



    }

    @Override
    public void onItemSelected(Event event) {

    }

    @Override
    public void onMyItemClick(Event event) {
        homeViewModel.setEvent(event);
        navController.navigate(R.id.action_myEventFragment_to_detailFragment);
    }


    @Override
    public void onStart() {
        super.onStart();
        eventAdapter = new MyEventAdapter(this);


        dataObserve();
    }
}