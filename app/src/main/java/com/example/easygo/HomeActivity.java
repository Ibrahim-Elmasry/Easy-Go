package com.example.easygo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easygo.Auth.LoginActivity;
import com.example.easygo.Trip.User.UserTripActivity;
import com.example.easygo.User.Flight.YourFlightActivity;
import com.example.easygo.User.ProfileActivity;
import com.example.easygo.BasicData.BasicDataActivity;
import com.example.easygo.Flight.Admin.AdminFlightActivity;
import com.example.easygo.Flight.User.FlightActivity;
import com.example.easygo.Models.UserModel;
import com.example.easygo.Trip.Admin.AdminTripsActivity;
import com.example.easygo.User.Trip.YourTripActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView ivSideNavBtn, ivFLightBtn, ivTripBtn, ivMenuHomeBtn, ivMenuProfileBn, ivMenuFlightBtn, ivMenuTripBtn;
    private TextView tvUserName;
    private String stUserName;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //define all the variables
        init();
        deploy();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {
        getUser();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        ivSideNavBtn = findViewById(R.id.home_SideButton);
        ivFLightBtn = findViewById(R.id.home_flight);
        ivTripBtn = findViewById(R.id.home_trip);
        ivMenuHomeBtn = findViewById(R.id.home_homeBtn);
        ivMenuProfileBn = findViewById(R.id.home_profile);
        ivMenuFlightBtn = findViewById(R.id.home_menuFlight);
        ivMenuTripBtn = findViewById(R.id.home_menuTrip);
        tvUserName = findViewById(R.id.home_userName);


        // side navigation drawer
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // get menu from navigationView
        getUser();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void deploy() {

        ivSideNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        ivFLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToFlight();
            }
        });
        ivTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToTrip();
            }
        });
        ivMenuHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivMenuProfileBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToProfile();
            }
        });
        ivMenuFlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToFlight();
            }
        });
        ivMenuTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToTrip();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = null; // not null
        if (id == R.id.nav_flight_reservation) {
            customStartActivityForResult(this, YourFlightActivity.class);
        } /*else if (id == R.id.nav_flight_cancelReservation) {
            //  i= new Intent(HomeActivity.this,);
           // startActivity(i);
        } */ else if (id == R.id.nav_trip_reservation) {
            customStartActivityForResult(this, YourTripActivity.class);
        }
        /*else if (id == R.id.nav_trip_cancelReservation) {
            //  i= new Intent(HomeActivity.this,);
          //  startActivity(i);
        }*/
        else if (id == R.id.nav_userProfile) {
            sendToProfile();
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SplashScreenActivity.class));
            finish();
        } else if (id == R.id.nav_flight_adminManage) {
            i = new Intent(HomeActivity.this, AdminFlightActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_trip_adminManage) {
            i = new Intent(HomeActivity.this, AdminTripsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_trip_adminBasicData) {
            i = new Intent(HomeActivity.this, BasicDataActivity.class);
            startActivity(i);
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
            UserModel user = ((UserClient) (getApplicationContext())).getUser();
            if (user != null)
                tvUserName.setText(user.getName());

        }
    }

    private void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void sendToFlight() {
        Intent intent = new Intent(this, FlightActivity.class);
        startActivity(intent);
    }

    private void sendToTrip() {
        Intent intent = new Intent(this, UserTripActivity.class);
        startActivity(intent);
    }


    private void getUser() {
        UserModel user = ((UserClient) (getApplicationContext())).getUser();
        tvUserName.setText(user.getName());
        Menu menu = navigationView.getMenu();
        MenuItem nav_adminPanel = menu.findItem(R.id.nav_adminPanel);

        if (user.getType().equals("admin")) {
            nav_adminPanel.setVisible(true);
        } else {
            nav_adminPanel.setVisible(false);
        }

    }

}
