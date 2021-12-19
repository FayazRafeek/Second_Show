package com.fayaz.secondshow.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fayaz.secondshow.Adapter.BookingAdapter;
import com.fayaz.secondshow.Adapter.ShowAdapter;
import com.fayaz.secondshow.Models.Booking;
import com.fayaz.secondshow.Models.Show;
import com.fayaz.secondshow.R;
import com.fayaz.secondshow.SingletonRepo;
import com.fayaz.secondshow.databinding.ActivityUserHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity implements ShowAdapter.ShowClick {


    ActivityUserHomeBinding binding;
    FirebaseFirestore mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = FirebaseFirestore.getInstance();

        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchShows();

        fetchBookings();

        binding.showSwipeRefreshUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchShows();
            }
        });

        binding.userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this,UserProfileActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    void fetchShows() {

        binding.showSwipeRefreshUser.setRefreshing(true);

        CollectionReference colRef = mDb.collection("Shows");

        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        binding.showSwipeRefreshUser.setRefreshing(false);

                        if(task.isSuccessful()){

                            ArrayList<Show> showList = new ArrayList<>();

                            for(QueryDocumentSnapshot doc : task.getResult()){

                                Show show = doc.toObject(Show.class);
                                showList.add(show);

                                updateRecylerView(showList);

                                Log.d("SHOW", "onComplete: SHOW => " + show.getShowName());
                            }

                        } else {
                            Toast.makeText(UserHomeActivity.this, "Failed to fetch shows", Toast.LENGTH_SHORT).show();
                            Log.e("FETCH_ERROR", "onComplete: ", task.getException());
                        }
                    }
                });

    }

    ShowAdapter adapter;
    void updateRecylerView(ArrayList<Show> showList) {

        if (adapter == null){

            adapter = new ShowAdapter(showList,"User",this);
            binding.uShowRecycler.setLayoutManager(new GridLayoutManager(this,2));
            binding.uShowRecycler.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onShowClick(Show show) {
        SingletonRepo.getInstance().setShowInfo(show);
        Intent intent = new Intent(UserHomeActivity.this, ShowInfoActivity.class);
        intent.putExtra("type","User");
        startActivity(intent);
    }

    void fetchBookings() {

        SharedPreferences pref = getSharedPreferences("PREFS",MODE_PRIVATE);
        String uid = pref.getString("UID","");

        mDb.collection("Bookings")
                .whereEqualTo("userId", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Booking> list = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult()){
                                Booking item = doc.toObject(Booking.class);
                                list.add(item);
                            }

                            updateBookingRecycler(list);
                        } else {

                        }
                    }
                });
    }

    BookingAdapter bookingAdapter;
    void updateBookingRecycler(List<Booking> list){

        if(bookingAdapter == null){

            bookingAdapter = new BookingAdapter(list);
            binding.uBookingRecycler.setAdapter(bookingAdapter);
            binding.uBookingRecycler.setLayoutManager(new LinearLayoutManager(this));

        } else bookingAdapter.updateList(list);
    }
}
