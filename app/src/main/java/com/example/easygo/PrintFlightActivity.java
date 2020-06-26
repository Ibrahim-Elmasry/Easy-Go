package com.example.easygo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.easygo.Models.DbModels.BookedFlights;
import com.example.easygo.Models.DbModels.BookedTrips;
import com.example.easygo.Models.UserModel;
import com.example.easygo.Utils.XmlToPdf;
import com.squareup.picasso.Picasso;

import java.util.Random;
import java.util.UUID;

public class PrintFlightActivity extends AppCompatActivity {
    private TextView tvSource,tvName,tvCode,tvDestination,tvPrice,tvClass,tvStartAt,tvEndAt,tvCompany,tvStops,tvRefundable,tvMeal,tvPrint , tvHome ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_flight);
        init();
        deploy();
    }



    @Override
    public void onBackPressed() {

    }

    private void init() {
        tvSource = findViewById(R.id.printFlight_source);
        tvName = findViewById(R.id.printFlight_name);
        tvCode = findViewById(R.id.printFlight_code);
        tvDestination = findViewById(R.id.printFlight_destination);
        tvPrice = findViewById(R.id.printFlight_price);
        tvClass = findViewById(R.id.printFlight_class);
        tvStartAt = findViewById(R.id.printFlight_startAt);
        tvEndAt = findViewById(R.id.printFlight_endAt);
        tvCompany = findViewById(R.id.printFlight_company);
        tvStops = findViewById(R.id.printFlight_stops);
        tvRefundable = findViewById(R.id.printFlight_refund);
        tvMeal = findViewById(R.id.printFlight_meal);
        tvPrint  = findViewById(R.id.printFlight_printBtn);
        tvHome = findViewById(R.id.printFlight_home);
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
        final BookedFlights model = this.getIntent().getParcelableExtra("flightModel");
        tvSource.setText(model.getFlight().getModel().getSource().getName());

        UserModel user = ((UserClient) (getApplicationContext())).getUser();
        if (user != null){
            tvName.setText(user.getName());
        }
        tvCode.setText(getSaltString());
        tvDestination.setText(model.getFlight().getModel().getDestination().getName());
        tvPrice.setText(model.getFlight().getChoice().getTotalPrice()+" L.E");
        tvClass.setText(model.getFlight().getChoice().getClassType());
        tvStartAt.setText(model.getFlight().getModel().getDepartDate());
        tvEndAt.setText(model.getFlight().getModel().getReturnDate());
        tvCompany.setText(model.getFlight().getModel().getCompany().getTitle());
        tvStops.setText(model.getFlight().getModel().getStopsNo());
        tvRefundable.setText(model.getFlight().getModel().getRefund());
        tvMeal.setText(model.getFlight().getModel().getMeal());



        tvPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XmlToPdf(PrintFlightActivity.this).createPdf(findViewById(R.id.printFlightLinear),model.getFlightId());
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrintFlightActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }
}
