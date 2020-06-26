package com.example.easygo.BasicData;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.easygo.BasicData.Cities.CitiesActivity;
import com.example.easygo.BasicData.Companies.CompaniesActivity;
import com.example.easygo.BasicData.Programs.ProgramActivity;
import com.example.easygo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicDataActivity extends AppCompatActivity {

    private static final String TAG = "BasicDataActivity";
    private RecyclerView mRecyclerView;
    private BasicDataAdapter mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_data);

        init();

        getData();


    }

    private void init(){
        //setTitle
        setTitle("Basic Data");
        //back Btn
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // initialize the List
        mList = new ArrayList<>();

        //initialize recycler view
        mRecyclerView = findViewById(R.id.recycler_BasicData);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(BasicDataActivity.this);
        mRecyclerView.setLayoutManager(LayoutManager);

        // initialize Adapter
        mAdapter = new BasicDataAdapter(BasicDataActivity.this, mList, new BasicDataAdapter.AdapterListener() {
            @Override
            public void basicData(View v, String  data) {
                if (data.equals("Companies")){
                    Intent intent = new Intent(BasicDataActivity.this, CompaniesActivity.class);
                    intent.putExtra("dataType","Companies");
                    startActivity(intent);
                }else if (data.equals("Cities")){
                    Intent intent = new Intent(BasicDataActivity.this, CitiesActivity.class);
                    intent.putExtra("dataType","Cities");
                    startActivity(intent);
                }else if (data.equals("Programs")){
                    Intent intent = new Intent(BasicDataActivity.this, ProgramActivity.class);
                    intent.putExtra("dataType","Cities");
                    startActivity(intent);
                }

            }});

        // link the adapter with the recycler
        mRecyclerView.setAdapter(mAdapter);
    }


    //handle on back pressed ( higher btn )
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        mList.clear();

        mList.add("Companies");
        mList.add("Cities");
        mList.add("Programs");

        mAdapter.notifyDataSetChanged();

    }

}
