package com.example.cathelp.view;

import static com.example.cathelp.view.StartActivity.APP_PREFERENCES;
import static com.example.cathelp.view.StartActivity.APP_PREFERENCES_LOCALE;
import static com.example.cathelp.view.StartActivity.mSettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import com.example.cathelp.R;
import com.example.cathelp.repositories.HomeRepo;
import com.example.cathelp.databinding.ActivityHomeBinding;
import com.example.cathelp.viewModals.HomeViewModel;

import java.util.Locale;

public class  HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    NavController navController;
    HomeViewModel homeViewModel;
    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new HomeRepo().loadEvents();

        initLocale();



        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //setSupportActionBar(binding.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navigationView,navController);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        binding.floatingBtn.setOnClickListener(v -> {
            Intent newEventIntent = new Intent(HomeActivity.this, AddNewProductActivity.class);
            startActivity(newEventIntent);
        });





    }
    private void initLocale() {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_PREFERENCES_LOCALE)) {
            String lang = mSettings.getString(APP_PREFERENCES_LOCALE, "");
            if (lang.equalsIgnoreCase(""))
                return;
            Locale myLocale = new Locale(lang);

            Locale.setDefault(myLocale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = myLocale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        initLocale();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu,menu);
        return true;

    }

    public static Intent getHomeActivityIntent(Context context){
        return new Intent(context, HomeActivity.class);

    }

}