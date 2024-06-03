package com.example.cathelp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cathelp.databinding.MarksItemBinding;
import com.example.cathelp.model.Event;

public class MarksAdapter extends ListAdapter<Event, MarksAdapter.MarkViewHolder> {
    MarksInterface marksInterface;
    public MarksAdapter(MarksInterface marksInterface) {

        super(Event.itemCallback);
        this.marksInterface = marksInterface;
    }
    class MarkViewHolder extends RecyclerView.ViewHolder{
        MarksItemBinding marksItemBinding;
        public MarkViewHolder(MarksItemBinding itemBinding) {

            super(itemBinding.getRoot());
            this.marksItemBinding = itemBinding;
        }
    }

    @NonNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MarksItemBinding marksItemBinding = MarksItemBinding.inflate(layoutInflater,parent,false);

        marksItemBinding.setMarksInterface(marksInterface);
        return new MarkViewHolder(marksItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder holder, int position) {
        Event event = getItem(position);
        holder.marksItemBinding.setEvent(event);
        holder.marksItemBinding.executePendingBindings();
    }


    public interface MarksInterface{
        void addItemMarks(Event event);
        void onItemClickMarks(Event event);


    }
}
