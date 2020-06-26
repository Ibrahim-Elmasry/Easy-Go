package com.example.easygo.BasicData.Companies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.Models.DbModels.FlightCompaniesModel;
import com.example.easygo.R;
import com.example.easygo.Utils.BaseUploadImageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class EditCompanyActivity extends BaseUploadImageActivity implements BaseUploadImageActivity.ImageInterface {

    private EditText edTitle, edEmail, edFax, edPhone, edAddress;
    private TextView tvAddImg;
    private ImageView ivAddImg;
    private String stId,stTitle, stEmail, stFax, stPhone, stAddress;
    private Button editBtn;
    private FirebaseFirestore mDb;

    //image
    public String getEncodedImage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        init();
        deploy();
    }


    private void init(){

        showBackBtn("Edit Company");

        //firebase
        mDb = FirebaseFirestore.getInstance();
        //init
        edTitle = findViewById(R.id.add_company_title);
        edEmail = findViewById(R.id.add_company_email);
        edFax = findViewById(R.id.add_company_fax);
        edPhone = findViewById(R.id.add_company_phone);
        edAddress = findViewById(R.id.add_company_address);

        tvAddImg = findViewById(R.id.add_company_imageName);
        ivAddImg = findViewById(R.id.add_company_imageURL);
        editBtn = findViewById(R.id.Add_company_addBtn);
        editBtn.setText("Edit");



    }

    private void deploy(){
        // get data from prev activity
        FlightCompaniesModel model = this.getIntent().getParcelableExtra("mModel");
        stId = model.getId();
        stTitle = model.getTitle();
        getEncodedImage = model.getLogo();
        //deploy data
        edTitle.setText(stTitle);
        edEmail.setText(model.getEmail());
        edFax.setText(model.getFax());
        edPhone.setText(model.getPhone());
        edAddress.setText(model.getAddress());

        tvAddImg.setText(Html.fromHtml("<u>Edit Image</u>"));
        Picasso.get().load(getEncodedImage).fit().into(ivAddImg);


        tvAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageUrl(EditCompanyActivity.this);
            }
        });

        ivAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageUrl(EditCompanyActivity.this);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }

    private void Validation() {
        stTitle = edTitle.getText().toString();
        stTitle = edTitle.getText().toString();
        stEmail = edEmail.getText().toString();
        stFax = edFax.getText().toString();
        stPhone = edPhone.getText().toString();
        stAddress = edAddress.getText().toString();



        if (stTitle == null || stTitle.isEmpty()
                || getEncodedImage == null || getEncodedImage.isEmpty()
                || stEmail == null || stEmail.isEmpty()
                || stFax == null || stFax.isEmpty()
                || stPhone == null || stPhone.isEmpty()
                || stAddress == null || stAddress.isEmpty()
        ){
            Toast.makeText(this, "Some values are missing", Toast.LENGTH_SHORT).show();
        }else {
            postData(stId,stTitle.toLowerCase(), getEncodedImage, stEmail, stFax, stPhone, stAddress);
        }


    }


    private void postData(String id,String trip_title, String imgUrl, String email, String fax, String phone, String address){
        final DocumentReference companiesRef = mDb
                .collection("companies")
                .document(id);

        FlightCompaniesModel newCompany = new FlightCompaniesModel(id, trip_title, imgUrl, email, fax, phone, address);
        companiesRef.set(newCompany).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(EditCompanyActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();

                } else {


                }
            }
        });


    }


    @Override
    public void get_Url(String url) {
        getEncodedImage = url;
        Picasso.get().load(url).fit().centerInside().into(ivAddImg);
        tvAddImg.setText(Html.fromHtml("<u>Edit Image</u>"));
    }
}