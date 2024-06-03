package com.example.cathelp.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cathelp.R;
import com.example.cathelp.adapters.MarksAdapter;
import com.example.cathelp.databinding.FragmentMarksBinding;
import com.example.cathelp.model.Event;
import com.example.cathelp.viewModals.HomeViewModel;

import java.util.List;


public class MarksFragment extends Fragment implements MarksAdapter.MarksInterface {
    private HomeViewModel homeViewModel;
    private FragmentMarksBinding marksBinding;
    private MarksAdapter marksAdapter;
    private NavController navController;

    public MarksFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        marksBinding = FragmentMarksBinding.inflate(inflater,container,false);
        return marksBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataObserve();
        navController = Navigation.findNavController(view);


    }
    public void dataObserve(){
        marksAdapter = new MarksAdapter(this);
        marksBinding.marksRecycleView.setAdapter(marksAdapter);
        marksBinding.marksRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel = new ViewModelProvider((requireActivity())).get(HomeViewModel.class);
        homeViewModel.getMark().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                Log.d("FRAGMENT"," "+events);
                Log.d("MarksFragment","viewmodel observe ");
                marksAdapter.submitList(events);

            }
        });

    }

    @Override
    public void addItemMarks(Event event) {
        boolean isAdded = homeViewModel.addEventToMarks(event);
        marksAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClickMarks(Event event) {
        homeViewModel.setEvent(event);
        navController.navigate(R.id.action_marksFragment_to_detailFragment);

    }
    @Override
    public void onStart() {
        super.onStart();
        marksAdapter = new MarksAdapter(this);


        dataObserve();
    }
}