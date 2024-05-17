package com.example.cathelp.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.cathelp.R;
import com.example.cathelp.databinding.FragmentAccountBinding;
import com.example.cathelp.repositories.EventRepo;
import com.example.cathelp.view.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;


public class AccountFragment extends Fragment {
    public static final String PATH_IMAGE = "image";
    private FragmentAccountBinding binding;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private NavController navController;


    public AccountFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserInfo();
        navController = Navigation.findNavController(view);
        binding.settingsView.setOnClickListener(v -> {
            Intent settingIntent = SettingsActivity.settingActivityIntent(getContext());
            startActivity(settingIntent);
        });
        binding.myEventBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_accountFragment_to_myEventFragment);
        });
        

    }



    private void loadUserInfo () {
        mDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = snapshot.child("name").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String profileImage = snapshot.child(AccountFragment.PATH_IMAGE).getValue().toString();

                        binding.name.setText(username);
                        binding.phone.setText(email);

                        if (profileImage.isEmpty()) {
//                            Glide.with(getContext())
//                                    .load("https://i.pinimg.com/474x/bd/18/cb/bd18cb31dc17600e58570dbf53ce38da.jpg")
//                                    .into(binding.imageAccount);

                        }else{
                            Glide.with(getContext())
                                    .load(profileImage)
                                    .into(binding.imageAccount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}