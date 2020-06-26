package com.example.easygo.Trip.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.BookedTrips;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.PaymentActivity;
import com.example.easygo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class UserTripDetailsActivity extends BaseActivity {
    private ImageView ivLogo;
    private TextView tvTitle,tvDuration, tvStartAt, tvEndAt, tvPrograms, tvLandmarks,
            tvDescription, tvCount, tvPrice, tvCheckOut , tvCompany;
    private String stCountAdult,stPrice,stTotalPrice , stTripCount;
    private Button addCount,removeCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertrip_details);

        init();
        deploy();

    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init() {
        ivLogo = findViewById(R.id.tripDetails_logo);
        tvTitle = findViewById(R.id.tripDetails_title);
        tvDuration = findViewById(R.id.tripDetails_duration);
        tvStartAt = findViewById(R.id.tripDetails_startAt);
        tvEndAt = findViewById(R.id.tripDetails_endAt);
        tvPrograms = findViewById(R.id.tripDetails_program);
        tvLandmarks = findViewById(R.id.tripDetails_landMark);
        tvCompany = findViewById(R.id.tripDetails_company);
        tvDescription = findViewById(R.id.tripDetails_desc);
        tvCount = findViewById(R.id.tripDetails_adultCount);
        tvPrice = findViewById(R.id.tripDetails_price);
        tvCheckOut = findViewById(R.id.tripDetails_checkOut);
        addCount = findViewById(R.id.tripDetails_addAdult);
        removeCount = findViewById(R.id.tripDetails_removeAdult);

    }

    private void deploy() {
        final TripModel model = this.getIntent().getParcelableExtra("mModel");
        showBackBtn(model.getTitle());

        stTripCount = model.getNumberOfTravelers();
        tvTitle.setText(model.getTitle().toUpperCase());
        tvDuration.setText(model.getDuration());
        tvStartAt.setText(model.getStartAt());
        tvEndAt.setText(model.getEndAt());

        for (int i = 0 ; i<model.getPrograms().size();i++){
            tvPrograms.append(model.getPrograms().get(i) + " , ");
        }

        for (int i = 0 ; i<model.getCmCity().getLandMarks().size();i++){
            tvLandmarks.append(model.getCmCity().getLandMarks().get(i) + " , ");
        }
        tvDescription.setText(model.getDescription());
        tvCount.setText("1");
        stPrice = model.getPrice();
        tvPrice.setText(stPrice);
        tvCompany.setText(model.getFcmCompany().getTitle());
        Picasso.get().load(model.getImageUrl()).fit().into(ivLogo);
        setAdultCount();
        tvCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookedTrips bookedTrips = new BookedTrips(FirebaseAuth.getInstance().getUid(), stCountAdult,stTotalPrice,model);

                Intent intent = new Intent(UserTripDetailsActivity.this, PaymentActivity.class);
                intent.putExtra("tripModel", bookedTrips);
                startActivityForResult(intent,1);

            }
        });

    }






    private void setAdultCount() {
        final int[] count = {1};
        final int totalTripSeats = Integer.valueOf(stTripCount);
        stCountAdult = "1";
        tvCount.setText(stCountAdult);
        stTotalPrice = stPrice;
        addCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalTripSeats>count[0]){
                    count[0] = count[0] + 1;
                    stCountAdult = String.valueOf(count[0]);
                    tvCount.setText(stCountAdult);
                    // change the price
                    stTotalPrice = String.valueOf(Integer.valueOf(stPrice)*count[0]);
                    tvPrice.setText(stTotalPrice);
                }
            }
        });

        removeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] > 1) {
                    count[0] = count[0] - 1;
                    stCountAdult = String.valueOf(count[0]);
                    tvCount.setText(stCountAdult);
                    stTotalPrice = String.valueOf(Integer.valueOf(stPrice)*count[0]);
                    tvPrice.setText(stTotalPrice);
                }

            }
        });
    }
}
