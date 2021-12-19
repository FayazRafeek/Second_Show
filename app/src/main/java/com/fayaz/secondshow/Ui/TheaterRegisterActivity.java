package com.fayaz.secondshow.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.secondshow.Models.Theater;
import com.fayaz.secondshow.databinding.ActivityTheaterRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TheaterRegisterActivity extends AppCompatActivity {

    ActivityTheaterRegisterBinding binding;

    private static final String TAG = "Theater register";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTheaterRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tLoginSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TheaterRegisterActivity.this, TheaterLoginActivity.class));
                finish();
            }
        });

        binding.tRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Theater theater = getInputTheater();
                startRegister(theater);
            }
        });


    }

    Theater getInputTheater() {
        String name = binding.tRNameInp.getText().toString().trim();
        String location = binding.tRLocationInp.getText().toString().trim();
        String email = binding.tREmailInp.getText().toString().trim();
        String password = binding.tRPassInp.getText().toString().trim();
        return new Theater(name,location,email,password);
    }

    FirebaseAuth mAuth;
    FirebaseFirestore mDb;
    void startRegister(Theater theater){

        showProgress();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(theater.getEmail(),theater.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String userId = mAuth.getUid();
                            theater.setTheaterId(userId);

                            mDb = FirebaseFirestore.getInstance();

                            mDb.collection("Theaters").document(userId).set(theater)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            stopProgress();
                                            if(task.isSuccessful()){

                                                showToast("Theater registration was successfull");

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(TheaterRegisterActivity.this,TheaterLoginActivity.class));
                                                        finish();
                                                    }
                                                },1000);

                                            } else showToast("Failed to add theater to database");
                                        }
                                    });

                        } else {
                            stopProgress();
                            Log.e(TAG, "onComplete: Error",task.getException() );
                            showToast("Failed to authenticate theater");
                        }
                    }
                });

    }


    void showToast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TheaterRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showProgress() {
        binding.tRProgress.setVisibility(View.VISIBLE);
    }

    void stopProgress() {
        binding.tRProgress.setVisibility(View.GONE);
    }
}
