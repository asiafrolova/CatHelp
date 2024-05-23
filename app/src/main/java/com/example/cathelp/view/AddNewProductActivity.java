package com.example.cathelp.view;

import static com.example.cathelp.view.StartActivity.APP_PREFERENCES_LOCALE;
import static com.example.cathelp.view.StartActivity.mSettings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.cathelp.R;
import com.example.cathelp.databinding.ActivityAddNewProductBinding;
import com.example.cathelp.repositories.EventRepo;
import com.example.cathelp.repositories.HomeRepo;
import com.example.cathelp.model.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;
import com.sucho.placepicker.PlacePickerActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vanillaplacepicker.data.VanillaAddress;
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker;

import com.vanillaplacepicker.utils.PickerLanguage;
import com.vanillaplacepicker.utils.PickerType;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddNewProductActivity extends AppCompatActivity {
    private ActivityAddNewProductBinding binding;
    private static final int GALLERY = 1;

    private Uri ImageUri;
    private String description, price, eventName, author, imageName, adress, animalType, connection;
    private double latitude = 0, longitude = 0;
    private String saveCurrentDate, saveCurrentTime;
    private String eventRandomKey;
    private StorageReference EventImageRef;
    private int radioButtonID;
    private int selectedIndex;
    private String downloadImageUrl;
    private DatabaseReference eventsRef;
    private FusedLocationProviderClient fusedLocationClient;
    CustomAdapter spinnerAdapter;
    Resources res ;
    private String[] animalTypes ;
    private String[] animalTypesEN ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddNewProductBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        res = this.getResources();
        animalTypes = new String[]{res.getString(R.string.cat)
                , res.getString(R.string.dog), res.getString(R.string.parrot), res.getString(R.string.fish), res.getString(R.string.exotic)};
        animalTypesEN = new String[]{"cat"
                , "dog", "parrot","fish", "exotic"};
        init();
        spinnerAdapter = new CustomAdapter(this,
                R.layout.spinner_item, animalTypes);
        binding.spinnerType.setAdapter(spinnerAdapter);
        binding.spinnerType.setSelection(4);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                animalType = animalTypesEN[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.selectImage.setOnClickListener(v -> {
            //OpenGallery();
            CropImage.activity(ImageUri)
                    .setAspectRatio(1, 1)
                    .start(AddNewProductActivity.this);
        });
        binding.saveBtn.setOnClickListener(v -> {
            ValidateEventData();
        });
        binding.groupButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonID = binding.groupButton.getCheckedRadioButtonId();
                selectedIndex = (radioButtonID != -1) ? binding.groupButton.indexOfChild(findViewById(radioButtonID)) : -1;
                if (selectedIndex == 1) {
                    binding.selectPrice.setVisibility(View.VISIBLE);
                } else {
                    binding.selectPrice.setVisibility(View.GONE);
                }
            }
        });
        binding.mapAdd.setOnClickListener(v -> {
            binding.loadingBarAdd.setVisibility(View.VISIBLE);
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });

        });

    }
    private ActivityResultLauncher<Intent> placePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == Activity.RESULT_OK){

                        Intent data = o.getData();
                        if (o.getResultCode() == Activity.RESULT_OK && data != null) {
                            VanillaAddress vanillaAddress = VanillaPlacePicker.Companion.getPlaceResult(data);
                        }
                        if(data!=null){
                            latitude = data.getDoubleExtra("latitude",0.0);
                            longitude = data.getDoubleExtra("longitude",0.0);
                            adress = data.getStringExtra("address");

                            Log.d("PLACE",latitude+ " LAT "+longitude+" LONG "+adress+" ADD ");
                        }
                    }else{
                        Log.d("PLACE","Error");
                    }
                }
            }
    );

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {
                                               latitude = location.getLatitude();
                                               longitude = location.getLongitude();
                                               showPlacePicker(longitude,latitude);
                                            }
                                        }
                                    });


                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {
                                                latitude = location.getLatitude();
                                                longitude = location.getLongitude();
                                                showPlacePicker(longitude,latitude);
                                            }
                                        }
                                    });


                        } else {
                            showPlacePicker(longitude,latitude);

                        }
                    }
            );





    private void showPlacePicker(double longitude,double latitude) {
        PlacePickerActivity activity = new PlacePickerActivity();
            Intent intent = new PlacePicker.IntentBuilder()
                    .setLatLong(latitude, longitude)
                    .setFabColor(R.color.new_pink)
                    .setMarkerDrawable(R.drawable.map_pin)
                    .setMarkerImageImageColor(R.color.new_pink)
                    .setPrimaryTextColor(R.color.black)
                    .setSecondaryTextColor(R.color.grey)
                    .showLatLong(true)
                    .setMapRawResourceStyle(R.raw.map_style)
                    .setMapType(MapType.NORMAL)
                    .hideLocationButton(true)
                    .build(AddNewProductActivity.this);

            startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);


    }


    private void ValidateEventData() {

        connection = binding.selectConnection.getText().toString();
        description = binding.selectDescription.getText().toString();
        eventName = binding.selectName.getText().toString();
        radioButtonID = binding.groupButton.getCheckedRadioButtonId();
        selectedIndex = (radioButtonID != -1) ? binding.groupButton.indexOfChild(findViewById(radioButtonID)) : -1;
        author = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adress = binding.textAdress.getText().toString();
        if (selectedIndex==1){
            price  = binding.selectPrice.getText().toString();
        }else{
            price = "0";
        }

        for (Event event : HomeRepo.eventList) {
            if(event.getName().equals(eventName)){
                Toast.makeText(this, R.string.an_event_with_the_same_name_already_exists,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ImageUri == null){
            Toast.makeText(this, R.string.add_an_image,Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, R.string.add_a_description,Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(connection)) {
            Toast.makeText(this, R.string.how_to_contact_you,Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(eventName)){
            Toast.makeText(this, R.string.add_a_title,Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price) && selectedIndex==0){
            Toast.makeText(this, R.string.add_a_price,Toast.LENGTH_SHORT).show();
        }else if(adress.equals(getString(R.string.address_not_selected))){
            Toast.makeText(this, R.string.add_an_address,Toast.LENGTH_SHORT).show();
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
        StorageReference filePath = EventImageRef.child(ImageUri.getLastPathSegment() + eventRandomKey );
        imageName =  ImageUri.getLastPathSegment() + eventRandomKey;
        UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddNewProductActivity.this,getString(R.string.error)+message,Toast.LENGTH_SHORT).show();
                binding.loadingBar.setVisibility(ProgressBar.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        filePath.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadImageUrl = uri.toString();
                                        SaveEventInfoToDataBase();
                                        //eventsRef.child("Events").child(eventName).child(AccountFragment.PATH_IMAGE).setValue(uri.toString());
                                    }
                                });
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddNewProductActivity.this, R.string.image_save,Toast.LENGTH_SHORT).show();




                        }

                    }


                });
            }
        });




            }

            private void SaveEventInfoToDataBase() {
                HashMap<String, Object> eventMap = new HashMap<>();



                eventMap.put("eid", eventRandomKey);
                eventMap.put("date", saveCurrentDate);
                eventMap.put("time", saveCurrentTime);
                eventMap.put("description", description);
                eventMap.put("image",downloadImageUrl);
                //eventMap.put("category", categoryName);
                eventMap.put("price", price);
                eventMap.put("eventName", eventName);
                eventMap.put("author",author);
                eventMap.put("imageName",imageName);
                eventMap.put("address",adress);
                eventMap.put("latitude",latitude);
                eventMap.put("longitude",longitude);
                eventMap.put("animalType",animalType);
                eventMap.put("connection",connection);

                eventsRef.child(eventName).updateChildren(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent homeIntent = new Intent(AddNewProductActivity.this, HomeActivity.class);
                            startActivity(homeIntent);

                            //new HomeRepo().loadEvents();
                            HomeRepo.eventList.add(new Event(eventName,description,eventRandomKey,author,downloadImageUrl,imageName,adress,animalType,latitude,longitude,Integer.parseInt(price),connection));


                            Toast.makeText(AddNewProductActivity.this, R.string.event_added, Toast.LENGTH_SHORT).show();
                            binding.loadingBar.setVisibility(ProgressBar.GONE);
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(AddNewProductActivity.this, R.string.error + message, Toast.LENGTH_SHORT).show();
                            binding.loadingBar.setVisibility(ProgressBar.GONE);
                        }

                    }
                });

            }

            private void OpenGallery() {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY);
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                //super.onActivityResult(requestCode, resultCode, data);

                /*if (requestCode == GALLERY && resultCode == RESULT_OK && data != null) {
                    ImageUri = data.getData();
                    binding.selectImage.setImageURI(ImageUri);
                }*/



                binding.loadingBarAdd.setVisibility(View.INVISIBLE);
                if (requestCode == Constants.PLACE_PICKER_REQUEST) {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        try {
                            AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                            latitude = addressData.component1();
                            longitude = addressData.component2();
                            binding.textAdress.setText(addressData.component3().get(0).getAddressLine(0).toString());

                        } catch (Exception e) {
                            Log.e("AddEventActivity", e.getMessage());
                        }
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);


                    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        ImageUri = result.getUri();



                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        roundedBitmapDrawable.setCornerRadius(20);
                        binding.selectImage.setImageBitmap(bitmap);

                    }
                }
            }

    public class CustomAdapter extends ArrayAdapter<String> {

        public CustomAdapter(Context context, int textViewResourceId,
                             String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_item, parent, false);
            TextView label = (TextView) row.findViewById(R.id.item);
            label.setText(animalTypes[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.icon);

            if (position == 0) {
                icon.setImageResource(R.drawable.cat);
            } else if (position == 1) {
                icon.setImageResource(R.drawable.dog);
            } else if (position == 2) {
                icon.setImageResource(R.drawable.parrot);
            } else if (position == 3) {
                icon.setImageResource(R.drawable.fish);
            } else {
                icon.setImageResource(R.drawable.salamand);
            }
            return row;
        }

    }




            private void init() {
                EventImageRef = FirebaseStorage.getInstance().getReference().child("EventImages");
                eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");
                animalType = animalTypes[4];


                if(mSettings.contains(APP_PREFERENCES_LOCALE)) {
                    String lang = mSettings.getString(APP_PREFERENCES_LOCALE, "");
                    Locale locale = new Locale(lang);

                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getResources().updateConfiguration(
                            config,
                            getResources().getDisplayMetrics());
                }




            }
        }