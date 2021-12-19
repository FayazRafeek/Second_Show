package com.fayaz.secondshow.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fayaz.secondshow.Models.Show;
import com.fayaz.secondshow.R;
import com.fayaz.secondshow.SingletonRepo;
import com.fayaz.secondshow.databinding.ActivityShowInfoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowInfoActivity extends AppCompatActivity {


    ActivityShowInfoBinding binding;

    String type = "User";
    Show show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        show = SingletonRepo.getInstance().getShowInfo();
        updateUi();

        fetchShow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchShow();
    }

    void updateUi() {

        if(type.equals("User")){
            binding.theaterAction.setVisibility(View.GONE);
            binding.bookBtn.setVisibility(View.VISIBLE);
        } else {
            binding.theaterAction.setVisibility(View.VISIBLE);
            binding.bookBtn.setVisibility(View.GONE);
        }

        Glide
                .with(this)
                .load(show.getPosterUrl())
                .placeholder(R.drawable.poster_placeholder)
                .into(binding.showPoster);

        binding.showName.setText(show.getShowName());
        binding.showLocation.setText("Location : " + show.getTheaterName());
        binding.showRating.setText("rating : " + show.getRating());
        binding.showPrice.setText("ticket price : " + show.getPrice());
        binding.showInfo.setText(show.getDesc() == null? "" : show.getDesc() );

        String timimgs = "";

        for(String s : show.getShows())
            timimgs = timimgs + s + " ";

        binding.showTims.setText("Shows : " + timimgs);

        binding.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingletonRepo.getInstance().setBookShow(show);
                startActivity(new Intent(ShowInfoActivity.this, BookShowActivity.class));
            }
        });

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editShow();
            }
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteShow();
            }
        });


    }

    FirebaseFirestore db;
    void deleteShow(){

        db = FirebaseFirestore.getInstance();

        db.collection("Shows")
                .document(show.getShowId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(ShowInfoActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },500);

                        }
                    }
                });
    }


    void editShow() {

        SingletonRepo.getInstance().setEditShow(show);
        Intent intent = new Intent(ShowInfoActivity.this, CreateShowActivity.class);
        intent.putExtra("IS_EDIT", true);
        startActivity(intent);
    }

    void fetchShow() {

        db = FirebaseFirestore.getInstance();
        db.collection("Shows")
                .document(show.getShowId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Show s = task.getResult().toObject(Show.class);
                            if(s != null) show = s;
                            updateUi();
                        }
                    }
                });
    }
}
