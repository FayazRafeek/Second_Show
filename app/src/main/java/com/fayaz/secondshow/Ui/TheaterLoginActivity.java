package com.fayaz.secondshow.Ui;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.secondshow.Models.Theater;
import com.fayaz.secondshow.databinding.ActivityTheaterLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class TheaterLoginActivity extends AppCompatActivity {

    ActivityTheaterLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTheaterLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tRegisterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TheaterLoginActivity.this, TheaterRegisterActivity.class));
                finish();
            }
        });


        binding.tLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.tLEmailInp.getText().toString();
                String pass = binding.tLPassInp.getText().toString();

                startLogin(email,pass);
            }
        });
    }


    FirebaseAuth mAuth;
    void startLogin(String email,String pass){


        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            fetchTheater();
                        } else {
                            showToast(task.getException().getMessage());
                        }
                    }
                });

    }

    void saveToPref(String tId) {
        SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGGED_IN",true);
        editor.putString("LOGIN_TYPE","Theater");
        editor.putString("TID",tId);
        editor.apply();
    }

    FirebaseFirestore db;
    void fetchTheater() {

        db = FirebaseFirestore.getInstance();

        Log.d("CHECK", "fetchTheater: " + mAuth.getUid());
        db.collection("Theaters")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            Theater theater = task.getResult().toObject(Theater.class);
                            Gson gson = new Gson();
                            String objString = gson.toJson(theater);
                            Log.d("CHECK", "onComplete: THEARER : " + task.getResult());
                            SharedPreferences pref = getSharedPreferences("PREFS",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("THEATER",objString);
                            editor.apply();

                            showToast("Log in successfull");
                            saveToPref(mAuth.getUid());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(TheaterLoginActivity.this,TheaterHomeActivity.class));
                                    finish();

                                }
                            },800);

                        } else
                            Toast.makeText(TheaterLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    void showToast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TheaterLoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showProgress() {
        binding.tLProgress.setVisibility(View.VISIBLE);
    }

    void stopProgress() {
        binding.tLProgress.setVisibility(View.GONE);
    }
}
