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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCompanyActivity extends BaseUploadImageActivity implements BaseUploadImageActivity.ImageInterface {
    private EditText edTitle, edEmail, edFax, edPhone, edAddress;
    private TextView tvAddImg;
    private ImageView ivAddImg;
    private String stTitle, stEmail, stFax, stPhone, stAddress;
    private Button addBtn;
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

    private void init() {
        showBackBtn("Add Company");
        mDb = FirebaseFirestore.getInstance();

        edTitle = findViewById(R.id.add_company_title);
        edEmail = findViewById(R.id.add_company_email);
        edFax = findViewById(R.id.add_company_fax);
        edPhone = findViewById(R.id.add_company_phone);
        edAddress = findViewById(R.id.add_company_address);

        tvAddImg = findViewById(R.id.add_company_imageName);
        ivAddImg = findViewById(R.id.add_company_imageURL);
        addBtn = findViewById(R.id.Add_company_addBtn);


    }

    private void deploy() {
        tvAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageUrl(AddCompanyActivity.this);
            }
        });

        ivAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageUrl(AddCompanyActivity.this);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }

    private void Validation() {
        stTitle = edTitle.getText().toString();

        stEmail = edEmail.getText().toString();
        stFax = edFax.getText().toString();
        stPhone = edPhone.getText().toString();
        stAddress = edAddress.getText().toString();


        // Pattern match for email id
        String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(stEmail);



        if (stTitle == null || stTitle.isEmpty()
                || getEncodedImage == null || getEncodedImage.isEmpty()
                || stEmail == null || stEmail.isEmpty()
                || stFax == null || stFax.isEmpty()
                || stPhone == null || stPhone.isEmpty()
                || stAddress == null || stAddress.isEmpty()
        ) {
            Toast.makeText(this, "Some values are missing", Toast.LENGTH_SHORT).show();
        }else if (!m.find()) {
            edEmail.setError("Your Email Id is Invalid.");
        } else {
            postData(stTitle.toLowerCase(), getEncodedImage, stEmail, stFax, stPhone, stAddress);
        }


    }

    private void postData(String trip_title, String imgUrl, String email, String fax, String phone, String address) {
        final DocumentReference companiesRef = mDb
                .collection("companies")
                .document();

        FlightCompaniesModel newCompany = new FlightCompaniesModel(companiesRef.getId(), trip_title, imgUrl, email, fax, phone, address);
        companiesRef.set(newCompany).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(AddCompanyActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
