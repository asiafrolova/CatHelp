package com.example.cathelp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cathelp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.registerBtn.setOnClickListener(v -> {
            CreateAccount();
        });
    }

    private void CreateAccount() {
        String username = binding.registerUsernameInput.getText().toString();
        String phone = binding.registerPhoneInput.getText().toString();
        String password = binding.registerPasswordInput.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Введите имя",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this,"Введите номер телефона",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Введите пароль",Toast.LENGTH_LONG).show();
        }else{
            binding.loadingBar.setVisibility(ProgressBar.VISIBLE);

            ValidatePhone(username, phone, password);
        }
    }

    private void ValidatePhone(String username, String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone",phone);
                    userDataMap.put("name",username);
                    userDataMap.put("password",password);

                    RootRef.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                binding.loadingBar.setVisibility(ProgressBar.GONE);
                                Toast.makeText(RegisterActivity.this,"Регистрация прошла успешно",Toast.LENGTH_LONG).show();
                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(loginIntent);

                            }else {
                                binding.loadingBar.setVisibility(ProgressBar.GONE);
                                Toast.makeText(RegisterActivity.this,"Произошла ошибка",Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }else {
                    Toast.makeText(RegisterActivity.this
                            ,"Номер "+phone+" уже зарегестрирован"
                            ,Toast.LENGTH_LONG).show();
                    binding.loadingBar.setVisibility(ProgressBar.GONE);
                    Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(loginIntent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}