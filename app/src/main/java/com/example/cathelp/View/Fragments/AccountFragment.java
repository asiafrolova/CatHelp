package com.example.cathelp.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cathelp.R;
import com.example.cathelp.View.User.SettingsActivity;
import com.example.cathelp.databinding.FragmentAccountBinding;
import com.example.cathelp.databinding.FragmentHomeBinding;


public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;

    public AccountFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.settingsView.setOnClickListener(v -> {
            startActivity(SettingsActivity.intentSettingActivity(getContext()));
        });
    }
}