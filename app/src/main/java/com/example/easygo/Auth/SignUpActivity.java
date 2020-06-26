package com.example.easygo.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.HomeActivity;
import com.example.easygo.Models.UserModel;
import com.example.easygo.R;
import com.example.easygo.UserClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity {
    private static final String TAG = "SignUpActivity";
    private EditText edName, edPhone, edEmail, edPassword, edConfirmPassword;
    private String stUserType,stName, stPhone, stEmail, stPassword, stConfirmPassword;
    private TextView tvSignIn;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private FirebaseFirestore mDb;
private RadioGroup rgUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        deploy();
    }
    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }
    private void sendToHome(String type) {
        if (type.equals("admin")){
            Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
            i.putExtra("type","admin");

            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
            i.putExtra("type","user");
            startActivity(i);
            finish();
        }
    }


    private void sendToLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        sendToLogIn();
    }

    private void init() {
        mDb = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar_SignUp);
        edEmail = findViewById(R.id.SignUp_email);
        edPassword = findViewById(R.id.SignUp_password);
        edName = findViewById(R.id.SignUp_name);
        edPhone = findViewById(R.id.SignUp_number);
        edConfirmPassword = findViewById(R.id.SignUp_repeat_password);

        btnSignUp = findViewById(R.id.SignUp_signUpBtn);
        tvSignIn = findViewById(R.id.SignUp_signInBtn);

        rgUserType = findViewById(R.id.SignUp_userType);

    }

    private void deploy() {

        rgUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.SignUp_typeUser:
                        stUserType = "user";
                        break;
                    case R.id.SignUp_typeAdmin:
                        stUserType = "admin";
                        break;

                }
            }

        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogIn();
            }
        });
    }


    private void validation() {
        stName = edName.getText().toString();
        stPhone = edPhone.getText().toString();
        stEmail = edEmail.getText().toString();
        stPassword = edPassword.getText().toString();
        stConfirmPassword = edConfirmPassword.getText().toString();

        // Pattern match for email id
        String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(stEmail);


        if (stEmail == null || stEmail.isEmpty()
                || stPassword == null || stPassword.isEmpty()
                || stConfirmPassword == null || stConfirmPassword.isEmpty()
                || stName == null || stName.isEmpty()
                || stPhone == null || stPhone.isEmpty()
                || stUserType == null || stUserType.isEmpty()
        ) {
            Toast.makeText(this, "Missing Values", Toast.LENGTH_SHORT).show();
        } else if (!m.find()) {
            edEmail.setError("Your Email Id is Invalid.");
        } else if (!stConfirmPassword.equals(stPassword)) {
            edPassword.setError("Both password doesn't match.");
        } else if (stPassword.length() < 6) {
            edPassword.setError("Password length must be greater than 6 letters");
        } else {
            doSignUp(stUserType,stName, stPhone, stEmail, stPassword);
        }


    }

    private void doSignUp(final String type,final String name, final String phone, final String email, final String password) {

        progressBar.setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {

                            //insert some default data
                            final UserModel user = new UserModel();
                            user.setEmail(email);
                            user.setName(name);
                            user.setType(type);
                            user.setId(FirebaseAuth.getInstance().getUid());
                            user.setPassword(password);
                            user.setPhone(phone);

                            if (FirebaseAuth.getInstance().getUid() != null) {
                                DocumentReference newUserRef = mDb
                                        .collection("users")
                                        .document(FirebaseAuth.getInstance().getUid());

                                newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            ((UserClient) (getApplicationContext())).setUser(user);
                                            progressBar.setVisibility(View.GONE);
                                            sendToHome(user.getType());
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(SignUpActivity.this, "Error" + task.getException(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    }
                });

    }

}






