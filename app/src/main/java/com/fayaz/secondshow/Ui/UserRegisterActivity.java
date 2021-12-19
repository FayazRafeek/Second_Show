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

import com.fayaz.secondshow.Models.User;
import com.fayaz.secondshow.databinding.ActivityUserRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRegisterActivity extends AppCompatActivity {

    private static final String TAG = "User Register Activity";
    ActivityUserRegisterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.uLoginSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegisterActivity.this, UserLoginActivity.class));
                finish();
            }
        });

        binding.uRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = getInputUser();
                startRegister(user);
            }
        });
    }

    User getInputUser() {
        String name = binding.uRNameInp.getText().toString().trim();
        String email = binding.uREmailInp.getText().toString().trim();
        String password = binding.uRPassInp.getText().toString().trim();
        return new User(name,email,password);
    }

    FirebaseAuth mAuth;
    FirebaseFirestore mDb;
    void startRegister(User user){

        showProgress();

        Log.d(TAG, "startRegister: User => " + user.getEmail());

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String userId = mAuth.getUid();
                            user.setuId(userId);

                            mDb = FirebaseFirestore.getInstance();

                            mDb.collection("Users").document(userId).set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            stopProgress();
                                            if(task.isSuccessful()){

                                                showToast("User registration was successfull");

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(UserRegisterActivity.this,UserLoginActivity.class));
                                                        finish();
                                                    }
                                                },1000);

                                            } else showToast("Failed to add user to database");
                                        }
                                    });

                        } else {
                            stopProgress();
                            Log.e(TAG, "onComplete: Error",task.getException() );
                            showToast("Failed to authenticate user");
                        }
                    }
                });

    }

    void showToast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UserRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showProgress() {
        binding.uRProgress.setVisibility(View.VISIBLE);
    }

    void stopProgress() {
        binding.uRProgress.setVisibility(View.GONE);
    }
}
