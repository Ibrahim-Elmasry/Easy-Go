package com.example.easygo.Flight.User;

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
import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.Models.SearchFlightModel;
import com.example.easygo.Models.UserFlightChoice;
import com.example.easygo.Models.UserFlightChoiceSorting;
import com.example.easygo.R;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FlightResultActivity extends BaseActivity {

    private static final String TAG = "FlightResultActivity";
    private RecyclerView mRecyclerView;
    private FlightResultAdapter mAdapter;
    private List<FlightModel> mList;
    private List<UserFlightChoice> mList2;
    private List<UserFlightChoiceSorting> mSortingList;
    private SearchFlightModel searchFlightModel;
    private FirebaseFirestore mDb;

    private static int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_result);
        init();
    }

    private void init() {
        //get data from previous activity
        searchFlightModel= this.getIntent().getParcelableExtra("mModel");

        //show back btn and set activity title
        showBackBtn(searchFlightModel.getCityFrom().getName() + " to " + searchFlightModel.getCityTo().getName());
        mDb = FirebaseFirestore.getInstance();

        // initialize the List
        mList = new ArrayList<>();
        mList2 = new ArrayList<>();
        mSortingList= new ArrayList<>();
        //initialize recycler view
        mRecyclerView = findViewById(R.id.recycler_FlightResult);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(FlightResultActivity.this);
        mRecyclerView.setLayoutManager(LayoutManager);

        // initialize Adapter
        mAdapter = new FlightResultAdapter(FlightResultActivity.this, mSortingList,new FlightResultAdapter.AdapterListener() {
            @Override
            public void detailsFlight(View v, UserFlightChoiceSorting model) {
                Intent intent = new Intent(FlightResultActivity.this, FlightDetailsActivity.class);
                intent.putExtra("mModel", model);
                startActivityForResult(intent,1);


            }
        });

        // link the adapter with the recycler
        mRecyclerView.setAdapter(mAdapter);

        getData(searchFlightModel);
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
                getData(searchFlightModel);
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


    private void getData(final SearchFlightModel searchFlightModel) {
        final int totalCount = Integer.valueOf(searchFlightModel.getCountAdult()) + Integer.valueOf(searchFlightModel.getCountChild());
        final CollectionReference reference = mDb
                .collection("flights");
        Query query = null;

        if (searchFlightModel.getFlightClass().equals("economicCount")) {
            query = reference
                    .whereEqualTo("departDate", searchFlightModel.getTimeDepart())
                    .whereGreaterThan("economicCount", totalCount - 1)
                    .whereEqualTo("source.id", searchFlightModel.getCityFrom().getId())
                    .whereEqualTo("destination.id", searchFlightModel.getCityTo().getId());

        } else if (searchFlightModel.getFlightClass().equals("businessCount")) {
            query = reference
                    .whereEqualTo("departDate", searchFlightModel.getTimeDepart())
                    .whereGreaterThan("businessCount", totalCount - 1)
                    .whereEqualTo("source.id", searchFlightModel.getCityFrom().getId())
                    .whereEqualTo("destination.id", searchFlightModel.getCityTo().getId());

        } else if (searchFlightModel.getFlightClass().equals("vipCount")) {
            query = reference
                    .whereEqualTo("departDate", searchFlightModel.getTimeDepart())
                    .whereGreaterThan("vipCount", totalCount - 1)
                    .whereEqualTo("source.id", searchFlightModel.getCityFrom().getId())
                    .whereEqualTo("destination.id", searchFlightModel.getCityTo().getId());
        }

        if (query != null) {
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        mList2.clear();
                        mSortingList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            FlightModel model = doc.toObject(FlightModel.class);
                            UserFlightChoice flightChoice ;
                            //  use this for returnDate sorting
                            if (compareTime(searchFlightModel.getTimeReturn(),model.getReturnDate()) ==-1 ||compareTime(searchFlightModel.getTimeReturn(),model.getReturnDate()) ==0){
                                mList.add(model);
                            }



                            if (searchFlightModel.getFlightClass().equals("economicCount")) {
                                flightChoice = new UserFlightChoice("economic",String.valueOf(model.getEconomicPrice()),searchFlightModel.getCountChild(),searchFlightModel.getCountAdult());
                                mList2.add(flightChoice);
                                mSortingList.add(new UserFlightChoiceSorting(model,flightChoice));
                            } else if (searchFlightModel.getFlightClass().equals("businessCount")) {
                                flightChoice=new UserFlightChoice("business",String.valueOf(model.getBusinessPrice()),searchFlightModel.getCountChild(),searchFlightModel.getCountAdult());
                                mList2.add(flightChoice);
                                mSortingList.add(new UserFlightChoiceSorting(model,flightChoice));
                            } else if (searchFlightModel.getFlightClass().equals("vipCount")) {
                                flightChoice=new UserFlightChoice("vip",String.valueOf(model.getVipPrice()),searchFlightModel.getCountChild(),searchFlightModel.getCountAdult());
                                mList2.add(flightChoice);
                                mSortingList.add(new UserFlightChoiceSorting(model,flightChoice));
                            }

                            mAdapter.notifyDataSetChanged();
                        }


                        mAdapter.notifyDataSetChanged();

                    }
                }
            });
        }

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

    private void sendToFlightFilterActivity() {

        Intent i = new Intent(this, FlightFilterActivity.class);

        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("mod", (ArrayList<? extends Parcelable>) mSortingList);


        i.putExtras(bundle);

        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                mSortingList.clear();
                if (data.getExtras()!=null){
                    List<UserFlightChoiceSorting> list = data.getExtras().getParcelableArrayList("res");
                    if (list != null) {
                        mSortingList.addAll(list);
                    }
                    mAdapter.notifyDataSetChanged();

                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                mSortingList.clear();
                getData(searchFlightModel);

                Log.e(TAG, "onCancel: " );
            }
        }
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }


}
