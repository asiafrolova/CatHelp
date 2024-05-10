package com.example.cathelp.view;

import static com.example.cathelp.view.StartActivity.APP_PREFERENCES_LOCALE;
import static com.example.cathelp.view.StartActivity.mSettings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cathelp.R;
import com.example.cathelp.view.fragments.AccountFragment;
import com.example.cathelp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        initLocale();

        binding.registerBtn.setOnClickListener(v -> {
            CreateAccount();
        });
    }

    private void CreateAccount() {
        String username = binding.registerUsernameInput.getText().toString();
        String phone = binding.registerPhoneInput.getText().toString();
        String password = binding.registerPasswordInput.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, R.string.add_name,Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, R.string.add_e_mail,Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.add_password,Toast.LENGTH_LONG).show();
        }else{
            binding.loadingBar.setVisibility(ProgressBar.VISIBLE);

            ValidatePhone(username, phone, password);
        }
    }
    private void ValidatePhone(String username, String email, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    addUserOnDatabase(username,email);
                    Log.d("TAG", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                }else{
                    Log.d("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, R.string.authentication_failed,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void addUserOnDatabase(String username, String email){
        HashMap<String,String> userInfo = new HashMap<>();
        userInfo.put("email",email);
        userInfo.put("name",username);
        userInfo.put(AccountFragment.PATH_IMAGE,"");
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, R.string.successful,Toast.LENGTH_LONG).show();
                            binding.loadingBar.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(RegisterActivity.this,R.string.error,Toast.LENGTH_LONG).show();
                            binding.loadingBar.setVisibility(View.GONE);
                        }

                    }
                });
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));

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