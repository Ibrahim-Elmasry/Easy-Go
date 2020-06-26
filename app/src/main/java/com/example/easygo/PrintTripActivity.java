package com.example.easygo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.easygo.Models.DbModels.BookedTrips;
import com.example.easygo.Models.UserModel;
import com.example.easygo.Utils.XmlToPdf;

import java.util.Random;

public class PrintTripActivity extends AppCompatActivity {
    private TextView tvTitle,tvName,tvCode,tvDuration, tvStartAt, tvEndAt, tvPrograms, tvLandmarks,
            tvDescription, tvCount, tvPrice , tvCompany , tvPrint , tvHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_trip);
        init();
        deploy();
    }

    @Override
    public void onBackPressed() {

    }

    private void init() {
        tvTitle = findViewById(R.id.printTrip_title);
        tvName = findViewById(R.id.printTrip_name);
        tvCode = findViewById(R.id.printTrip_code);
        tvDuration = findViewById(R.id.printTrip_duration);
        tvStartAt = findViewById(R.id.printTrip_startAt);
        tvEndAt = findViewById(R.id.printTrip_endAt);
        tvPrograms = findViewById(R.id.printTrip_program);
        tvLandmarks = findViewById(R.id.printTrip_landMark);
        tvCompany = findViewById(R.id.printTrip_company);
        tvDescription = findViewById(R.id.printTrip_desc);
        tvCount = findViewById(R.id.printTrip_adultCount);
        tvPrice = findViewById(R.id.printTrip_price);
        tvPrint = findViewById(R.id.printTrip_printBtn);
        tvHome = findViewById(R.id.printTrip_home);
    }
    protected String getSaltString() {
        String temp = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * temp.length());
            salt.append(temp.charAt(index));
        }
        return salt.toString();

    }

    private void deploy() {
        final BookedTrips model = this.getIntent().getParcelableExtra("tripModel");

        tvTitle.setText(model.getFlight().getTitle().toUpperCase());

        UserModel user = ((UserClient) (getApplicationContext())).getUser();
        if (user != null){
            tvName.setText(user.getName());
        }
        tvCode.setText(getSaltString());

        tvDuration.setText(model.getFlight().getDuration());
        tvStartAt.setText(model.getFlight().getStartAt());
        tvEndAt.setText(model.getFlight().getEndAt());

        for (int i = 0 ; i<model.getFlight().getPrograms().size();i++){
            tvPrograms.append(model.getFlight().getPrograms().get(i) + " , ");
        }

        for (int i = 0 ; i<model.getFlight().getCmCity().getLandMarks().size();i++){
            tvLandmarks.append(model.getFlight().getCmCity().getLandMarks().get(i) + " , ");
        }
        tvDescription.setText(model.getFlight().getDescription());
        tvCount.setText(model.getCount());
        tvPrice.setText(model.getFlight().getPrice());
        tvCompany.setText(model.getFlight().getFcmCompany().getTitle());

        tvPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XmlToPdf(PrintTripActivity.this).createPdf(findViewById(R.id.printTripLinear),model.getTripId());
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrintTripActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }
}
