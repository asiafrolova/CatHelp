package com.example.cathelp.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cathelp.model.Event;
import com.example.cathelp.viewModals.HomeViewModel;
import com.example.cathelp.databinding.FragmentDetailBinding;
import com.rey.material.widget.ImageButton;


public class DetailFragment extends Fragment{
    private static FragmentDetailBinding fragmentDetailBinding;
    static HomeViewModel homeViewModel;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater,container,false);
        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        fragmentDetailBinding.setHomeViewModel(homeViewModel);
        fragmentDetailBinding.exitDetail.setOnClickListener(v -> {
            Log.d("EXIT","exit");
            getActivity().getFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().popBackStack();

        });
    }

    @BindingAdapter("android:addItem")
    public static void addItem(ImageButton imageButton,Event event) {
        imageButton.setOnClickListener(v -> {


        Log.d("addItem",event.toString());
        boolean isAdded = homeViewModel.addEventToMarks(event);
        fragmentDetailBinding.invalidateAll();
        });
    }



}