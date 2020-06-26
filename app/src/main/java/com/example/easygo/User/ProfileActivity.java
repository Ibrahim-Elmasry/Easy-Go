package com.example.easygo.User;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.UserModel;
import com.example.easygo.R;
import com.example.easygo.User.Flight.YourFlightActivity;
import com.example.easygo.User.Trip.YourTripActivity;
import com.example.easygo.UserClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

//pdf test


public class ProfileActivity extends BaseActivity {
    private EditText edName, edEmail, edPhone, edPassword;
    private String stName, stEmail, stPhone, stPassword,stType;
    private TextView tvUserName, tvYourFlightBtn, tvYourTripBtn, tvUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        deploy();
        ActivityCompat.requestPermissions(ProfileActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);


    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init() {
        edName = findViewById(R.id.profile_edName);
        edEmail = findViewById(R.id.profile_edEmail);
        edPhone = findViewById(R.id.profile_edPhone);
        edPassword = findViewById(R.id.profile_edPassword);
        tvUpdateBtn = findViewById(R.id.profile_Update);
        tvUserName = findViewById(R.id.profile_userName);
        tvYourFlightBtn = findViewById(R.id.profile_yourFlight);
        tvYourTripBtn = findViewById(R.id.profile_yourTrip);

    }

    private void deploy() {
        UserModel user = ((UserClient) (getApplicationContext())).getUser();
        edName.setText(user.getName());
        edPhone.setText(user.getPhone());
        edEmail.setText(user.getEmail());
        edPassword.setText(user.getPassword());
        tvUserName.setText(user.getName());
        stType = user.getType();
        tvUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        tvYourFlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customStartActivityForResult(ProfileActivity.this, YourFlightActivity.class);
            }
        });

        tvYourTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customStartActivityForResult(ProfileActivity.this, YourTripActivity.class);
            }
        });
    }

    private void validation() {
        stName = edName.getText().toString();
        stPhone = edPhone.getText().toString();
        stEmail = edEmail.getText().toString();
        stPassword = edPassword.getText().toString();


        if (stName == null || stName.isEmpty()
                || stPassword == null || stPassword.isEmpty()
                || stPhone == null || stPhone.isEmpty()
                || stType == null || stType.isEmpty()
        ) {
            Toast.makeText(this, "Missing Values", Toast.LENGTH_SHORT).show();
        } else {
            // do something
            editUser(stName, stPhone, stEmail, stPassword , stType);
            tvUpdateBtn.setClickable(false);
        }


    }

    private void editUser(final String name, final String phone, final String email, final String password,  final String type) {

        if (FirebaseAuth.getInstance().getUid() != null) {
            DocumentReference userRef = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(FirebaseAuth.getInstance().getUid());

            userRef.update("name" , name , "phone" , phone ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "User Updated", Toast.LENGTH_SHORT).show();
                        saveUser();
                    } else {
                        Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                        tvUpdateBtn.setClickable(true);

                    }
                }
            });


        } else {
            Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
            tvUpdateBtn.setClickable(true);

        }


    }

    private void saveUser() {
        if (FirebaseAuth.getInstance().getUid() != null) {
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.e("HomeActivity", "onComplete: successfully set the user client.");
                        final UserModel user = Objects.requireNonNull(task.getResult()).toObject(UserModel.class);
                        ((UserClient) (getApplicationContext())).setUser(user);
                        tvUserName.setText(Objects.requireNonNull(user).getName());
                        edName.setText(user.getName());
                        edPhone.setText(user.getPhone());
                        tvUpdateBtn.setClickable(true);

                    }else {
                        tvUpdateBtn.setClickable(true);
                    }
                }
            });



        }


    }










        }
