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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fayaz.secondshow.Adapter.ShowAdapter;
import com.fayaz.secondshow.Models.Show;
import com.fayaz.secondshow.R;
import com.fayaz.secondshow.SingletonRepo;
import com.fayaz.secondshow.databinding.ActivityTheaterHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TheaterHomeActivity extends AppCompatActivity implements ShowAdapter.ShowClick {


    ActivityTheaterHomeBinding binding;
    FirebaseFirestore mDb;
    String theaterId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = FirebaseFirestore.getInstance();
        theaterId = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("TID", "");

        binding = ActivityTheaterHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.createShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TheaterHomeActivity.this, CreateShowActivity.class));
            }
        });

        binding.showSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchShows();
            }
        });

        binding.userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TheaterHomeActivity.this, TheaterProfileActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchShows();
    }



    void fetchShows() {

        binding.showSwipeRefresh.setRefreshing(true);

        CollectionReference colRef = mDb.collection("Shows");
        Query query = colRef.whereEqualTo("theaterId", theaterId);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        binding.showSwipeRefresh.setRefreshing(false);

                        if(task.isSuccessful()){

                            ArrayList<Show> showList = new ArrayList<>();

                            for(QueryDocumentSnapshot doc : task.getResult()){

                                Show show = doc.toObject(Show.class);
                                showList.add(show);

                                updateRecylerView(showList);

                                Log.d("SHOW", "onComplete: SHOW => " + show.getShowName());
                            }

                        } else Toast.makeText(TheaterHomeActivity.this, "Failed to fetch shows", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    ShowAdapter adapter;
    void updateRecylerView(ArrayList<Show> showList) {

        if (adapter == null){

            adapter = new ShowAdapter(showList,"Theater",this);
            binding.tShowRecycler.setLayoutManager(new GridLayoutManager(this,2));
            binding.tShowRecycler.setAdapter(adapter);
        } else {
            adapter.updateItems(showList);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    @Override
    public void onShowClick(Show show) {

        SingletonRepo.getInstance().setShowInfo(show);
        Intent intent = new Intent(this, ShowInfoActivity.class);
        intent.putExtra("type","Theater");
        startActivity(intent);
    }
}
