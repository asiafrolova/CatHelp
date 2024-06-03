package com.example.cathelp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cathelp.databinding.EventsItemBinding;
import com.example.cathelp.model.Event;

public class HomeAdapter extends ListAdapter<Event, HomeAdapter.HomeViewHolder> {

    HomeInterface homeInterface;
    public HomeAdapter(HomeInterface homeInterface) {

        super(Event.itemCallback);
        this.homeInterface = homeInterface;

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

        eventsItemBinding.setHomeInterface(homeInterface);
        return new HomeViewHolder(eventsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
        Event event = getItem(position);
        holder.eventsItemBinding.setEvent(event);
        holder.eventsItemBinding.executePendingBindings();
    }
    public interface HomeInterface{
        void addItem(Event event);
        void onItemClick(Event event);


    }
}
