package com.example.cathelp.View.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.canhub.cropper.CropImageActivity;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.example.cathelp.Prevalent.Prevalent;
import com.example.cathelp.R;
import com.example.cathelp.databinding.ActivitySettingsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;

    private boolean checker = false;
    private Uri imageUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveBtn.setOnClickListener(v -> {
            if (checker){
                userInfoSaved();

            }else{
                updateOnlyInfoUser();
            }
        });
        binding.imageAccount.setOnClickListener(v -> {
            checker = true;
            ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(getApplicationContext(), true));
                }
            });
            CropImageOptions cropImageOptions = new CropImageOptions();
            cropImageOptions.imageSourceIncludeGallery = false;
            cropImageOptions.imageSourceIncludeCamera = true;
            CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
            cropImage.launch(cropImageContractOptions);
        });
    }

    private void updateOnlyInfoUser() {
    }

    private void userInfoSaved() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name",binding.name.getText().toString());
        userMap.put("phone",binding.phone.getText().toString());
        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(this,"Сохранено",Toast.LENGTH_LONG).show();
        finish();

    }

    public static Intent intentSettingActivity(Context activity){
        Intent intent = new Intent(activity, SettingsActivity.class);
        return intent;
    }
}