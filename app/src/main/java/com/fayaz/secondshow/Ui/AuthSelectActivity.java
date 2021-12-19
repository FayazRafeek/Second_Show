package com.fayaz.secondshow.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.secondshow.R;
import com.fayaz.secondshow.databinding.ActivityAuthSelectBinding;

public class AuthSelectActivity extends AppCompatActivity {

    ActivityAuthSelectBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAuthSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.userOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthSelectActivity.this, UserLoginActivity.class));
            }
        });

        binding.theaterOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthSelectActivity.this, TheaterLoginActivity.class));
            }
        });
    }
}
