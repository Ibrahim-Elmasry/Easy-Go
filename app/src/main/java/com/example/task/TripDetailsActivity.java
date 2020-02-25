package com.example.task;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class TripDetailsActivity extends AppCompatActivity {
    private TextView tvTitle, tvNum, tvPrice, tvDest, tvDepart, tvReturn, tvDescription, tvCompany;
    private String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        tvTitle = findViewById(R.id.details_trip_title);
        tvNum = findViewById(R.id.details_trip_number);
        tvPrice = findViewById(R.id.details_trip_price);
        tvDest = findViewById(R.id.details_trip_Destin);
        tvDepart = findViewById(R.id.details_trip_departure);
        tvReturn = findViewById(R.id.details_trip_return);
        tvDescription = findViewById(R.id.details_trip_Descrip);
        tvCompany = findViewById(R.id.details_trip_company);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TripModel model = this.getIntent().getParcelableExtra("mModel");
        tripId = this.getIntent().getStringExtra("idz");

        setTitle(model.getTrip_title()+" to "+model.getDestination());


        tvTitle.setText("From : "+model.getTrip_title());
        tvNum.setText("Number : "+model.getTrip_number());
        tvPrice.setText("Price :  "+model.getPrice());
        tvDest.setText("Destination : "+model.getDestination());
        tvDepart.setText("Departure : "+model.getDeparture_time());
        tvReturn.setText("Return : "+model.getReturn_time());
        tvDescription.setText("Description : "+model.getDescription());
        tvCompany.setText("Company : "+model.getCompany());

    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
