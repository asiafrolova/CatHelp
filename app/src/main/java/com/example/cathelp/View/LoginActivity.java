package com.example.cathelp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cathelp.View.Admin.AdminAddNewProductActivity;
import com.example.cathelp.Models.Users;
import com.example.cathelp.Prevalent.Prevalent;
import com.example.cathelp.View.User.HomeActivity;
import com.example.cathelp.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    private String parentDbName =  "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        Paper.init(this);

        binding.loginBtn.setOnClickListener(v -> {
            loginUser();
        });
        binding.adminPanelLink.setOnClickListener(v -> {
            binding.adminPanelLink.setVisibility(View.INVISIBLE);
            binding.notAdminPanelLink.setVisibility(View.VISIBLE);
            binding.loginBtn.setText("Вход для админа");
            parentDbName = "Admins";

        });
        binding.notAdminPanelLink.setOnClickListener(v -> {
            binding.adminPanelLink.setVisibility(View.VISIBLE);
            binding.notAdminPanelLink.setVisibility(View.INVISIBLE);
            binding.loginBtn.setText("Войти");
            parentDbName = "Users";
        });
    }
    private void loginUser() {
        String phone = binding.loginPhoneInput.getText().toString();
        String password = binding.loginPasswordInput.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this,"Введите номер телефона",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Введите пароль",Toast.LENGTH_LONG).show();
        }else{
            binding.loadingBar.setVisibility(ProgressBar.VISIBLE);

            ValidateUser(phone, password);}
    }

    private void ValidateUser(String phone, String password) {

        if(binding.loginCheckbox.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists()){
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone) ){
                        if(usersData.getPassword().equals(password) ){
                            if(parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this,"Успешный вход",Toast.LENGTH_LONG).show();
                                binding.loadingBar.setVisibility(ProgressBar.GONE);
                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                            } else if (parentDbName.equals("Admins")) {

                                Toast.makeText(LoginActivity.this,"Успешный вход",Toast.LENGTH_SHORT).show();
                                binding.loadingBar.setVisibility(ProgressBar.GONE);
                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                            }

                        }else {
                            Toast.makeText(LoginActivity.this,"Неверный пароль",Toast.LENGTH_LONG).show();
                            binding.loadingBar.setVisibility(ProgressBar.GONE);

                        }
                    }
                }else
                {
                    Toast.makeText(LoginActivity.this
                            ,"Номер "+phone+" ещё не зарегестрирован"
                            ,Toast.LENGTH_LONG).show();
                    binding.loadingBar.setVisibility(ProgressBar.GONE);
                    Intent registerIntent = new Intent(LoginActivity.this,LoginActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}