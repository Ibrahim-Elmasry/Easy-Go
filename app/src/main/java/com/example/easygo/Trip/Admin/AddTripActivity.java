package com.example.easygo.Trip.Admin;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.BasicData.Companies.AddCompanyActivity;
import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.Models.DbModels.FlightCompaniesModel;
import com.example.easygo.Models.DbModels.ProgramModel;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.R;
import com.example.easygo.Utils.BaseUploadImageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTripActivity extends BaseUploadImageActivity implements BaseUploadImageActivity.ImageInterface {

    private EditText edTitle, edPrice, edDuration, edNumberOfTravelers, edDescription;
    private TextView tvStartAt, tvEndAt, tvAddImage,tvPrograms;
    private ImageView ivLogo;
    private String stTitle, stPrice, stDuration, stNumberOfTravelers, stDescription, stImageUrl, stStartAt, stEndAt;
    private Spinner spCompany, spCity , spProgram;
    private Button addBtn,addProgramBtn;
    private FirebaseFirestore mDb;
    private final Calendar myCalendar = Calendar.getInstance();
    private Date dateDepart,dateReturn;

    private List<CitiesModel> citiesList;
    private CitiesModel cmCity;
    private List<FlightCompaniesModel> flightCompanies;
    private List<ProgramModel> programModelList;
    private FlightCompaniesModel fcmCompany;
    private ProgramModel proModel;
    private List<String> sourceList, companyList,programList,selectedPrograms;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        showBackBtn("Add Trip");

        init();
        deploy();


    }

    private void init() {
        mDb = FirebaseFirestore.getInstance();

        sourceList = new ArrayList<>();
        companyList = new ArrayList<>();
        flightCompanies = new ArrayList<>();
        citiesList = new ArrayList<>();
        programList = new ArrayList<>();
        selectedPrograms = new ArrayList<>();
        programModelList= new ArrayList<>();

        edTitle = findViewById(R.id.Add_trip_title);
        edPrice = findViewById(R.id.Add_trip_price);
        edDuration = findViewById(R.id.Add_trip_duration);
        edNumberOfTravelers = findViewById(R.id.Add_trip_num);
        edDescription = findViewById(R.id.Add_trip_Desc);
        tvStartAt = findViewById(R.id.Add_trip_startAt);
        tvEndAt = findViewById(R.id.Add_trip_endAt);
        tvPrograms = findViewById(R.id.Add_trip_programList);

        ivLogo = findViewById(R.id.Add_trip_imageURL);
        tvAddImage = findViewById(R.id.Add_trip_imageName);
        spCompany = findViewById(R.id.Add_trip_company);
        spProgram= findViewById(R.id.Add_trip_programSpinner);
        spCity = findViewById(R.id.Add_trip_city);
        addBtn = findViewById(R.id.Add_trip_addBtn);
        addProgramBtn = findViewById(R.id.Add_trip_addProgram);

        getCities();
        getCompany();
        getProgram();
    }


    private void deploy() {
        tvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageUrl(AddTripActivity.this);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });

        addProgramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proModel!=null){
                    selectedPrograms.add(proModel.getName());
                    tvPrograms.append(proModel.getName() + " , ");
                }
            }
        });
        datePicker();
    }

    @Override
    public void get_Url(String url) {
        stImageUrl = url;
        Picasso.get().load(url).fit().centerInside().into(ivLogo);
        tvAddImage.setText(Html.fromHtml("<u>Edit Image</u>"));
    }

    private void Validation() {
        stTitle = edTitle.getText().toString();
        stPrice = edPrice.getText().toString();
        stDuration = edDuration.getText().toString();
        stNumberOfTravelers = edNumberOfTravelers.getText().toString();
        stDescription = edDescription.getText().toString();


        if (stTitle == null || stTitle.isEmpty()
                || stPrice == null || stPrice.isEmpty()
                || stDuration == null || stDuration.isEmpty()
                || stNumberOfTravelers == null || stNumberOfTravelers.isEmpty()
                || stDescription == null || stDescription.isEmpty()
                || stImageUrl == null || stImageUrl.isEmpty()
                || stStartAt == null || stStartAt.isEmpty()
                || stEndAt == null || stEndAt.isEmpty()
                || cmCity == null
                || fcmCompany == null
                || selectedPrograms ==null || selectedPrograms.size()==0) {

        } else if (dateReturn.before(dateDepart)){
            Toast.makeText(this, "End Date can't be before Start Date", Toast.LENGTH_SHORT).show();
            tvEndAt.setError("End Date can't be before Start Date");
        } else {
            postData(stTitle.toLowerCase(), stPrice, stDuration, stNumberOfTravelers, stDescription, stImageUrl, stStartAt, stEndAt, cmCity, fcmCompany,selectedPrograms);
        }


    }


    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    private void postData(String title, String price, String duration, String numberOfTravelers, String description,
                          String imageUrl, String startAt, String endAt, CitiesModel cmCity, FlightCompaniesModel fcmCompany,List<String> proList) {
        final DocumentReference tripRef = mDb
                .collection("trips")
                .document();

        TripModel newTrip = new TripModel(tripRef.getId(),title, price, duration, numberOfTravelers, description,
                imageUrl, startAt, endAt, cmCity, fcmCompany,proList);
        tripRef.set(newTrip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(AddTripActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    finish();

                } else {


                }
            }
        });


    }

    //Date Picker
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

        tvStartAt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTripActivity.this, departDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        tvEndAt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTripActivity.this, returnDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
    }

    private void updateLabel(int check) {
        String myFormat = "dd/MM/yy"; //time Format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (check == 1) {
            stStartAt = sdf.format(myCalendar.getTime());
            dateDepart =myCalendar.getTime();
            tvStartAt.setText(stStartAt);
        } else if (check == 2) {
            stEndAt = sdf.format(myCalendar.getTime());
            tvEndAt.setText(stEndAt);
            dateReturn =myCalendar.getTime();

        }

    }


    //Spinners
    private void setCompanySpinner(final List<FlightCompaniesModel> fcm) {
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
        spCompany.setAdapter(CompanySpinnerArrayAdapter);
        spCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCitySpinner(final List<CitiesModel> cm) {
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> citySpinnerArrayAdapter = new ArrayAdapter<String>(
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

        citySpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spCity.setAdapter(citySpinnerArrayAdapter);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


                    cmCity = cm.get(position - 1);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setProgramSpinner(final List<ProgramModel> pro) {
        final ArrayAdapter<String> CompanySpinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, programList) {
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
        spProgram.setAdapter(CompanySpinnerArrayAdapter);
        spProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    proModel = pro.get(position - 1);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getProgram() {
        CollectionReference collectionReference = mDb
                .collection("program");

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
                    programModelList.clear();
                    programList.clear();

                    programList.add(0, "Program...");
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ProgramModel getModel = doc.toObject(ProgramModel.class);
                        programList.add(getModel.getName());
                        programModelList.add(getModel);
                    }
                    setProgramSpinner(programModelList);
                }
            }
        });


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

                    sourceList.add(0, "City...");
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        CitiesModel getModel = doc.toObject(CitiesModel.class);
                        sourceList.add(getModel.getName());
                        citiesList.add(getModel);
                    }
                    setCitySpinner(citiesList);
                }
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
