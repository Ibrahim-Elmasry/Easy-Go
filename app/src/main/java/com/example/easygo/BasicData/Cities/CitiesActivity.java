package com.example.easygo.BasicData.Cities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.BasicData.DetailsProgramCitiesActivity;
import com.example.easygo.BasicData.Companies.EditCompanyActivity;
import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.R;
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

public class CitiesActivity extends BaseActivity {
    private static final String TAG = "CitiesActivity";
    private RecyclerView mRecyclerView;
    private CitiesAdapter mAdapter;
    private List<CitiesModel> mList;
    private FirebaseFirestore mDb;
    private MaterialSearchView searchView;
    private String search;

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

    private void init(){
        //get data from previous activity
        String dataType = this.getIntent().getStringExtra("dataType");
        //tool bar setup
        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Cities");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //search view
        searchView = findViewById(R.id.search_view);

        // data base
        mDb = FirebaseFirestore.getInstance();

        //
        mList = new ArrayList<>();

        //method used to fetch data from firebase server
        getData();

        //recycler view
        mRecyclerView = findViewById(R.id.recycler_Trip);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(CitiesActivity.this);
        mRecyclerView.setLayoutManager(LayoutManager);

        // adapter
        mAdapter = new CitiesAdapter(CitiesActivity.this, mList, new CitiesAdapter.AdapterListener() {
            @Override
            public void detailsTrip(View v, CitiesModel model, String id) {
                Intent intent = new Intent(CitiesActivity.this, DetailsProgramCitiesActivity.class);
                intent.putExtra("mModel",model);
                intent.putExtra("type","City");

                startActivity(intent);
            }

            @Override
            public void editTrip(View v, CitiesModel model, String id) {
                Intent intent = new Intent(CitiesActivity.this, EditCompanyActivity.class);
                intent.putExtra("mModel",model);
                startActivity(intent);
            }

            @Override
            public void deleteTrip(View v, String id) {
                mDb.collection("city").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CitiesActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                                getData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CitiesActivity.this, "Delete Fail", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(CitiesActivity.this, AddCityActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        MenuItem action_filter = menu.findItem(R.id.action_filter);
        action_filter.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh :
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
                .collection("city");

        allTripsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("CitiesActivity", "onEvent: Listen failed.", e);
                    return;
                }
                Log.e("CitiesActivity", "onEvent:2 ");

                if (queryDocumentSnapshots != null) {
                    Log.e("CitiesActivity", "onEvent: ");
                    // Clear the list and add all the users again
                    mList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        CitiesModel foodDisplay = doc.toObject(CitiesModel.class);

                        mList.add(foodDisplay);
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
                .collection("city");

        Query query = allTripsRef
                .whereEqualTo("name",searchInput.toLowerCase());


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

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        CitiesModel model = doc.toObject(CitiesModel.class);

                        mList.add(model);
                        Log.e("searchz", "onEvent: " + doc.getId());
                        mAdapter.notifyDataSetChanged();
                    }


                }
            }
        });
    }


}
