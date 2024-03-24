package com.example.cathelp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cathelp.Models.Users;
import com.example.cathelp.Prevalent.Prevalent;
import com.example.cathelp.UI.User.HomeActivity;
import com.example.cathelp.databinding.ActivityStartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        Paper.init(this);

        binding.loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });
        binding.registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        String userPhone = Paper.book().read(Prevalent.userPhoneKey);
        String userPassword = Paper.book().read(Prevalent.userPasswordKey);

        if (userPassword != "" && userPhone != ""){
            if(!TextUtils.isEmpty(userPhone) && !TextUtils.isEmpty(userPassword)){
                binding.loadingBar.setVisibility(ProgressBar.VISIBLE);
                ValidateUser(userPhone,userPassword);
            }
        }
    }

    private void ValidateUser(String phone, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(phone).exists()){
                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone) ){
                        if(usersData.getPassword().equals(password) ){
                            Toast.makeText(StartActivity.this,"Успешный вход",Toast.LENGTH_LONG).show();
                            binding.loadingBar.setVisibility(ProgressBar.GONE);
                            Intent homeIntent = new Intent(StartActivity.this, HomeActivity.class);
                            startActivity(homeIntent);

                        }else {
                            binding.loadingBar.setVisibility(ProgressBar.GONE);

                        }
                    }
                }else
                {
                    Toast.makeText(StartActivity.this
                            ,"Номер "+phone+" ещё не зарегестрирован"
                            ,Toast.LENGTH_LONG).show();
                    binding.loadingBar.setVisibility(ProgressBar.GONE);
                    Intent registerIntent = new Intent(StartActivity.this,LoginActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}