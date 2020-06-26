package com.example.easygo.BasicData.Programs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddProgramActivity extends BaseActivity {
    private EditText edTitle,edLandMark;
    private TextView tvListOfLandMark;
    private String stTitle,stTotalLandMarks;
    private List<String> mList;
    private Button btnAdd ,btnAddLandMarkBtn;
    private FirebaseFirestore mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        init();
        deploy();
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init(){
        showBackBtn("Add Program");
        mList = new ArrayList<>();
        mDb = FirebaseFirestore.getInstance();

        edTitle = findViewById(R.id.add_city_title);
        edTitle.setHint("Program Title");
        edLandMark = findViewById(R.id.add_city_landMark);
        edLandMark.setHint("City name");
        tvListOfLandMark = findViewById(R.id.add_city_ListOFLandMark);

        btnAdd = findViewById(R.id.Add_city_addBtn);
        btnAddLandMarkBtn = findViewById(R.id.add_city_addLandMark);
        btnAddLandMarkBtn.setText("Add City");

    }

    private void deploy(){

        btnAddLandMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String landMark = edLandMark.getText().toString();
                if (landMark== null || landMark.isEmpty()){


                }else {
                    mList.add(landMark);
                    if(stTotalLandMarks== null||stTotalLandMarks.isEmpty()){
                        stTotalLandMarks = landMark ;
                        tvListOfLandMark.setText(stTotalLandMarks);
                        edLandMark.setText("");

                    }else {
                        stTotalLandMarks = stTotalLandMarks +" , " +landMark ;
                        tvListOfLandMark.setText(stTotalLandMarks);
                        edLandMark.setText("");
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }

    private void Validation() {
        stTitle = edTitle.getText().toString();



        if (stTitle== null || stTitle.isEmpty()
                ){
            Toast.makeText(this, "Some values are missing", Toast.LENGTH_SHORT).show();
        }else {
            postData(stTitle.toLowerCase(),mList);
        }


    }

    private void postData(String title, List<String> landmark){
        final DocumentReference companiesRef = mDb
                .collection("program")
                .document();

        CitiesModel newCompany = new CitiesModel(companiesRef.getId(),title,landmark);
        companiesRef.set(newCompany).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(AddProgramActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();

                } else {


                }
            }
        });


    }






}
