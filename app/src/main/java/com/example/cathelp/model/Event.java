package com.example.cathelp.model;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cathelp.R;
import com.example.cathelp.view.fragments.SmallMapsFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Event {
    private String name = "";
    private String description = "";
    private String id = "0";
    private String author;
    private String imageUri = "https://media.istockphoto.com/id/1270583177/vector/animal-pet-dog-icon-gray-vector-icon-design-is-isolated-on-a-white-background.jpg?s=170667a&w=0&k=20&c=6e0h-5Yp9hlPEzgrmxnMmKSno_3d4mEgh7w0OPZYbTo=";

    private String imageName;
    private String addres;
    private String animalType;
    private double latitude;
    private double longitude;
    private double price = 0;
    private LatLng latLng;
    private String connection;

    public LatLng getLatLng() {
        return this.latLng;
    }

    public void setLatLng(final LatLng latLng) {
        this.latLng = latLng;
    }

    public Event(final String name, final String description, final String id, final String author, final String imageUri, final String imageName, final String addres, final String animalType, final double latitude, final double longitude, final double price,final String connection) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.author = author;
        this.imageUri = imageUri;
        this.imageName = imageName;
        this.addres = addres;
        this.animalType = animalType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.latLng = new LatLng(latitude,longitude);
        this.connection = connection;
    }

    public String getConnection() {
        return this.connection;
    }

    public void setConnection(final String connection) {
        this.connection = connection;
    }

    public String getAnimalType() {
        return this.animalType;
    }

    public void setAnimalType(final String animalType) {
        this.animalType = animalType;
    }

    public String getAddres() {
        return this.addres;
    }

    public void setAddres(final String addres) {
        this.addres = addres;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }


    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(final String imageName) {
        this.imageName = imageName;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public Event() {
    }





    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(final String imageUri) {
        this.imageUri = imageUri;
    }



    public double getPrice() {
        return this.price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        obj = (Event)obj;
        return  this.getId()==((Event) obj).getId();

    }

    /*@Override
    public int hashCode() {
        return Objects.hash(this.name, this.description, this.id, this.imageUri, this.floorUri, this.price);
    }*/

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", price=" + price +
                '}';
    }
    public static DiffUtil.ItemCallback<Event> itemCallback = new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.equals(newItem);
        }
    };
    @BindingAdapter("android:eventImage")

    public static void loadImage(ImageView imageView, String imageUri){
        imageView.setBackgroundResource(R.drawable.animal_default);


        imageView.setClipToOutline(true);
        imageView.setBackground(null);
        Glide.with(imageView)
                .load(imageUri)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(imageView);
    }
    @BindingAdapter("android:authorName")

    public static void getAuthorName(TextView textView,String uid){
        DatabaseReference mRef  = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @BindingAdapter("android:authorProfile")

    public static void getAuthorImage(ImageView imageView, String uid){

        DatabaseReference mRef  = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("image").getValue().toString().isEmpty()){
                    Glide.with(imageView.getContext())
                            .load("https://i.pinimg.com/474x/bd/18/cb/bd18cb31dc17600e58570dbf53ce38da.jpg")
                            .fitCenter()
                            .into(imageView);
                }else {
                    Glide.with(imageView.getContext())
                            .load(snapshot.child("image").getValue().toString())
                            .fitCenter()

                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @BindingAdapter("android:geolocation")

    public static void getGeolocation(FragmentContainerView fragmentContainerView, Event event){
        SmallMapsFragment.setMapLocation(fragmentContainerView.getFragment(),event);


    }
}
