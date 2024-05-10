package com.example.cathelp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cathelp.R;
import com.example.cathelp.repositories.HomeRepo;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isOnline(this)) {
            PopupDialog.getInstance(this)
                    .standardDialogBuilder()
                    .createStandardDialog()
                    .setHeading(getString(R.string.no_internet))
                    .setDescription(getString(R.string.please_check_your_internet_connection))
                    .setNegativeButtonText(getString(R.string.retry))
                    .setPositiveButtonText(getString(R.string.exit))
                    .setPositiveButtonBackgroundColor(R.color.new_yellow)

                    .setPositiveButtonRippleColor(R.color.yellow_white)
                    .setIcon(R.drawable.wifi_not_icon)
                    .setIconColor(R.color.grey)
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                            finish();

                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                            new HomeRepo().loadEvents();
                            startActivity(new Intent(SplashScreenActivity.this,SplashScreenActivity.class));
                        }
                    })
                    .show();
        } else{
            mAuth = FirebaseAuth.getInstance();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                reload();


            } else {
                Intent intent = new Intent(SplashScreenActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }







    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            Log.d("TAG","WI_FI");
            return true;
        }
        Log.d("TAG","NO WI_FI");
        return false;
    }
    private void reload() {

        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //startActivity(HomeActivity.getHomeActivityIntent(SplashScreenActivity.this));
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("TAG", "reload:(", task.getException());
                    Toast.makeText(SplashScreenActivity.this,
                            R.string.error,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}