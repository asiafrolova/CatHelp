package com.example.cathelp.view;

import static android.widget.Toast.LENGTH_LONG;

import static com.example.cathelp.view.StartActivity.APP_PREFERENCES_LOCALE;
import static com.example.cathelp.view.StartActivity.mSettings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cathelp.R;
import com.example.cathelp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private boolean forgetPassword = false;


    private String parentDbName =  "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        Paper.init(this);
        initLocale();


        binding.loginBtn.setOnClickListener(v -> {
            if(forgetPassword){
                FirebaseAuth.getInstance().sendPasswordResetEmail(binding.loginPhoneInput.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, R.string.reset_email_send, LENGTH_LONG).show();
                                }
                            }
                        });

            }else{
                loginUser();
            }

        });

        binding.forgetPasswordLink.setOnClickListener(v -> {
            if(forgetPassword){
                forgetPassword = false;
                binding.loginPasswordInput.setVisibility(View.VISIBLE);
                binding.forgetPasswordLink.setText( R.string.forgot_password);
            }else{
                forgetPassword = true;
                binding.loginPasswordInput.setVisibility(View.GONE);
                binding.forgetPasswordLink.setText( R.string.know_password);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
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
    private void loginUser() {
        String phone = binding.loginPhoneInput.getText().toString();
        String password = binding.loginPasswordInput.getText().toString();


        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this,R.string.add_e_mail, LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.add_password,Toast.LENGTH_LONG).show();
        }else if(password.length()<8){
            Toast.makeText(this, R.string.password_must_be_longer_than_8_characters,Toast.LENGTH_LONG).show();
        }else{
            binding.loadingBar.setVisibility(ProgressBar.VISIBLE);


            ValidateUser(phone, password);}
    }
    private void ValidateUser(String email,String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                            binding.loadingBar.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(LoginActivity.this,R.string.error, LENGTH_LONG).show();
                            binding.loadingBar.setVisibility(View.GONE);
                        }


                    }
                });
    }



}