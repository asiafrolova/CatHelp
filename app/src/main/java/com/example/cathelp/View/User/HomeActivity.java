package com.example.cathelp.View.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cathelp.R;
import com.example.cathelp.View.Admin.AdminAddNewProductActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    com.example.cathelp.databinding.ActivityHomeBinding binding;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.cathelp.databinding.ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navigationView,navController);

        binding.floatingBtn.setOnClickListener(v -> {
            Intent newEventIntent = new Intent(HomeActivity.this, AdminAddNewProductActivity.class);
            startActivity(newEventIntent);
        });




    }
}