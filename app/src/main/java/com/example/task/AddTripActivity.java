package com.example.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddTripActivity extends AppCompatActivity {

    private EditText edTitle,edNum,edPrice, edDest, edDepart, edReturn, edDescription;
    private String stTitle,stNum,stPrice, stDest, stDepart, stReturn, stDescription;
    private Button addBtn;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDb = FirebaseFirestore.getInstance();

        edTitle = findViewById(R.id.Add_trip_title);
        edNum = findViewById(R.id.Add_trip_number);
        edPrice = findViewById(R.id.Add_trip_price);
        edDest = findViewById(R.id.Add_trip_Destin);
        edDepart = findViewById(R.id.Add_trip_departure);
        edReturn = findViewById(R.id.Add_trip_return);
        edDescription = findViewById(R.id.Add_trip_Descrip);
        addBtn = findViewById(R.id.Add_trip_addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }

    private void Validation() {
        stTitle = edTitle.getText().toString();

        stNum = edNum.getText().toString();
        stPrice = edPrice.getText().toString();
        stDest = edDest.getText().toString();
        stDepart = edDepart.getText().toString();
        stReturn = edReturn.getText().toString();
        stDescription = edDescription.getText().toString();

        if (stTitle.equals("") || stTitle.length() == 0
                ||stNum.equals("") || stNum.length() == 0
                || stPrice.equals("") || stPrice.length() == 0
                || stDest.equals("") || stDest.length() == 0
                || stDepart.equals("") || stDepart.length() == 0
                || stReturn.equals("") || stReturn.length() == 0
                || stDescription.equals("") || stDescription.length() == 0){

        }else {
            postData(stTitle,stNum, stPrice, stDest, stDepart, stReturn, stDescription);
        }


    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddTripActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent i = new Intent(AddTripActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postData(String trip_title,String trip_number, String price, String destination, String departure_time, String return_time, String description){
        final DocumentReference tripRef = mDb
                .collection("trips")
                .document();

        TripModel newTrip = new TripModel(trip_title,trip_number, price, destination, departure_time, return_time, description);
        tripRef.set(newTrip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(AddTripActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {


                }
            }
        });


    }

}
