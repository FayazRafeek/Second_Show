package com.fayaz.secondshow.Ui;

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


import com.fayaz.secondshow.Models.User;
import com.fayaz.secondshow.databinding.ActivityUserLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


public class UserLoginActivity  extends AppCompatActivity {

    ActivityUserLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.uRegisterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginActivity.this, UserRegisterActivity.class));
                finish();
            }
        });


        binding.uLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.uLEmailInp.getText().toString();
                String password = binding.uLPassInp.getText().toString();

                startLogin(email,password);
            }
        });
    }

    FirebaseAuth mAuth;
    void startLogin(String email, String pass){

        mAuth = FirebaseAuth.getInstance();

        showProgress();

        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            fetchUserDetail();

                        } else {
                            showToast("Failed to authenticate !");
                            Log.e("LOGIN_ERROR", "onComplete: Failed",task.getException() );
                        }
                    }
                });


    }

    void saveToPref(User user) {
        SharedPreferences sharedPref = getSharedPreferences("PREFS",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGGED_IN",true);
        editor.putString("LOGIN_TYPE","User");
        editor.putString("UID",user.getuId());
        Gson gson = new Gson();
        String objString = gson.toJson(user);
        editor.putString("USER",objString);

        editor.apply();
    }

    void showToast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UserLoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    void showProgress() {
        binding.uLProgress.setVisibility(View.VISIBLE);
    }

    void stopProgress() {
        binding.uLProgress.setVisibility(View.GONE);
    }

    FirebaseFirestore db;
    void fetchUserDetail() {

        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            User user = task.getResult().toObject(User.class);

                            showToast("Successfully logged in");
                            saveToPref(user);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(UserLoginActivity.this,UserHomeActivity.class));
                                    finish();
                                }
                            },1000);

                        } else
                            Toast.makeText(UserLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}

