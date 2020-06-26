package com.example.easygo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.easygo.Auth.LoginActivity;
import com.example.easygo.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    saveUser();
                } else {
                    // User is signed out, send to register/login
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }


    private void saveUser()     {
        if (FirebaseAuth.getInstance().getUid() != null) {
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.e("HomeActivity", "onComplete: successfully set the user client.");
                        if (task.getResult() != null) {
                            final UserModel user = task.getResult().toObject(UserModel.class);
                            ((UserClient) (getApplicationContext())).setUser(user);
                            if (user != null){

                                if (user.getType().equals("admin")){
                                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                    i.putExtra("type","admin");
                                    i.putExtra("name",user.getName());

                                    startActivity(i);
                                    finish();
                                }else {
                                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                    i.putExtra("type","user");
                                    i.putExtra("name",user.getName());
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