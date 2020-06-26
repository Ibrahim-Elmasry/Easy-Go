package com.example.easygo.User.Trip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.BookedFlights;
import com.example.easygo.Models.DbModels.BookedTrips;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.PaymentActivity;
import com.example.easygo.R;
import com.example.easygo.User.Flight.UserFlightDetailsActivity;
import com.example.easygo.Utils.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class YourTripDetailsActivity extends BaseActivity {
    private ImageView ivLogo;
    private TextView tvTitle,tvDuration, tvStartAt, tvEndAt, tvPrograms, tvLandmarks,
            tvDescription, tvCount, tvPrice, tvCheckOut , tvCompany;
    private String stPrice, stTripCount , stCurrentTripCount;
    private FirebaseFirestore mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourtrip_details);

        init();
        deploy();

    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init() {
        mDb = FirebaseFirestore.getInstance();
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

    }

    private void deploy() {
        final BookedTrips model = this.getIntent().getParcelableExtra("mModel");
        final String alternativeStart = this.getIntent().getStringExtra("pro");

        showBackBtn(model.getFlight().getTitle());
        getTrip(model.getFlight().getId());
        stTripCount = model.getFlight().getNumberOfTravelers();
        tvTitle.setText(model.getFlight().getTitle().toUpperCase());
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
        stPrice = model.getFlight().getPrice();
        tvPrice.setText(stPrice);
        tvCompany.setText(model.getFlight().getFcmCompany().getTitle());
        Picasso.get().load(model.getFlight().getImageUrl()).fit().into(ivLogo);





        if (alternativeStart==null || alternativeStart.isEmpty()){

        }else if (alternativeStart.equals("Cancel")){
            tvCheckOut.setText(alternativeStart);
            tvCheckOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stCurrentTripCount != null && !stCurrentTripCount.isEmpty()){
                        cancelBooking(model);
                        tvCheckOut.setClickable(false);
                    }
                }
            });
        }else {
            tvCheckOut.setText(alternativeStart);
            tvCheckOut.setClickable(false);
        }
    }



    private void cancelBooking(final BookedTrips bookedTrips){
        final Loading loading = new Loading(YourTripDetailsActivity.this);
        loading.showCancelBooking();

        mDb.collection("booked trip").document(bookedTrips.getTripId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        String count = String.valueOf(Integer.valueOf(stCurrentTripCount)+Integer.valueOf(bookedTrips.getCount()));
                        updateTrip(bookedTrips.getFlight().getId(),count);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(YourTripDetailsActivity.this, "Delete Fail", Toast.LENGTH_SHORT).show();
                        loading.dismissLoading();
                    }
                });

    }


    private void updateTrip(String tripId, String newCount) {
        final DocumentReference flightRef = mDb
                .collection("trips")
                .document(tripId);


        flightRef.update("numberOfTravelers", newCount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    finish();

                } else {
                    tvCheckOut.setClickable(true);
                }
            }
        });
    }




    //get trip current count
    private void getTrip(String id) {
        final DocumentReference reference = mDb
                .collection("trips").document(id);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot != null) {
                    TripModel model = documentSnapshot.toObject(TripModel.class);
                    if (model != null) {
                        stCurrentTripCount = model.getNumberOfTravelers();
                    }
                }

            }
        });
    }

}
