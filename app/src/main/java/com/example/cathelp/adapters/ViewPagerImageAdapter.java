package com.example.cathelp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cathelp.databinding.ViewpagerItemBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerImageAdapter extends RecyclerView.Adapter<ViewPagerImageAdapter.ViewHolder> {
    ArrayList<String> uris;


    public ViewPagerImageAdapter(final ArrayList<String> uris) {
        this.uris = uris;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewpagerItemBinding viewpagerItemBinding = ViewpagerItemBinding.inflate(layoutInflater,parent,false);

        return  new ViewHolder(viewpagerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUri = uris.get(position);
        WindowManager windowManager = (WindowManager) holder.binding.image.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();


        Picasso.get()
                .load(imageUri)
                .resize(display.getWidth(),display.getWidth())
                .noPlaceholder()
                .centerCrop()
                .into(holder.binding.image, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.binding.loadingBarPager.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.binding.loadingBarPager.setVisibility(View.GONE);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewpagerItemBinding binding;
        public ViewHolder(ViewpagerItemBinding viewpagerItemBinding) {
            super(viewpagerItemBinding.getRoot());
            this.binding = viewpagerItemBinding;
        }
    }
}
