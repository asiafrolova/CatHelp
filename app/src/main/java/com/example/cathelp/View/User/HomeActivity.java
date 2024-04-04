package com.example.cathelp.View.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cathelp.R;
import com.example.cathelp.View.Admin.AdminAddNewProductActivity;
import com.example.cathelp.View.Fragments.AccountFragment;
import com.example.cathelp.View.Fragments.HomeFragment;
import com.example.cathelp.View.Fragments.MapsFragment;
import com.example.cathelp.View.Fragments.MarksFragment;
import com.example.cathelp.View.LoginActivity;
import com.example.cathelp.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.SnackBar;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    View floatingActionButton;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        replaceFragment(new HomeFragment());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        floatingActionButton =(View) findViewById(R.id.view);
        floatingActionButton.bringToFront();


        bottomNavigationView =  findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        floatingActionButton.setOnClickListener(v -> {
            Log.d("TAG","Click");
            Intent newEventIntent = new Intent(HomeActivity.this, AdminAddNewProductActivity.class);
            startActivity(newEventIntent);
        });

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId()==R.id.home){
                replaceFragment(new HomeFragment());

            }else if(menuItem.getItemId()==R.id.account){
                replaceFragment(new AccountFragment());

            } else if (menuItem.getItemId()==R.id.location) {
                replaceFragment(new MapsFragment());
            } else if(menuItem.getItemId()==R.id.bookmarks){
                replaceFragment(new MarksFragment());
            }
            return  true;
        });





    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        floatingActionButton =(View) findViewById(R.id.view);
        floatingActionButton.bringToFront();
        floatingActionButton.setOnClickListener(v -> {
            Log.d("TAG","Click");
            Intent newEventIntent = new Intent(HomeActivity.this, AdminAddNewProductActivity.class);
            startActivity(newEventIntent);
        });
    }
}