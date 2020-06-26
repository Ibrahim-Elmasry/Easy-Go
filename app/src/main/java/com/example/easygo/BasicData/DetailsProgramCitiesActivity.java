package com.example.easygo.BasicData;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.R;

import java.util.Objects;

public class DetailsProgramCitiesActivity extends AppCompatActivity {
        private TextView tvTitle,tvDetails;
        private String stTitle = "",stDetails = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_program_cities);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CitiesModel model = this.getIntent().getParcelableExtra("mModel");
        setTitle(model.getName());

        if (this.getIntent().getStringExtra("type").equals("Program")){
            stTitle = "Program Name : " + model.getName();
            stDetails = "City : " + "\n\n";
        }else {
            stTitle = "City Name : " + model.getName();
            stDetails = "Landmarks : " + "\n\n";
        }


        tvTitle = findViewById(R.id.details_proCit_Title);
        tvDetails = findViewById(R.id.details_proCit_details);

        tvTitle.setText(stTitle);
        for (int i = 0 ; i<model.getLandMarks().size(); i++){
            stDetails =  stDetails + " - "+ model.getLandMarks().get(i) + "\n";
        }
        tvDetails.setText(stDetails);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
