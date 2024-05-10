package com.example.cathelp.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cathelp.databinding.ActivityStartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Locale;

import io.paperdb.Paper;

public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;
    private FirebaseAuth mAuth;
    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOCALE = "locale";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Paper.init(this);
        initLocale();

        mAuth = FirebaseAuth.getInstance();



        binding.loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });
        binding.registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });




    }
    public static Intent startActivityIntent(Context context){
        return new Intent(context, StartActivity.class);
    }
    private void initLocale() {
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
}