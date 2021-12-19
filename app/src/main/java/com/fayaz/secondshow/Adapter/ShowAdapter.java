package com.fayaz.secondshow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fayaz.secondshow.Models.Show;
import com.fayaz.secondshow.databinding.ShowLayoutBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowsVH> {

    ArrayList<Show> showList = new ArrayList<>();
    String type;
    ShowClick listner;

    public ShowAdapter(ArrayList<Show> showList, String type, ShowClick listner) {
        this.showList = showList;
        this.type = type;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ShowsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShowsVH(ShowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false), listner);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowsVH holder, int position) {

        Show item = showList.get(position);
        holder.binding.showName.setText(item.getShowName());
        holder.binding.showPrice.setText("Ticket price : " +item.getPrice());
        holder.binding.showLocation.setText(item.getTheaterName());
        String rating = item.getRating() + " " + "★";
        holder.binding.showRating.setText(rating);

        switch (type){
            case "User" :
                holder.binding.showPrice.setVisibility(View.GONE);
                holder.binding.showLocation.setVisibility(View.VISIBLE);
                break;
            case "Theater" :
                holder.binding.showPrice.setVisibility(View.VISIBLE);
                holder.binding.showLocation.setVisibility(View.GONE);

        }
    }

    public void updateItems(ArrayList<Show> list) {
        this.showList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return showList != null ? showList.size() : 0;
    }

    public class ShowsVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        ShowLayoutBinding binding;
        ShowClick listner;

        public ShowsVH(@NonNull ShowLayoutBinding binding, ShowClick listner) {
            super(binding.getRoot());
            this.binding = binding;
            this.listner = listner;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listner.onShowClick(showList.get(getAdapterPosition()));
        }
    }

    public interface ShowClick {
        void onShowClick(Show show);
    }
}
