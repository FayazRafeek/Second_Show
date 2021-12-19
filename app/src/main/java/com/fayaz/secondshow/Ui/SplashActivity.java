package com.fayaz.secondshow.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.secondshow.R;

public class SplashActivity extends AppCompatActivity {


    SharedPreferences sharedPref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        sharedPref = getSharedPreferences("PREFS",Context.MODE_PRIVATE);

        if(isLoggedIn()){

            switch (getLogintype()){
                case "User" :
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this,UserHomeActivity.class));
                            finish();
                        }
                    },2000);
                    break;
                case "Theater" :
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this,TheaterHomeActivity.class));
                            finish();
                        }
                    },2000);

                    break;

                default:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this,AuthSelectActivity.class));
                            finish();
                        }
                    },2000);
            }

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,AuthSelectActivity.class));
                    finish();
                }
            },2000);
        }


    }

    Boolean isLoggedIn() {
        return sharedPref.getBoolean("IS_LOGGED_IN",false);
    }

    String getLogintype() {
        return sharedPref.getString("LOGIN_TYPE","empty");
    }

}
