package com.example.cathelp.view;


import static com.example.cathelp.view.StartActivity.APP_PREFERENCES;
import static com.example.cathelp.view.StartActivity.APP_PREFERENCES_LOCALE;
import static com.example.cathelp.view.StartActivity.mSettings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cathelp.R;
import com.example.cathelp.databinding.ActivitySettingsBinding;
import com.example.cathelp.repositories.EventRepo;
import com.example.cathelp.repositories.HomeRepo;
import com.example.cathelp.view.fragments.AccountFragment;
import com.example.cathelp.model.Event;
import com.example.cathelp.viewModals.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    ActivityResultLauncher<String> cropImage;
    private StorageReference storageProfilePictureref;
    private UploadTask uploadTask;

    private boolean checker = false;
    private Uri imageUri;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private HomeViewModel homeViewModel;
    private String[] languages = {"English","Русский"};
     // имя кота



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        com.chivorn.smartmaterialspinner.R.layout.smart_material_spinner_dropdown_item_layout,
                        languages);
        binding.spinnerLanguage.setAdapter(adapter);
        //binding.spinnerLanguage.setSelection(4);


        loadUserInfo();

        initLocale();


        /*binding.spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    changeLocale("en");
                }else if(position==1){
                    changeLocale("ru");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        binding.exitImageView.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(SettingsActivity.this, StartActivity.class));

        });
        binding.changePasswordBtn.setOnClickListener(v -> {
            userReAuth();

        });
        binding.changePassword.setOnClickListener(v -> {
            binding.newPasswordLayout.setVisibility(View.VISIBLE);
            binding.oldPasswordLayout.setVisibility(View.VISIBLE);
            binding.changePasswordBtn.setVisibility(View.VISIBLE);
        });
        binding.deleteAccountBtn.setOnClickListener(v -> {
            PopupDialog.getInstance(this)
                    .standardDialogBuilder()
                    .createStandardDialog()
                    .setHeading(getString(R.string.delete_account))
                    .setDescription(getString(R.string.are_you_sure_you_delete_account))
                    .setNegativeButtonText(getString(R.string.cancel))
                    .setPositiveButtonText(getString(R.string.delete))
                    .setPositiveButtonBackgroundColor(R.color.new_yellow)

                    .setPositiveButtonRippleColor(R.color.yellow_white)
                    .setIcon(R.drawable.remove_icon)
                    .setIconColor(R.color.grey)
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                            binding.loadingBarSettings.setVisibility(View.VISIBLE);
                            String userID = mAuth.getCurrentUser().getUid();
                            List<Event> myEvents = new EventRepo().getMyEvent().getValue();
                            deleteFromAuth();
                            for (Event myEvent : myEvents) {
                                new EventRepo().deleteItemToMarks(myEvent);
                                deleteEventFromDatabase(myEvent);
                            }
                            deleteUserFromDatabase(userID);

                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();

                        }
                    })
                    .show();
            /*CharSequence options[]=new CharSequence[]{
                    // select any from the value
                    "Delete",
                    "Cancel",
            };
            AlertDialog.Builder builder=new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Delete Content");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0) {

                    }

                }
            });
            builder.show();*/


        });



        binding.saveBtn.setOnClickListener(v -> {
            if(binding.spinnerLanguage.getSelectedItemPosition()==0){
                changeLocale("en");
            }else if(binding.spinnerLanguage.getSelectedItemPosition()==1){
                changeLocale("ru");
            }

            if (checker) {
                try {
                    userAllInfoSaved();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                userInfoSaved();

            }
        });
        binding.chanheProfileImage.setOnClickListener(v -> {
            checker = true;
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(SettingsActivity.this);


        });



    }

    @Override
    protected void onResume() {
        initLocale();
        super.onResume();
    }

    private void changeLocale(String lang)

    {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);

        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


        saveLocale(lang);
        //startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));

    }
    private void saveLocale(String lang) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_LOCALE, lang);
        editor.apply();

    }


    public void userReAuth(){
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(),binding.oldPasswordInput.getText().toString());
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Settings","user re-auth");
                            userUpdatePassword();
                        }
                        else {
                            Toast.makeText(SettingsActivity.this, R.string.uncorrect_password,Toast.LENGTH_LONG).show();
                            binding.newPasswordInput.setText("");
                            binding.oldPasswordInput.setText("");
                        }

                    }
                });
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

            saveLocale(lang);
            if(lang.equals("ru")){
                binding.spinnerLanguage.setSelection(1);
            }else if(lang.equals("en")){
                binding.spinnerLanguage.setSelection(0);
            }
        }


    }

    private void userUpdatePassword() {
        String newPassword = binding.newPasswordInput.getText().toString();
        mAuth.getCurrentUser().updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this, R.string.password_change_successful,Toast.LENGTH_LONG).show();
                            binding.newPasswordInput.setText("");
                            binding.oldPasswordInput.setText("");
                        }else {
                            Log.d("Settings","error in update");
                        }
                    }
                });
    }


    private void loadUserInfo () {
            mDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String username = snapshot.child("name").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();
                            String profileImage = snapshot.child(AccountFragment.PATH_IMAGE).getValue().toString();

                            binding.name.setText(username);
                            binding.phone.setText(email);

                            if ((profileImage != "")) {
                                Glide.with(getApplicationContext())
                                        .load(profileImage)
                                        .into(binding.imageAccount);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        private void userInfoSaved () {

            String name = binding.name.getText().toString();
            String email = binding.phone.getText().toString();

            DatabaseReference ref = mDatabase.getReference().child("Users");

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("email", email);
            ref.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

            mAuth.getCurrentUser().updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User email address updated.");
                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));

                            }

                        }
                    });

            Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();


        }

        private void userAllInfoSaved () throws IOException {
            uploadImage();


        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();

                Log.d("TAG", imageUri.toString());


                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                binding.imageAccount.setImageBitmap(bitmap);

            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();

                startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                finish();
            }

        }
        private void uploadImage () {

            binding.loadingBarSettings.setVisibility(View.VISIBLE);

            Uri file = Uri.parse(imageUri.toString());

            if (imageUri != null) {

                String uid = mAuth.getCurrentUser().getUid();
                Log.d("Settings",uid);
                final StorageReference ref = mStorage.getReference().child("profileImages/" + uid);

                uploadTask = ref.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorage.getReference().child("profileImages/" + uid).getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mDatabase.getReference().child("Users").child(uid).child(AccountFragment.PATH_IMAGE).setValue(uri.toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                binding.loadingBarSettings.setVisibility(View.GONE);
                                                                userInfoSaved();
                                                            }
                                                        });

                                    }
                                });
                    }
                });

            }
        }

        public static Intent intentSettingActivity (Context activity){
            Intent intent = new Intent(activity, SettingsActivity.class);
            return intent;
        }

    private void deleteEventFromDatabase(Event event){
        StorageReference storageRef = mStorage.getReference().child("EventImages/"+event.getImageName());


        DatabaseReference mRef  = mDatabase.getReference().child("Events");

        Query query = mRef.orderByKey().equalTo(event.getName());

        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("MyEvent","Delete storage succssful");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot eventSnapshot: snapshot.getChildren()) {
                            eventSnapshot.getRef().removeValue();
                            HomeRepo.eventList.remove(event);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });





    }

    private void deleteUserFromDatabase(String userID){
        StorageReference storageRef = mStorage.getReference().child("profileImages/"+userID);

        DatabaseReference mRef  = mDatabase.getReference().child("Users");
        Query query = mRef.orderByKey().equalTo(userID);
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("UserDelete","Delete storage succssful");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot eventSnapshot: snapshot.getChildren()) {
                            eventSnapshot.getRef().removeValue();
                            binding.loadingBarSettings.setVisibility(View.GONE);
                            startActivity(StartActivity.startActivityIntent(SettingsActivity.this));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });




    }
    private void deleteFromAuth(){
        mAuth.getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Settings","user delete");


                    }
                });
    }


        public static Intent settingActivityIntent (Context activity){
            Intent intent = new Intent(activity, SettingsActivity.class);
            return intent;
        }

    }
