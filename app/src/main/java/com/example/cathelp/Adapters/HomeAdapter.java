package com.example.cathelp.Adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cathelp.model.Event;
import com.example.cathelp.R;
import com.example.cathelp.databinding.EventsItemBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends ListAdapter<Event, HomeAdapter.HomeViewHolder> {

    public HomeAdapter() {
        super(Event.itemCallback);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        EventsItemBinding eventsItemBinding;

        public HomeViewHolder(EventsItemBinding binding) {


            super(binding.getRoot());
            this.eventsItemBinding = binding;
        }
    }



    @NonNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EventsItemBinding eventsItemBinding = EventsItemBinding.inflate(layoutInflater,parent,false);
        return new HomeViewHolder(eventsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
        Event event = getItem(position);
        holder.eventsItemBinding.setEvent(event);
    }
    public interface HomeInterface{
        void addItem(Event event);
        void onItemClick(Event event);
    }
}
