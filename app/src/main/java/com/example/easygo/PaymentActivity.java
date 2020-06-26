package com.example.easygo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import com.braintreepayments.cardform.view.CardForm;
import com.example.easygo.Models.DbModels.BookedFlights;
import com.example.easygo.Models.DbModels.BookedTrips;
import com.example.easygo.Utils.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentActivity extends BaseActivity {

    private Button buy;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
        deploy();
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init(){
        showBackBtn("Payments");
        CardForm cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);


        mDb = FirebaseFirestore.getInstance();

    }
    private void deploy(){
        final BookedTrips tripModel = this.getIntent().getParcelableExtra("tripModel");
        final BookedFlights flightModel = this.getIntent().getParcelableExtra("flightModel");




        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tripModel!=null){
                    checkOutTrip(tripModel);
                }else if (flightModel!=null){
                    checkOutFlight(flightModel);
                }

            }
        });
    }


    private void checkOutTrip(final BookedTrips bookedTrips) {
        final Loading loading = new Loading(PaymentActivity.this);
        loading.showBooking();
        if (FirebaseAuth.getInstance().getUid() != null) {
            DocumentReference ref = FirebaseFirestore.getInstance()
                    .collection("booked trip")
                    .document();

            bookedTrips.setTripId(ref.getId());

            ref.set(bookedTrips).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        int count;


                        count = Integer.valueOf(bookedTrips.getFlight().getNumberOfTravelers()) - Integer.valueOf(bookedTrips.getCount());


                        updateTrip(bookedTrips.getFlight().getId(), String.valueOf(count) ,bookedTrips );


                    } else {
                        loading.dismissLoading();
                    }
                }
            });


        }
    }
    private void updateTrip(String tripId, String newCount , final BookedTrips bookedTrips) {
        final DocumentReference flightRef = mDb
                .collection("trips")
                .document(tripId);


        flightRef.update("numberOfTravelers", newCount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Intent intent = new Intent(PaymentActivity.this, PrintTripActivity.class);
                    intent.putExtra("tripModel", bookedTrips);
                    startActivity(intent);

                }
            }
        });
    }


    private void checkOutFlight(final BookedFlights bookedFlights){
        final Loading loading = new Loading(PaymentActivity.this);
        loading.showBooking();
        if (FirebaseAuth.getInstance().getUid() != null) {
            DocumentReference ref = FirebaseFirestore.getInstance()
                    .collection("booked flight")
                    .document();

            bookedFlights.setFlightId(ref.getId());

            ref.set(bookedFlights).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        String classType ;
                        int count;
                        if (bookedFlights.getFlight().getChoice().getClassType().equals("economic")){
                            classType = "economicCount";
                            count = bookedFlights.getFlight().getModel().getEconomicCount() - bookedFlights.getFlight().getChoice().getTotalCount() ;
                        }else if (bookedFlights.getFlight().getChoice().getClassType().equals("business")){
                            classType = "businessCount";
                            count = bookedFlights.getFlight().getModel().getBusinessCount() - bookedFlights.getFlight().getChoice().getTotalCount() ;
                        }else {
                            classType = "vipCount";
                            count = bookedFlights.getFlight().getModel().getVipCount() - bookedFlights.getFlight().getChoice().getTotalCount() ;
                        }

                        updateFlight(bookedFlights.getFlight().getModel().getId(),count,classType,bookedFlights);


                    } else {
                        loading.dismissLoading();
                    }
                }
            });


        }
    }
    private void updateFlight(String flightId, int newCount , String classType , final BookedFlights bookedFlights ){
        final DocumentReference flightRef = mDb
                .collection("flights")
                .document(flightId);


        flightRef.update(classType,newCount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Intent intent = new Intent(PaymentActivity.this, PrintFlightActivity.class);
                    intent.putExtra("flightModel", bookedFlights);
                    startActivity(intent);

                }
            }
        });
    }


}