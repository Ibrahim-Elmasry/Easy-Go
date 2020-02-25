package com.example.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  implements SortFilterBottomSheet.BottomSheetListener{
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private TripAdapter mAdapter;
    private List<TripModel> mList;
    private List<String> mIdList;
    private FirebaseFirestore mDb;
    private MaterialSearchView searchView;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void init(){

        //tool bar setup
        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Trips");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        //search view
        searchView = findViewById(R.id.search_view);

        mDb = FirebaseFirestore.getInstance();

        mList = new ArrayList<>();
        mIdList = new ArrayList<>();
        getData();

        mRecyclerView = findViewById(R.id.recycler_Trip);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(LayoutManager);

        mAdapter = new TripAdapter(MainActivity.this, mList, mIdList, new TripAdapter.AdapterListener() {
            @Override
            public void detailsTrip(View v, TripModel model, String id) {
                Intent intent = new Intent(MainActivity.this,TripDetailsActivity.class);
                intent.putExtra("mModel",model);
                intent.putExtra("idz",id);
                startActivity(intent);
            }

            @Override
            public void editTrip(View v, TripModel model, String id) {
                Intent intent = new Intent(MainActivity.this,EditTripActivity.class);
                intent.putExtra("mModel",model);
                intent.putExtra("idz",id);
                startActivity(intent);
            }

            @Override
            public void deleteTrip(View v, String id) {
                mDb.collection("trips").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                                getData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Delete Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        //fab
        FloatingActionButton fab = findViewById(R.id.addTrip);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter :
                onButtonSheetPressed();
                break;
            case R.id.action_refresh :
                getData();
                break;
        }
        return true;
    }

    private void getData() {
        CollectionReference allTripsRef = mDb
                .collection("trips");

        allTripsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("MainActivity", "onEvent: Listen failed.", e);
                    return;
                }
                Log.e("MainActivity", "onEvent:2 ");

                if (queryDocumentSnapshots != null) {
                    Log.e("MainActivity", "onEvent: ");
                    // Clear the list and add all the users again
                    mList.clear();
                    mIdList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        TripModel foodDisplay = doc.toObject(TripModel.class);

                        mList.add(foodDisplay);
                        mIdList.add(doc.getId());
                        Log.e("LALALALA", "onEvent: " + doc.getId());
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.notifyDataSetChanged();

                }
            }
        });


    }
    private void dbSearch(String searchInput){
        CollectionReference allTripsRef = mDb
                .collection("trips");

        Query query = allTripsRef
                .whereEqualTo("destination",searchInput.toLowerCase());


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("searchz", "onEvent: Listen failed.", e);
                    return;
                }
                Log.e("searchz", "onEvent:2 ");

                if (queryDocumentSnapshots != null) {
                    Log.e("searchz", "onEvent: read ");
                    // Clear the list and add all the users again
                    mList.clear();
                    mIdList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        TripModel foodDisplay = doc.toObject(TripModel.class);

                        mList.add(foodDisplay);
                        mIdList.add(doc.getId());
                        Log.e("searchz", "onEvent: " + doc.getId());
                        mAdapter.notifyDataSetChanged();
                    }


                }
            }
        });
    }

    private void onButtonSheetPressed(){
        SortFilterBottomSheet bottomSheet = new SortFilterBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
    }

    @Override
    public void onSaveButtonClicked(String company, String price, String order) {
        CollectionReference allTripsRef = mDb
                .collection("trips");

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
                    .orderBy("trip_title", Objects.requireNonNull(orderDirection))
                    .orderBy("price", Objects.requireNonNull(priceDirection));


        }else if (price.equals("null")&&!order.equals("null")&&company.equals("null")){
            query = allTripsRef
                    .orderBy("trip_title", Objects.requireNonNull(orderDirection));
        }else if (!price.equals("null")&&order.equals("null")&&company.equals("null")){
            query = allTripsRef
                    .orderBy("price", Objects.requireNonNull(priceDirection));
        }else if (!price.equals("null")&&!company.equals("null")){
            query = allTripsRef
                    .whereEqualTo("company",company)
                    .orderBy("price", Objects.requireNonNull(priceDirection));
        }else if (price.equals("null")&&!company.equals("null")){
            query = allTripsRef
                    .whereEqualTo("company",company)

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
                        mIdList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            TripModel foodDisplay = doc.toObject(TripModel.class);

                            mList.add(foodDisplay);
                            mIdList.add(doc.getId());
                            Log.e(TAG, "onEvent: " + doc.getId());
                            mAdapter.notifyDataSetChanged();
                        }


                    }
                }
            });
        }

    }
}
