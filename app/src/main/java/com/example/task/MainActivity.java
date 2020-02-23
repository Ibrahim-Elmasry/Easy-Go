package com.example.task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TripAdapter mAdapter;
    private List<TripModel> mList;
    private List<String> mIdList;
    private FirebaseFirestore mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Task");

        mDb = FirebaseFirestore.getInstance();

        getData();

        mRecyclerView = findViewById(R.id.recycler_Trip);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(LayoutManager);

        mAdapter = new TripAdapter(MainActivity.this, mList, mIdList, new TripAdapter.AdapterListener() {
            @Override
            public void editTrip(View v, TripModel model, String id) {
                Intent intent = new Intent(MainActivity.this,EditTripActivity.class);

                intent.putExtra("mModel",model);
                intent.putExtra("idz",id);
                startActivity(intent);
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



    private void getData() {
        mList = new ArrayList<>();
        mIdList = new ArrayList<>();
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


                }
            }
        });


    }


}
