package com.example.easygo.User.Flight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easygo.Models.DbModels.BookedFlights;
import com.example.easygo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OngoingFlightFragment extends Fragment {
    private static final String TAG = "OngoingFlightFragment";
    private RecyclerView mRecyclerView;
    private YourFlightAdapter mAdapter;
    private List<BookedFlights> mList;
    private final Date date = Calendar.getInstance().getTime();
    private FirebaseFirestore mDb;

    public OngoingFlightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_flight_result, container, false);
        init(view);

        return view;
    }

    private void init(View mView) {
        mDb = FirebaseFirestore.getInstance();

        // initialize the List
        mList= new ArrayList<>();
        //initialize recycler view
        mRecyclerView = mView.findViewById(R.id.recycler_FlightResult);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(LayoutManager);

        // initialize Adapter
        mAdapter = new YourFlightAdapter(getContext(), mList,new YourFlightAdapter.AdapterListener() {
            @Override
            public void detailsFlight(View v, BookedFlights model) {
                Intent intent = new Intent(getContext(), UserFlightDetailsActivity.class);
                intent.putExtra("mModel", model);
                intent.putExtra("pro","Cancel");
                startActivityForResult(intent,1);


            }
        });

        // link the adapter with the recycler
        mRecyclerView.setAdapter(mAdapter);

        getUserFlights();
    }

    private void getUserFlights() {
        CollectionReference categoryRef = mDb
                .collection("booked flight");

        Query query = categoryRef.whereEqualTo("id", FirebaseAuth.getInstance().getUid());


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }
                Log.e(TAG, "onEvent:2 ");

                if (queryDocumentSnapshots != null) {
                    Log.e(TAG, "onEvent: read ");
                    // Clear the list and add all the users again
                    mList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        BookedFlights model = doc.toObject(BookedFlights.class);


                        if (compareTime(model.getFlight().getModel().getDepartDate()) ==-1
                                ||compareTime(model.getFlight().getModel().getDepartDate()) ==0){
                            mList.add(model);
                        }
                        mAdapter.notifyDataSetChanged();
                    }


                }
            }
        });
    }

    private int compareTime(String inputDate){
        String myFormat = "dd/MM/yy"; //time Format
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        String currentDate = dateFormat.format(date);
        int x = 5;
        try {
            Date one = dateFormat.parse(inputDate);
            Date two = dateFormat.parse(currentDate);
            x = two.compareTo(one);
            return x;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return x;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(getContext(), ""+result, Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                getUserFlights();

            }
        }
    }
}