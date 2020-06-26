package com.example.easygo.User.Flight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.BookedFlights;
import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.R;
import com.example.easygo.Utils.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class UserFlightDetailsActivity extends BaseActivity {
    private ImageView uselessAirplaneImage, ivLogo;
    private TextView tvBackBtn, tvFrom, tvTo, tvCompanyName, tvDepartDate,
            tvReturnData, tvMeal, tvStops, tvClass, tvRefund, tvPrice, tvCheckOut;
    private FirebaseFirestore mDb;

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
        mDb = FirebaseFirestore.getInstance();

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
        final BookedFlights model = this.getIntent().getParcelableExtra("mModel");

        final String alternativeStart = this.getIntent().getStringExtra("pro");



        tvFrom.setText(model.getFlight().getModel().getSource().getName());
        tvTo.setText(model.getFlight().getModel().getDestination().getName());
        tvCompanyName.setText("Company : "+model.getFlight().getModel().getCompany().getTitle());
        tvDepartDate.setText(model.getFlight().getModel().getDepartDate());
        tvReturnData.setText(model.getFlight().getModel().getReturnDate());
        tvMeal.setText(model.getFlight().getModel().getMeal());
        tvStops.setText(model.getFlight().getModel().getStopsNo());
        tvClass.setText(model.getFlight().getChoice().getClassType());
        tvRefund.setText(model.getFlight().getModel().getRefund());
        tvPrice.setText(model.getFlight().getChoice().getTotalPrice()+" L.E");
        Picasso.get().load(model.getFlight().getModel().getCompany().getLogo()).fit().centerInside().into(ivLogo);
        Picasso.get().load(R.drawable.airplane).fit().into(uselessAirplaneImage);

        if (alternativeStart==null || alternativeStart.isEmpty()){

        }else if (alternativeStart.equals("Cancel")){
            tvCheckOut.setText(alternativeStart);
            tvCheckOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelBooking(model);
                    tvCheckOut.setClickable(false);
                }
            });
        }else {
            tvCheckOut.setText(alternativeStart);
            tvCheckOut.setClickable(false);
        }


        tvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cancelBooking(final BookedFlights bookedFlights){
        final Loading loading = new Loading(UserFlightDetailsActivity.this);
        loading.showCancelBooking();

        mDb.collection("booked flight").document(bookedFlights.getFlightId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String classType ;
                        int count;
                        if (bookedFlights.getFlight().getChoice().getClassType().equals("economic")){
                            classType = "economicCount";
                            count = bookedFlights.getFlight().getModel().getEconomicCount() + bookedFlights.getFlight().getChoice().getTotalCount() ;
                        }else if (bookedFlights.getFlight().getChoice().getClassType().equals("business")){
                            classType = "businessCount";
                            count = bookedFlights.getFlight().getModel().getBusinessCount() + bookedFlights.getFlight().getChoice().getTotalCount() ;
                        }else {
                            classType = "vipCount";
                            count = bookedFlights.getFlight().getModel().getVipCount() + bookedFlights.getFlight().getChoice().getTotalCount() ;
                        }

                        updateFlight(bookedFlights.getFlight().getModel().getId(),count,classType);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserFlightDetailsActivity.this, "Delete Fail", Toast.LENGTH_SHORT).show();
                        loading.dismissLoading();
                    }
                });

    }

    private void updateFlight(String flightId,int newCount , String classType ){
        final DocumentReference flightRef = mDb
                .collection("flights")
                .document(flightId);


        flightRef.update(classType,newCount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserFlightDetailsActivity.this, "Flight Canceled", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    tvCheckOut.setClickable(true);
                }
            }
        });
    }

}
