package com.example.cathelp.UI.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cathelp.R;
import com.example.cathelp.UI.LoginActivity;
import com.example.cathelp.UI.User.HomeActivity;
import com.example.cathelp.databinding.ActivityAdminAddNewProductBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private ActivityAdminAddNewProductBinding binding;
    private static final int GALLERY = 1;
    private Uri ImageUri;
    private String categoryName,description,price,eventName;
    private String saveCurrentDate,saveCurrentTime;
    private String eventRandomKey;
    private StorageReference EventImageRef;
    private int radioButtonID;
    private int selectedIndex;
    private String downloadImageUrl;
    private DatabaseReference eventsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAdminAddNewProductBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        init();

        Toast.makeText(this,"Приветствуем админа!",Toast.LENGTH_SHORT).show();

        binding.selectImage.setOnClickListener(v -> {
            OpenGallery();
        });
        binding.saveBtn.setOnClickListener(v -> {
            ValidateEventData();
        });
        binding.groupButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonID = binding.groupButton.getCheckedRadioButtonId();
                selectedIndex = (radioButtonID != -1) ? binding.groupButton.indexOfChild(findViewById(radioButtonID)) : -1;
                if (selectedIndex==1){
                    binding.selectPrice.setVisibility(View.VISIBLE);
                }
                else {
                    binding.selectPrice.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void ValidateEventData() {


        description = binding.selectDescription.getText().toString();
        eventName = binding.selectName.getText().toString();
        radioButtonID = binding.groupButton.getCheckedRadioButtonId();
        selectedIndex = (radioButtonID != -1) ? binding.groupButton.indexOfChild(findViewById(radioButtonID)) : -1;
        if (selectedIndex==1){
            price  = binding.selectPrice.getText().toString();
        }

        if (ImageUri == null){
            Toast.makeText(this,"Добавьте изображение",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this,"Добавьте описание",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(eventName)){
            Toast.makeText(this,"Добавьте заголовок",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price) && selectedIndex==0){
            Toast.makeText(this,"Укажите цену",Toast.LENGTH_SHORT).show();
        }
        else {
            binding.loadingBar.setVisibility(ProgressBar.VISIBLE);
            StoreEventInformation();
        }
    }

    private void StoreEventInformation() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        eventRandomKey = saveCurrentDate + saveCurrentTime;
        StorageReference filePath = EventImageRef.child(ImageUri.getLastPathSegment() + eventRandomKey + ".webm");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this,"Ошибка: "+message,Toast.LENGTH_SHORT).show();
                binding.loadingBar.setVisibility(ProgressBar.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Фото загружено",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminAddNewProductActivity.this,"Фото сохранено",Toast.LENGTH_SHORT).show();


                            SaveEventInfoToDataBase();
                        }
                    }


                });
            }
        });

    }

    private void SaveEventInfoToDataBase() {
        HashMap<String, Object> eventMap = new HashMap<>();

        eventMap.put("eid",eventRandomKey);
        eventMap.put("date",saveCurrentDate);
        eventMap.put("time",saveCurrentTime);
        eventMap.put("description",description);
        eventMap.put("image",downloadImageUrl);
        eventMap.put("category",categoryName);
        eventMap.put("price",price);
        eventMap.put("eventName",eventName);

        eventsRef.child(eventName).updateChildren(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent homeIntent = new Intent(AdminAddNewProductActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    
                    Toast.makeText(AdminAddNewProductActivity.this,"Событие добавлено",Toast.LENGTH_SHORT).show();
                    binding.loadingBar.setVisibility(ProgressBar.VISIBLE);
                }
                else {
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this,"Ошибка: "+message,Toast.LENGTH_SHORT).show();
                    binding.loadingBar.setVisibility(ProgressBar.VISIBLE);
                }
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            binding.selectImage.setImageURI(ImageUri);
        }
    }
    private void init(){
        EventImageRef = FirebaseStorage.getInstance().getReference().child("Event Images");
        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");

    }
}