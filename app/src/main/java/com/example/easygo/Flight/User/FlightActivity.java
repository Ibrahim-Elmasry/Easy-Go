package com.example.easygo.Flight.User;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.Models.DbModels.FlightCompaniesModel;
import com.example.easygo.Models.SearchFlightModel;
import com.example.easygo.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FlightActivity extends BaseActivity {
    private static final String TAG = "FlightActivity";
    private Spinner fromSpinner, toSpinner;
    private TextView tvDepart, tvReturn, tvAdultCount, tvChildCount, tvFlyBtn;
    private Button btnAddAdult, btnRemoveAdult, btnAddChild, btnRemoveChild;
    private RadioGroup rgFlightClass;
    private String  stTimeDepart, stTimeReturn, stCountAdult, stCountChild, stFlightClass;
    private final Calendar myCalendar = Calendar.getInstance();
    private ImageView closeBtn;
    private List<CitiesModel> citiesList;
    private CitiesModel cmSource, cmDestination;
    private List<String> sourceList, destinationList;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        init();

        deploy();
    }


    private void init() {
        mDb = FirebaseFirestore.getInstance();

        sourceList = new ArrayList<>();
        destinationList = new ArrayList<>();
        citiesList = new ArrayList<>();

        fromSpinner = (Spinner) findViewById(R.id.fly_spinner_from);
        toSpinner = (Spinner) findViewById(R.id.fly_spinner_to);
        //tv
        tvDepart = findViewById(R.id.fly_tv_depart);
        tvReturn = findViewById(R.id.fly_tv_return);
        tvAdultCount = findViewById(R.id.fly_tv_adultCount);
        tvChildCount = findViewById(R.id.fly_tv_childCount);
        //fly Btn
        tvFlyBtn = findViewById(R.id.fly_tv_flyBtn);
        //btns
        btnAddAdult = findViewById(R.id.fly_btn_addAdult);
        btnRemoveAdult = findViewById(R.id.fly_btn_removeAdult);
        btnAddChild = findViewById(R.id.fly_btn_addChild);
        btnRemoveChild = findViewById(R.id.fly_btn_removeChild);
        closeBtn = findViewById(R.id.fly_closeBtn);
        //rg
        rgFlightClass = findViewById(R.id.fly_rg_flightClass);
        getCities();
    }

    private void deploy() {
        datePicker();
        setRadioGroup();
        setAdultCount();
        setChildCount();
        setRadioGroup();

        tvFlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void setFromSpinner(final List<CitiesModel> cm) {
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> fromSpinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, sourceList) {
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

        fromSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        fromSpinner.setAdapter(fromSpinnerArrayAdapter);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    //stFrom = selectedItemText;


                    cmSource = cm.get(position - 1);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setToSpinner(final List<CitiesModel> cm) {

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> toSpinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, destinationList) {
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


        toSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        toSpinner.setAdapter(toSpinnerArrayAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                    //cityTo
                    //stTo = selectedItemText;

                    cmDestination = cm.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setChildCount() {
        final int[] count = {0};
        stCountChild = String.valueOf(count[0]);
        tvChildCount.setText(stCountChild);

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0] = count[0] + 1;
                stCountChild = String.valueOf(count[0]);
                tvChildCount.setText(stCountChild);
            }
        });

        btnRemoveChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] > 0) {
                    count[0] = count[0] - 1;
                    stCountChild = String.valueOf(count[0]);
                    tvChildCount.setText(stCountChild);

                }

            }
        });

    }

    private void setAdultCount() {
        final int[] count = {1};
        stCountAdult = String.valueOf(count[0]);
        tvAdultCount.setText(stCountAdult);

        btnAddAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0] = count[0] + 1;
                stCountAdult = String.valueOf(count[0]);
                tvAdultCount.setText(stCountAdult);
            }
        });

        btnRemoveAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] > 1) {
                    count[0] = count[0] - 1;
                    stCountAdult = String.valueOf(count[0]);
                    tvAdultCount.setText(stCountAdult);

                }

            }
        });
    }

    private void setRadioGroup() {
        rgFlightClass.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.fly_rb_economy:
                        stFlightClass = "economicCount";
                        break;
                    case R.id.fly_rb_business:
                        stFlightClass = "businessCount";
                        break;
                    case R.id.fly_rb_vip:
                        stFlightClass = "vipCount";
                        break;
                }
            }

        });
    }

    private void datePicker() {
        final DatePickerDialog.OnDateSetListener departDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(1);
            }

        };

        final DatePickerDialog.OnDateSetListener returnDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(2);
            }

        };

        tvDepart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(FlightActivity.this, departDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        tvReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(FlightActivity.this, returnDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
    }

    private void updateLabel(int check) {
        String myFormat = "dd/MM/yy"; //time Format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.e(TAG, "updateLabel: " + stTimeDepart);
        if (check == 1) {
            stTimeDepart = sdf.format(myCalendar.getTime());
            tvDepart.setText(stTimeDepart);
        } else if (check == 2) {
            stTimeReturn = sdf.format(myCalendar.getTime());
            tvReturn.setText(stTimeReturn);
        }

    }

    private void validation() {


        if (cmSource == null
                || cmDestination == null
                || stTimeDepart == null || stTimeDepart.isEmpty()
                || stTimeReturn == null || stTimeReturn.isEmpty()
                || stCountAdult == null || stCountAdult.isEmpty()
                || stCountChild == null || stCountChild.isEmpty()
                || stFlightClass == null || stFlightClass.isEmpty()) {
            Toast.makeText(this, "Missing Values", Toast.LENGTH_SHORT).show();

        } else {
            // do something
            serverRequest(cmSource, cmDestination, stTimeDepart, stTimeReturn, stCountAdult, stCountChild, stFlightClass);
        }
    }

    private void getCities() {
        CollectionReference collectionReference = mDb
                .collection("city");

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
                    citiesList.clear();
                    sourceList.clear();
                    destinationList.clear();

                    sourceList.add(0, "Source...");
                    destinationList.add(0, "Destination...");
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        CitiesModel getModel = doc.toObject(CitiesModel.class);
                        sourceList.add(getModel.getName());
                        destinationList.add(getModel.getName());
                        citiesList.add(getModel);
                    }
                    setFromSpinner(citiesList);
                    setToSpinner(citiesList);
                }
            }
        });


    }



    private void serverRequest(CitiesModel cmsource, CitiesModel cmdestination, String timeDepart, String timeReturn, String countAdult, String countChild, String flightClass) {
        SearchFlightModel model = new SearchFlightModel(cmsource, cmdestination,
                timeDepart, timeReturn, countAdult, countChild, flightClass);

        Intent intent = new Intent(FlightActivity.this, FlightResultActivity.class);
        intent.putExtra("mModel", model);
        startActivity(intent);
    }
}



