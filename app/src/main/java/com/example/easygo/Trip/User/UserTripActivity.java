package com.example.easygo.Trip.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.easygo.BaseActivity;
import com.example.easygo.Flight.User.FlightDetailsActivity;
import com.example.easygo.Flight.User.FlightFilterActivity;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.Models.UserFlightChoiceSorting;
import com.example.easygo.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserTripActivity extends BaseActivity {

    private static final String TAG = "FlightResultActivity";
    private RecyclerView mRecyclerView;
    private UserTripAdapter mAdapter;
    private List<TripModel> mList;
    private FirebaseFirestore mDb;

    private static int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trip);
        init();
    }

    private void init() {
        //get data from previous activity
      //  searchFlightModel= this.getIntent().getParcelableExtra("mModel");

        //show back btn and set activity title
        showBackBtn("Trips");
        mDb = FirebaseFirestore.getInstance();

        // initialize the List
        mList = new ArrayList<>();

        //initialize recycler view
        mRecyclerView = findViewById(R.id.recycler_User_Trip);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(UserTripActivity.this);
        mRecyclerView.setLayoutManager(LayoutManager);

        // initialize Adapter
        mAdapter = new UserTripAdapter(UserTripActivity.this, mList,new UserTripAdapter.AdapterListener() {
            @Override
            public void details(View v, TripModel model) {


                Intent intent = new Intent(UserTripActivity.this, UserTripDetailsActivity.class);
                intent.putExtra("mModel", model);
                startActivityForResult(intent,1);


            }
        });
        // link the adapter with the recycler
        mRecyclerView.setAdapter(mAdapter);

        getData();
    }

    //handle on back pressed ( higher btn )
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.fly_menu_filter:
                sendToFlightFilterActivity();
                break;
            case R.id.fly_menu_refresh:
                getData();
                break;
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flight_menu, menu);

        return true;
    }

    private void getData() {
        final CollectionReference reference = mDb
                .collection("trips");

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                    @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("AdminFlightActivity", "onEvent: Listen failed.", e);
                        return;
                    }
                    Log.e("AdminFlightActivity", "onEvent:2 ");

                    if (queryDocumentSnapshots != null) {
                        Log.e("AdminFlightActivity", "onEvent: ");
                        // Clear the list and add all the users again
                        mList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            TripModel model = doc.toObject(TripModel.class);
                            //  use this for returnDate sorting
                            if (Integer.valueOf(model.getNumberOfTravelers())>0)
                            mList.add(model);

                            mAdapter.notifyDataSetChanged();
                        }


                        mAdapter.notifyDataSetChanged();

                    }
                }
            });
        }

    private int compareTime(String inputDate ,String returnDate){
        String myFormat = "dd/MM/yy"; //time Format
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        int x = 5;
        try {
            Date one = dateFormat.parse(inputDate);
            Date two = dateFormat.parse(returnDate);
            x = two.compareTo(one);
            return x;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return x;
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void sendToFlightFilterActivity() {

        Intent i = new Intent(this, TripFilterActivity.class);

        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("mod", (ArrayList<? extends Parcelable>) mList);


        i.putExtras(bundle);

        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                mList.clear();
                if (data.getExtras()!=null){
                    List<TripModel> list = data.getExtras().getParcelableArrayList("res");
                    if (list != null) {
                        mList.addAll(list);
                    }
                    mAdapter.notifyDataSetChanged();

                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                mList.clear();
                getData();

                Log.e(TAG, "onCancel: " );
            }
        }
    }

}
