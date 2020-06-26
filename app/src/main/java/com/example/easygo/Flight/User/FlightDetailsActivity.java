package com.example.easygo.Flight.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.BookedFlights;
import com.example.easygo.Models.DbModels.BookedTrips;
import com.example.easygo.Models.UserFlightChoiceSorting;
import com.example.easygo.PaymentActivity;
import com.example.easygo.R;
import com.example.easygo.Trip.User.UserTripDetailsActivity;
import com.example.easygo.Utils.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class FlightDetailsActivity extends BaseActivity {
    private ImageView uselessAirplaneImage, ivLogo;
    private TextView tvBackBtn, tvFrom, tvTo, tvCompanyName, tvDepartDate,
            tvReturnData, tvMeal, tvStops, tvClass, tvRefund, tvPrice, tvCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        init();
        deploy();

    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init() {
        uselessAirplaneImage = findViewById(R.id.uselessAirplane);
        ivLogo = findViewById(R.id.details_fly_companyLogo);
        tvBackBtn = findViewById(R.id.details_fly_backBtn);
        tvFrom = findViewById(R.id.details_fly_from);
        tvTo = findViewById(R.id.details_fly_to);
        tvCompanyName = findViewById(R.id.details_fly_companyName);
        tvDepartDate = findViewById(R.id.details_fly_departDate);
        tvReturnData = findViewById(R.id.details_fly_returnDate);
        tvMeal = findViewById(R.id.details_fly_meal);
        tvStops = findViewById(R.id.details_fly_stops);
        tvClass = findViewById(R.id.details_fly_class);
        tvRefund = findViewById(R.id.details_fly_refund);
        tvPrice = findViewById(R.id.details_fly_price);
        tvCheckOut = findViewById(R.id.details_fly_checkout);
    }

    private void deploy() {
        final UserFlightChoiceSorting model = this.getIntent().getParcelableExtra("mModel");



        tvFrom.setText(model.getModel().getSource().getName());
        tvTo.setText(model.getModel().getDestination().getName());
        tvCompanyName.setText("Company : "+model.getModel().getCompany().getTitle());
        tvDepartDate.setText(model.getModel().getDepartDate());
        tvReturnData.setText(model.getModel().getReturnDate());
        tvMeal.setText(model.getModel().getMeal());
        tvStops.setText(model.getModel().getStopsNo());
        tvClass.setText(model.getChoice().getClassType());
        tvRefund.setText(model.getModel().getRefund());
        tvPrice.setText(model.getChoice().getTotalPrice()+" L.E");
        Picasso.get().load(model.getModel().getCompany().getLogo()).fit().centerInside().into(ivLogo);
        Picasso.get().load(R.drawable.airplane).fit().into(uselessAirplaneImage);

        tvCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookedFlights bookedFlights = new BookedFlights(FirebaseAuth.getInstance().getUid(),model);
                Intent intent = new Intent(FlightDetailsActivity.this, PaymentActivity.class);
                intent.putExtra("flightModel", bookedFlights);
                startActivityForResult(intent,1);
            }
        });


        tvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
