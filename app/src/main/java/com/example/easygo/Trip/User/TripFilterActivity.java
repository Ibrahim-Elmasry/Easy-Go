package com.example.easygo.Trip.User;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.FlightCompaniesModel;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.R;
import com.example.easygo.Utils.TripFilter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripFilterActivity extends BaseActivity {
    private RadioGroup rgPrice, rgArrival, rgDeparture;
    private Spinner companySpinner;
    private String stPrice, stDepart, stReturn, stCompany;
    private Button sortBtn;
    private List<TripModel> mList;

    private FirebaseFirestore mDb;

    private List<FlightCompaniesModel> flightCompanies;
    private FlightCompaniesModel fcmCompany;
    private List<String> companyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_filter);

        init();
        deploy();
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void init() {
        mDb = FirebaseFirestore.getInstance();
        companyList = new ArrayList<>();
        flightCompanies = new ArrayList<>();
        showBackBtn("Sort And Filter");

        companySpinner = findViewById(R.id.sort_flight_spAirLine);
        rgPrice = findViewById(R.id.sort_flight_rgPrice);
        rgArrival = findViewById(R.id.sort_flight_rgArrivalTime);
        rgDeparture = findViewById(R.id.sort_flight_rgDepartureTime);
        sortBtn = findViewById(R.id.sort_flight_sortBtn);

        if (this.getIntent().getExtras() != null) {
            mList = this.getIntent().getExtras().getParcelableArrayList("mod");

        }


    }

    private void deploy() {

        rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.sort_flight_rbPriceCheap:
                        stPrice = "low";
                        break;
                    case R.id.sort_flight_rbPriceExpensive:
                        stPrice = "high";
                        break;
                }
            }

        });

        rgArrival.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.sort_flight_rbArrivEarly:
                        stReturn = "early";
                        break;
                    case R.id.sort_flight_rbArrivLate:
                        stReturn = "late";
                        break;
                }
            }

        });
        rgDeparture.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.sort_flight_rbDepartEarly:
                        stDepart = "early";
                        break;
                    case R.id.sort_flight_rbDepartLate:
                        stDepart = "late";
                        break;
                }
            }

        });

        getCompany();

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortAndFilter();
            }
        });
    }

    private void sortAndFilter() {
        List<TripModel> filteredList = mList;


        //company selected selected
        if (stCompany != null && !stCompany.isEmpty()) {
            try {
                filteredList = Lists.newArrayList(Collections2.filter(mList,
                        new TripFilter(fcmCompany.getId())));
            } catch (Exception e) {
                Log.e("TripFilter", "error : : ", e);
            }

        }


        //price selected
        if (stPrice != null && !stPrice.isEmpty()) {
            if (stPrice.equals("low")) {
                Collections.sort(filteredList, new Comparator<TripModel>() {
                    @Override
                    public int compare(TripModel lhs, TripModel rhs) {
                        return Integer.valueOf(lhs.getPrice()).compareTo(Integer.valueOf(rhs.getPrice()));
                    }
                });
            } else {

                Collections.sort(filteredList, new Comparator<TripModel>() {
                    @Override
                    public int compare(TripModel lhs, TripModel rhs) {
                        return Integer.valueOf(rhs.getPrice()).compareTo(Integer.valueOf(lhs.getPrice()));
                    }
                });

            }


        }

        //Arrival Time selected
        if (stReturn != null && !stReturn.isEmpty()) {

            if (stReturn.equals("early")) {

                Collections.sort(filteredList, new Comparator<TripModel>() {
                    @Override
                    public int compare(TripModel lhs, TripModel rhs) {
                        return stringToDate(lhs.getEndAt()).compareTo(stringToDate(rhs.getEndAt()));
                    }
                });

            } else {

                Collections.sort(filteredList, new Comparator<TripModel>() {
                    @Override
                    public int compare(TripModel lhs, TripModel rhs) {
                        return stringToDate(rhs.getEndAt()).compareTo(stringToDate(lhs.getEndAt()));
                    }
                });

            }


        }


        //depart Time selected
        if (stDepart != null && !stDepart.isEmpty()) {
            if (stDepart.equals("early")) {

                Collections.sort(filteredList, new Comparator<TripModel>() {
                    @Override
                    public int compare(TripModel lhs, TripModel rhs) {
                        return stringToDate(lhs.getStartAt()).compareTo(stringToDate(rhs.getStartAt()));
                    }
                });

            } else {

                Collections.sort(filteredList, new Comparator<TripModel>() {
                    @Override
                    public int compare(TripModel lhs, TripModel rhs) {
                        return stringToDate(rhs.getStartAt()).compareTo(stringToDate(lhs.getStartAt()));
                    }
                });

            }
        }


        // send the data back
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("res", (ArrayList<? extends Parcelable>) filteredList);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }


    private Date stringToDate(String inputDate) {
        String myFormat = "dd/MM/yy"; //time Format
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        try {
            return dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }


    //Spinners
    private void setCompanySpinner(final List<FlightCompaniesModel> fcm) {
        // Initializing a String Array for 'From' Spinner

        /*String[] companyArray = new String[]{
                "Company...",
                "Egypt Air",
                "KSA Air",
                "Dubai Air",
                "UAE Air"
        };
        final List<String> companyFromList = new ArrayList<>(Arrays.asList(companyArray));
        // Initializing an ArrayAdapter

*/

        final ArrayAdapter<String> CompanySpinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, companyList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        CompanySpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        companySpinner.setAdapter(CompanySpinnerArrayAdapter);
        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();

                    //cityFrom
                    // stCompany = selectedItemText;
                    fcmCompany = fcm.get(position - 1);
                    stCompany = fcmCompany.getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCompany() {
        CollectionReference collectionReference = mDb
                .collection("companies");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("AdminTripsActivity", "onEvent: Listen failed.", e);
                    return;
                }
                Log.e("AdminTripsActivity", "onEvent:2 ");

                if (queryDocumentSnapshots != null) {
                    Log.e("AdminTripsActivity", "onEvent: ");
                    // Clear the list and add all the users again
                    flightCompanies.clear();
                    companyList.clear();

                    companyList.add(0, "Company...");
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FlightCompaniesModel getModel = doc.toObject(FlightCompaniesModel.class);
                        companyList.add(getModel.getTitle());
                        flightCompanies.add(getModel);
                    }
                    setCompanySpinner(flightCompanies);
                }
            }
        });


    }

}
