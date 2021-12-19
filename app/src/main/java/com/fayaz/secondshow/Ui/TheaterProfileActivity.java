package com.fayaz.secondshow.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.secondshow.Models.Theater;
import com.fayaz.secondshow.databinding.ActivityTheaterProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class TheaterProfileActivity extends AppCompatActivity {


    ActivityTheaterProfileBinding binding;
    Theater theater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityTheaterProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences pref = getSharedPreferences("PREFS",MODE_PRIVATE);
        String recent = pref.getString("THEATER","");
        Log.d("THEATER JSON", "getShowInput: " + recent);
        if(!recent.equals("")) {
            Gson gson = new Gson();
            theater = gson.fromJson(recent, Theater.class);
        }

        binding.userName.setText(theater.getTheaterName());
        binding.userEmail.setText(theater.getEmail());

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogout();
            }
        });
    }

    FirebaseAuth mAuth;
    void startLogout() {

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGGED_IN",false);
        editor.apply();

        startActivity(new Intent(TheaterProfileActivity.this, AuthSelectActivity.class)); finish();

    }
}
