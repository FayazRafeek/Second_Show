package com.fayaz.secondshow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fayaz.secondshow.Models.Booking;
import com.fayaz.secondshow.databinding.BookingItemBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingVH> {

    List<Booking> items;

    public BookingAdapter(List<Booking> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BookingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingVH(BookingItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingVH holder, int position) {

        Booking item = items.get(position);
        holder.binding.bukShowName.setText(item.getShowName());
        holder.binding.bukShowLoc.setText(item.getTheaterName());
        String selectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(item.getDate()));
        holder.binding.date.setText(selectedDate);
        holder.binding.timing.setText(item.getTiming());
        holder.binding.price.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void updateList(List<Booking> newItems){
        this.items = newItems;
        notifyDataSetChanged();
    }

    class BookingVH extends RecyclerView.ViewHolder{

        BookingItemBinding binding;
        public BookingVH(@NonNull BookingItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
