package com.example.easygo.Flight.Admin;

import android.os.Bundle;
import android.widget.TextView;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.R;

public class AdminFlightDetailsActivity extends BaseActivity {


    private static final String TAG = "AdminFlightDetailsActivity";//image
    private TextView tvDepart, tvReturn , tvVipPrice, tvVipCount,
            tvBusinessPrice, tvBusinessCount, tvEconomicPrice, tvEconomicCount, tvDuration, tvStopsNo
            , tvcCompany, tvSource, tvDestination , tvMeal, tvRefund ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flight_details);
        init();
        deploy();
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init(){

        showBackBtn("Details");

        tvVipPrice = findViewById(R.id.flightDetails_vipPrice);
        tvVipCount = findViewById(R.id.flightDetails_vipCount);
        tvBusinessPrice = findViewById(R.id.flightDetails_BusinessPrice);
        tvBusinessCount = findViewById(R.id.flightDetails_BusinessCount);
        tvEconomicPrice = findViewById(R.id.flightDetails_economicPrice);
        tvEconomicCount = findViewById(R.id.flightDetails_economicCount);
        tvDuration = findViewById(R.id.flightDetails_duration);
        tvStopsNo = findViewById(R.id.flightDetails_noofstops);
        tvDepart = findViewById(R.id.flightDetails_departure);
        tvReturn = findViewById(R.id.flightDetails_return);
        tvcCompany = findViewById(R.id.flightDetails_company);
        tvSource = findViewById(R.id.flightDetails_from);
        tvDestination = findViewById(R.id.flightDetails_to);
        tvMeal = findViewById(R.id.flightDetails_meal);
        tvRefund = findViewById(R.id.flightDetails_refund);

    }

    private void deploy(){
        FlightModel model = this.getIntent().getParcelableExtra("mModel");

        tvVipPrice.append(String.valueOf(model.getVipPrice()));
        tvVipCount.append(String.valueOf(model.getVipCount()));
        tvBusinessPrice.append(String.valueOf(model.getBusinessPrice()));
        tvBusinessCount.append(String.valueOf(model.getBusinessCount()));
        tvEconomicPrice.append(String.valueOf(model.getEconomicPrice()));
        tvEconomicCount.append(String.valueOf(model.getEconomicCount()));
        tvDuration.append(model.getDuration());
        tvStopsNo.append(model.getStopsNo());
        tvDepart.append(model.getDepartDate());
        tvReturn.append(model.getReturnDate());
        tvcCompany.append(model.getCompany().getTitle());
        tvSource.append(model.getSource().getName());
        tvDestination.append(model.getDestination().getName());
        tvMeal.setText(model.getMeal());
        tvRefund.setText(model.getRefund());
    }
}
