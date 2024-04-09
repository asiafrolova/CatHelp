package com.example.cathelp.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class Event {
    private String name;
    private String description;
    private String id;
    private String imageUri;

    private double price;
    private boolean floorFemale = true;

    public Event(final String name, final String description, final String id, final String imageUri, final boolean floorFemale, final double price) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.imageUri = imageUri;
        this.floorFemale = floorFemale;
        this.price = price;
    }

    public boolean isFloor() {
        return this.floorFemale;
    }

    public void setFloorFemale(final boolean floorFemale) {
        this.floorFemale = floorFemale;
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final Event event = (Event) o;
        return 0 == Double.compare(price, event.price) && Objects.equals(this.name, event.name) && Objects.equals(this.description, event.description) && Objects.equals(this.id, event.id) && Objects.equals(this.imageUri, event.imageUri) && Objects.equals(this.floorFemale, event.floorFemale);
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
                ", floorFemale='" + floorFemale + '\'' +
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
        Glide.with(imageView)
                .load(imageUri)
                .fitCenter()
                .into(imageView);
    }
}
