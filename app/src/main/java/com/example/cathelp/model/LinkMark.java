package com.example.cathelp.model;

import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cathelp.R;
import com.example.cathelp.repositories.EventRepo;

import java.util.concurrent.ExecutionException;

@Entity
public class LinkMark {
    @PrimaryKey
    @NonNull
    private String nameEvent = " ";
    private boolean isMark = true;

    private String author= " ";

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public boolean isMark() {
        return this.isMark;
    }

    public void setMark(final boolean mark) {
        this.isMark = mark;
    }

    public LinkMark(final String nameEvent, String author) {
        this.nameEvent = nameEvent;
        this.author = author;
    }

    public String getNameEvent() {
        return this.nameEvent;
    }

    public void setNameEvent(final String nameEvent) {
        this.nameEvent = nameEvent;
    }


    @BindingAdapter("android:markImage")
    public synchronized static void setLike(ImageButton imageButton, Event event)  {
        EventRepo eventRepo = new EventRepo();
        try {
            if(eventRepo.isLike(event)){
                imageButton.setBackgroundResource(R.drawable.fill_star);

            }else{
                imageButton.setBackgroundResource(R.drawable.star);

            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
