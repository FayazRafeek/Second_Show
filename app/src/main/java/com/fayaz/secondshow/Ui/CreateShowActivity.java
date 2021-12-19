package com.fayaz.secondshow.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.secondshow.Models.Show;
import com.fayaz.secondshow.Models.Theater;
import com.fayaz.secondshow.SingletonRepo;
import com.fayaz.secondshow.databinding.ActivityCreateShowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class CreateShowActivity extends AppCompatActivity {

    ActivityCreateShowBinding binding;
    Theater theater;

    Boolean isEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("IS_EDIT",false);

        SharedPreferences pref = getSharedPreferences("PREFS",MODE_PRIVATE);
        String recent = pref.getString("THEATER","");
        Log.d("THEATER JSON", "getShowInput: " + recent);
        if(!recent.equals("")) {
            Gson gson = new Gson();
            theater = gson.fromJson(recent, Theater.class);
        }

        if(isEdit)
            updateEditUi();

        binding.createShowSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Show show = getShowInput();
                    if(show != null)
                        addShowToDb(show);
                    else
                        Toast.makeText(CreateShowActivity.this, "Input error", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("ERROR", "onClick: ", e);
                }

            }
        });
    }

    Show getShowInput() {

        String showName = binding.crMName.getText().toString();
        String rating = binding.crMRating.getText().toString();
        String price = binding.crMPrice.getText().toString();
        String desc = binding.crMDesc.getText().toString();

        String showId = "";
        if(isEdit) {
            if (editShow.getShowId() != null)
                showId = editShow.getShowId();
        }
        else
            showId = String.valueOf(new Date().getTime());

        ArrayList<String> shows = new ArrayList<>();

        if(binding.crMShowMatnee.isChecked()) shows.add("Matinee");
        if(binding.crMShowFirstShow.isChecked()) shows.add("First Show");
        if(binding.crMShowSecondShow.isChecked()) shows.add("Second Show");


       Show show =  new Show(showId,theater.getTheaterId(),showName,rating,price,shows,theater.getTheaterName());
       show.setDesc(desc);
       return show;

    }


    FirebaseFirestore mDb;

    void addShowToDb(Show show){

        mDb = FirebaseFirestore.getInstance();

        showProgress();

        mDb.collection("Shows")
                .document(show.getShowId())
                .set(show)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        stopProgress();

                        if(task.isSuccessful()){
                            Toast.makeText(CreateShowActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(CreateShowActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    Show editShow;
    void updateEditUi(){

        editShow = SingletonRepo.getInstance().getEditShow();
        if(editShow == null)
            return;

        binding.pageTitle.setText("Edit show");
        binding.createShowSubmit.setText("SUBMIT");
        if(editShow.getShowName() != null)
            binding.crMName.setText(editShow.getShowName());
        if(editShow.getPrice() != null)
            binding.crMPrice.setText(editShow.getPrice());
        if(editShow.getRating() != null)
            binding.crMRating.setText(editShow.getRating());
        if(editShow.getDesc() != null)
            binding.crMDesc.setText(editShow.getDesc());

        if(editShow.getShows() != null){

            for (String s : editShow.getShows()){

                switch (s){
                    case "Matinee" :
                        binding.crMShowMatnee.setChecked(true);
                        break;
                    case "First Show" :
                        binding.crMShowFirstShow.setChecked(true);
                        break;
                    case "Second Show" :
                        binding.crMShowSecondShow.setChecked(true);
                        break;
                }
            }
        }
    }

    void showProgress() {binding.crMProgress.setProgress(View.VISIBLE);}
    void stopProgress() {binding.crMProgress.setProgress(View.GONE);}
}
