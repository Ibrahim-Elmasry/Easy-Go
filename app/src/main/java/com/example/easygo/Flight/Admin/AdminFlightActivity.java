package com.example.easygo.Flight.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.R;
import com.example.easygo.Utils.SortFilterBottomSheet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminFlightActivity extends BaseActivity implements SortFilterBottomSheet.BottomSheetListener{
    private static final String TAG = "AdminFlightActivity";
    private RecyclerView mRecyclerView;
    private AdminFlightAdapter mAdapter;
    private List<FlightModel> mList;
    private MaterialSearchView searchView;
    private String search;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_trip);
        init();


        // search related
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                search = query;
                dbSearch(search);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic

                return false;
            }
        });
        // search related
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                //getData();
            }
        });

    }

    private void init() {

        //tool bar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        showBackBtnForCustomToolBar("Flights",toolbar);
        //search view
        searchView = findViewById(R.id.search_view);
        mDb = FirebaseFirestore.getInstance();


        //
        mList = new ArrayList<>();


        //recycler view
        mRecyclerView = findViewById(R.id.recycler_Trip);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(AdminFlightActivity.this);
        mRecyclerView.setLayoutManager(LayoutManager);

        // adapter
        mAdapter = new AdminFlightAdapter(AdminFlightActivity.this, mList, new AdminFlightAdapter.AdapterListener() {
            @Override
            public void details(View v, FlightModel model) {
                Intent intent = new Intent(AdminFlightActivity.this, AdminFlightDetailsActivity.class);
                intent.putExtra("mModel", model);
                startActivity(intent);
            }

            @Override
            public void edit(View v, FlightModel model) {
                Intent intent = new Intent(AdminFlightActivity.this, EditFlightActivity.class);
                intent.putExtra("mModel", model);
                startActivity(intent);
            }

            @Override
            public void delete(View v, String id) {
                mDb.collection("flights").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminFlightActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                                getData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminFlightActivity.this, "Delete Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        // link the adapter with the recycler
        mRecyclerView.setAdapter(mAdapter);

        //fab
        FloatingActionButton fab = findViewById(R.id.addTrip);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customStartActivityForResult(AdminFlightActivity.this, AddFlightActivity.class);
            }
        });


        //method used to fetch data
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        //menu.findItem(R.id.action_filter).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter :
                onButtonSheetPressed();
                break;
            case R.id.action_refresh:
                getData();
                break;
        }
        return true;
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {
        getData();
    }

    private void getData() {
        CollectionReference allTripsRef = mDb
                .collection("flights");

        allTripsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
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
                        FlightModel model = doc.toObject(FlightModel.class);

                        mList.add(model);
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.notifyDataSetChanged();

                }
            }
        });


    }


    private void dbSearch(String searchInput) {
        CollectionReference allTripsRef = mDb
                .collection("flights");

        Query query = allTripsRef
                .whereEqualTo("company.title",searchInput.toLowerCase());


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
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
                        FlightModel model = doc.toObject(FlightModel.class);

                        mList.add(model);
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.notifyDataSetChanged();

                }
            }
        });
    }


    //ignore this part
    private void onButtonSheetPressed(){
        SortFilterBottomSheet bottomSheet = new SortFilterBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
    }
    //ignore this part
    @Override
    public void onSaveButtonClicked(String company, String price, String order) {

        Log.e("XOXOXO", "onSaveButtonClicked: "+company );
        CollectionReference allTripsRef = mDb
                .collection("flights");

        Query query = null;

        Query.Direction priceDirection =null;
        Query.Direction orderDirection = null;


        if (price.equals("lth")){
            priceDirection = Query.Direction.ASCENDING;
        }else if (price.equals("htl")){
            priceDirection = Query.Direction.DESCENDING;
        }

        if (order.equals("a2z")){

            orderDirection = Query.Direction.ASCENDING;
        }else if (order.equals("z2a")){
            orderDirection = Query.Direction.DESCENDING;

        }


        if (!price.equals("null")&&!order.equals("null")&&company.equals("null")){
            query = allTripsRef
                    .orderBy("company.title", Objects.requireNonNull(orderDirection))
                    .orderBy("vipPrice", Objects.requireNonNull(priceDirection));


        }else if (price.equals("null")&&!order.equals("null")&&company.equals("null")){
            query = allTripsRef
                    .orderBy("company.title", Objects.requireNonNull(orderDirection));
        }else if (!price.equals("null")&&order.equals("null")&&company.equals("null")){
            query = allTripsRef
                    .orderBy("vipPrice", Objects.requireNonNull(priceDirection));
        }else if (!price.equals("null")&&!company.equals("null")){
            query = allTripsRef
                    .whereEqualTo("company.id",company)
                    .orderBy("vipPrice", Objects.requireNonNull(priceDirection));
        }else if (price.equals("null")&&!company.equals("null")){
            query = allTripsRef
                    .whereEqualTo("company.id",company)

            ;
        }


        if (query != null) {
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
                            FlightModel model = doc.toObject(FlightModel.class);

                            mList.add(model);
                            Log.e(TAG, "onEvent: " + doc.getId());
                            mAdapter.notifyDataSetChanged();
                        }


                    }
                }
            });
        }

    }
}
