package com.example.cathelp.adapters;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cathelp.databinding.EventsItemBinding;
import com.example.cathelp.databinding.MyEventItemBinding;
import com.example.cathelp.model.Event;

public class MyEventAdapter extends ListAdapter<Event, MyEventAdapter.MyEventViewHolder> {

    MyEventInterface myEventInterface;
    public MyEventAdapter(MyEventInterface myEventInterface) {

        super(Event.itemCallback);
        this.myEventInterface = myEventInterface;

    }

    class MyEventViewHolder extends RecyclerView.ViewHolder {
        MyEventItemBinding myEventItemBinding;

        public MyEventViewHolder(MyEventItemBinding binding) {


            super(binding.getRoot());
            this.myEventItemBinding= binding;


        }
    }



    @NonNull
    @Override
    public MyEventAdapter.MyEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MyEventItemBinding myEventItemBinding = MyEventItemBinding.inflate(layoutInflater,parent,false);

        myEventItemBinding.setEventInterface(myEventInterface);
        return new MyEventViewHolder(myEventItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventAdapter.MyEventViewHolder holder, int position) {
        Event event = getItem(position);
        holder.myEventItemBinding.setEvent(event);
        holder.myEventItemBinding.executePendingBindings();
    }
    public interface MyEventInterface{
        void deleteItem(Event event);
        void onItemSelected(Event event);
        void onMyItemClick(Event event);


    }
}
