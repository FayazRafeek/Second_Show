package com.fayaz.secondshow.Ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.secondshow.Models.Booking;
import com.fayaz.secondshow.Models.Show;
import com.fayaz.secondshow.SingletonRepo;
import com.fayaz.secondshow.databinding.ActivityBookShowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class BookShowActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ActivityBookShowBinding binding;
    Show show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        show = SingletonRepo.getInstance().getBookShow();
        updateUi();

        binding.datePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.fayaz.secondshow.Ui.DatePicker mDatePicker = new com.fayaz.secondshow.Ui.DatePicker();
                mDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateInputs()){
                    Booking book = generatebooking();
                    submitToDb(book);
                }
            }
        });

    }

    void updateUi(){

        if(show == null) finish();

        if(show.getShows().contains("Matinee"))
            binding.crMShowMatnee.setVisibility(View.VISIBLE);
        if(show.getShows().contains("First Show"))
            binding.crMShowFirstShow.setVisibility(View.VISIBLE);
        if(show.getShows().contains("Second Show"))
            binding.crMShowSecondShow.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        Calendar mCalendar = Calendar.getInstance();

        // Set static variables of Calendar instance

        mCalendar.set(Calendar.YEAR,year);

        mCalendar.set(Calendar.MONTH,month);

        mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        // Get the date in form of string

        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());

        // Set the textview to the selectedDate String

        date = mCalendar.getTime().toString();
        binding.dateText.setText(selectedDate);
    }

    Integer seats;String date,timing;
    Boolean validateInputs(){

        seats = Integer.valueOf(binding.seatInp.getText().toString());

        if(seats > 0)
            if(date != null && date.length() > 0)
                if(getTiming())
                    return true;
        return false;
    }

    Boolean getTiming(){
        if(binding.crMShowMatnee.isChecked()) timing = "Matinee";
        if(binding.crMShowFirstShow.isChecked()) timing = "First Show";
        if(binding.crMShowSecondShow.isChecked()) timing = "Second Show";

        if(timing != null && timing.length() > 0) return  true;
        return false;
    }

    Booking generatebooking(){
        SharedPreferences pref = getSharedPreferences("PREFS",MODE_PRIVATE);
        String uid = pref.getString("UID","");
        String ts = String.valueOf(System.currentTimeMillis());
        String price = String.valueOf(seats * Integer.valueOf(show.getPrice()));
        return new Booking(ts,show.getShowId(),uid,show.getShowName(),show.getTheaterId(),show.getTheaterName(),price,seats,date,timing);
    }


    FirebaseFirestore db;
    void submitToDb(Booking buk){

        showLoading();
        db = FirebaseFirestore.getInstance();
        db.collection("Bookings")
                .document(buk.getBookingId())
                .set(buk)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        hideLoading();
                        if(task.isSuccessful()){
                            Toast.makeText(BookShowActivity.this, "Booking was successfull", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(BookShowActivity.this,UserHomeActivity.class));
                                    finish();
                                }
                            },800);

                        } else
                            Toast.makeText(BookShowActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    void showLoading() {}
    void hideLoading() {}
}
