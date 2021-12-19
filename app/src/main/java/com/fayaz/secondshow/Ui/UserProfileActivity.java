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
import com.fayaz.secondshow.Models.User;
import com.fayaz.secondshow.databinding.ActivityUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class UserProfileActivity extends AppCompatActivity {


    ActivityUserProfileBinding binding;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogout();
            }
        });

        SharedPreferences pref = getSharedPreferences("PREFS",MODE_PRIVATE);
        String recent = pref.getString("USER","");
        Log.d("THEATER JSON", "getShowInput: " + recent);
        if(!recent.equals("")) {
            Gson gson = new Gson();
            user = gson.fromJson(recent, User.class);
        } else finish();

        binding.userName.setText(user.getName());
        binding.userEmail.setText(user.getEmail());
    }

    FirebaseAuth mAuth;
    void startLogout() {

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGGED_IN",false);
        editor.apply();

        startActivity(new Intent(UserProfileActivity.this, AuthSelectActivity.class)); finish();
    }
}
