package com.example.easygo.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends BaseActivity {
    private EditText edEmail, edPassword;
    private String stEmail, stPassword;
    private TextView tvSignUp;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        deploy();
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }



    private void init() {
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_logIn);
        edEmail = findViewById(R.id.Login_email);
        edPassword = findViewById(R.id.Login_password);
        btnLogin = findViewById(R.id.Login_SignInBtn);
        tvSignUp= findViewById(R.id.Login_SignUpBtn);
    }

    private void deploy() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validation();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSignUp();
            }
        });
    }

    private void validation() {
        stEmail = edEmail.getText().toString();

        stPassword = edPassword.getText().toString();

        if (stEmail==  null || stEmail.isEmpty()
                || stPassword==  null || stPassword.isEmpty()) {
            Toast.makeText(this, "Missing Values", Toast.LENGTH_SHORT).show();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(stEmail, stPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // if the login completed
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        saveUser();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login Error" , Toast.LENGTH_LONG).show();

                    }

                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            saveUser();
        }
    }

    private void sendToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUser(){
        if (FirebaseAuth.getInstance().getUid() != null) {
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            final UserModel user = task.getResult().toObject(UserModel.class);
                            ((UserClient) (getApplicationContext())).setUser(user);
                            if (user != null){

                                if (user.getType().equals("admin")){
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    i.putExtra("type","admin");
                                    startActivity(i);
                                    finish();
                                }else {
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    i.putExtra("type","user");
                                    startActivity(i);
                                    finish();
                                }

                            }


                        }


                    }
                }
            });


        }


    }
}
