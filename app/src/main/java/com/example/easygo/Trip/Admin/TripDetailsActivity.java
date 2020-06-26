package com.example.easygo.Trip.Admin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.R;
import com.squareup.picasso.Picasso;

public class TripDetailsActivity extends BaseActivity {
    private TextView tvTitle, tvNum, tvPrice, tvDest, tvDepart, tvReturn, tvDescription, tvCompany ,tvProgram;
    private ImageView ivLogo;
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
        ivLogo = findViewById(R.id.details_trip_logo);
        tvProgram = findViewById(R.id.details_trip_program);

        TripModel model = this.getIntent().getParcelableExtra("mModel");
        tripId = this.getIntent().getStringExtra("idz");

        showBackBtn(model.getTitle());
        tvTitle.setText("Title : "+model.getTitle());
        tvNum.setText("Duration : "+model.getDuration());
        tvPrice.setText("Price :  "+model.getPrice());
        tvDest.setText("City : "+model.getCmCity().getName());
        tvDepart.setText("Start At : "+model.getStartAt());
        tvReturn.setText("End At : "+model.getEndAt());
        tvDescription.setText("Description : "+model.getDescription());
        tvCompany.setText("Company : "+model.getFcmCompany().getTitle());
        for (int i = 0 ; i<model.getPrograms().size();i++){
            tvProgram.append(model.getPrograms().get(i) + " , ");
        }

        Picasso.get().load(model.getImageUrl()).fit().into(ivLogo);
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }
}
