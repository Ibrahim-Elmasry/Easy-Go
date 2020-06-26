package com.example.easygo.Flight.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.Models.DbModels.FlightCompaniesModel;
import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditFlightActivity extends BaseActivity {

    private static final String TAG = "EditFlightActivity";
    //image
    private static final int RESULT_IMG = 1;
    private final Calendar myCalendar = Calendar.getInstance();
    private String getEncodedImage = "";
    private EditText edVipPrice, edVipCount, edBusinessPrice, edBusinessCount, edEconomicPrice, edEconomicCount, edDuration, edStopsNo;
    private TextView tvDepart, tvReturn, tvAddImage;
    private Spinner companySpinner, fromSpinner, toSpinner;
    private CheckBox cbMeal, cbRefund;
    private ImageView ivLogo;
    private String stId,stVipPrice, stVipCount, stBusinessPrice, stBusinessCount, stEconomicPrice, stEconomicCount,
            stDuration, stStopsNo, stDepart, stReturn, stMeal = "No Meal", stRefund = "Non-Refundable";
    private Button addBtn;
    private Date dateDepart,dateReturn;

    private FirebaseFirestore mDb;
    private List<CitiesModel> citiesList;
    private CitiesModel cmSource, cmDestination;
    private List<FlightCompaniesModel> flightCompanies;
    private FlightCompaniesModel fcmCompany;
    private List<String> sourceList, destinationList, companyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);

        init();
        deploy();


    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }

    //init & deploy
    private void init() {
        showBackBtn("Edit Flight");



        mDb = FirebaseFirestore.getInstance();
        sourceList = new ArrayList<>();
        destinationList = new ArrayList<>();
        companyList = new ArrayList<>();
        flightCompanies = new ArrayList<>();
        citiesList = new ArrayList<>();

        edVipPrice = findViewById(R.id.add_flight_vipPrice);
        edVipCount = findViewById(R.id.add_flight_vipCount);
        edBusinessPrice = findViewById(R.id.add_flight_BusinessPrice);
        edBusinessCount = findViewById(R.id.add_flight_BusinessCount);
        edEconomicPrice = findViewById(R.id.add_flight_economicPrice);
        edEconomicCount = findViewById(R.id.add_flight_economicCount);

        edDuration = findViewById(R.id.add_flight_duration);
        edStopsNo = findViewById(R.id.add_flight_noofstops);

        tvDepart = findViewById(R.id.add_flight_departure);
        tvReturn = findViewById(R.id.add_flight_return);
        tvAddImage = findViewById(R.id.add_flight_imagename);

        companySpinner = findViewById(R.id.add_flight_company);
        fromSpinner = findViewById(R.id.add_flight_from);
        toSpinner = findViewById(R.id.add_flight_to);

        cbMeal = findViewById(R.id.add_flight_meal);
        cbRefund = findViewById(R.id.add_flight_refund);

        ivLogo = findViewById(R.id.add_flight_imageurl);


        addBtn = findViewById(R.id.add_flight_addbtn);
        addBtn.setText("Edit");
        setOldData();
    }

    private void setOldData() {
        FlightModel model = this.getIntent().getParcelableExtra("mModel");
        stId = model.getId();
        stVipPrice = String.valueOf(model.getVipPrice());
        stVipCount = String.valueOf(model.getVipCount());

        stBusinessPrice = String.valueOf(model.getBusinessPrice());
        stBusinessCount = String.valueOf(model.getBusinessCount());

        stEconomicPrice = String.valueOf(model.getEconomicPrice());
        stEconomicCount = String.valueOf(model.getEconomicCount());

        stDuration = model.getDuration();
        stStopsNo = model.getStopsNo();
        stDepart = model.getDepartDate();
        stReturn = model.getReturnDate();
        stMeal = model.getMeal();
        stRefund = model.getRefund();
        cmSource = model.getSource();
        cmDestination = model.getDestination();
        fcmCompany = model.getCompany();

        String myFormat = "dd/MM/yy"; //time Format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        try {
            dateDepart = sdf.parse(stDepart);
            dateReturn = sdf.parse(stReturn);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //editText
        edVipPrice.setText(stVipPrice);
        edVipCount.setText(stVipCount);

        edBusinessPrice.setText(stBusinessPrice);
        edBusinessCount.setText(stBusinessCount);

        edEconomicPrice.setText(stEconomicPrice);
        edEconomicCount.setText(stEconomicCount);

        edDuration.setText(stDuration);
        edStopsNo.setText(stStopsNo);

        //textView
        tvDepart.setText(stDepart);
        tvReturn.setText(stReturn);

        //checkbox
        if (stMeal.equals("No Meal")) {cbMeal.setChecked(false); } else { cbMeal.setChecked(true); }
        if (stRefund.equals("Non-Refundable")) { cbRefund.setChecked(false); } else { cbRefund.setChecked(true); }

        //spinners
        getCities(model.getSource().getName(),model.getDestination().getName());
        getCompany(model.getCompany().getTitle());


    }

    private void deploy() {
        datePicker();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });

        tvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });


        cbMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stMeal = "Meal Included";
                } else {
                    stMeal = "No Meal";

                }
            }
        });

        cbRefund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stRefund = "Refundable";
                } else {
                    stRefund = "Non-Refundable";
                }
            }
        });
    }

    //validate
    private void Validation() {
        stVipPrice = edVipPrice.getText().toString();
        stVipCount = edVipCount.getText().toString();
        stBusinessPrice = edBusinessPrice.getText().toString();
        stBusinessCount = edBusinessCount.getText().toString();
        stEconomicPrice = edEconomicPrice.getText().toString();
        stEconomicCount = edEconomicCount.getText().toString();

        stDuration = edDuration.getText().toString();
        stStopsNo = edStopsNo.getText().toString();

        if (stVipPrice == null || stVipPrice.isEmpty()
                || stVipCount == null || stVipCount.isEmpty()
                || stId == null || stId.isEmpty()
                || stBusinessPrice == null || stBusinessPrice.isEmpty()
                || stBusinessCount == null || stBusinessCount.isEmpty()
                || stEconomicPrice == null || stEconomicPrice.isEmpty()
                || stEconomicCount == null || stEconomicCount.isEmpty()
                || stDuration == null || stDuration.isEmpty()
                || stStopsNo == null || stStopsNo.isEmpty()
                || stDepart == null || stDepart.isEmpty()
                || stReturn == null || stReturn.isEmpty()
                || cmSource.getName() == null || cmSource.getName().isEmpty()
                || cmDestination.getName() == null || cmDestination.getName().isEmpty()
                || fcmCompany.getTitle() == null || fcmCompany.getTitle().isEmpty()
                || stMeal == null || stMeal.isEmpty()
                || stRefund == null || stRefund.isEmpty()) {

        } else if (dateReturn.before(dateDepart)){
            Toast.makeText(this, "Return Date can't be before Departure Date", Toast.LENGTH_SHORT).show();
            tvReturn.setError("Return Date can't be before Departure Date");
        }else {
            postData(stId,stVipPrice, stVipCount, stBusinessPrice, stBusinessCount,
                    stEconomicPrice, stEconomicCount, stDuration, stStopsNo,
                    stDepart, stReturn, stMeal, stRefund,
                    cmSource, cmDestination, fcmCompany);

        }

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
                    companyList.set(0, "Company...");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    sourceList.set(0, "Source...");


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
                    destinationList.set(0, "Destination...");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        tvDepart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditFlightActivity.this, departDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        tvReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditFlightActivity.this, returnDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
    }

    private void updateLabel(int check) {
        String myFormat = "dd/MM/yy"; //time Format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (check == 1) {
            dateDepart = myCalendar.getTime();
            stDepart = sdf.format(myCalendar.getTime());
            tvDepart.setText(stDepart);
        } else if (check == 2) {
            dateReturn = myCalendar.getTime();
            stReturn = sdf.format(myCalendar.getTime());
            tvReturn.setText(stReturn);
        }

    }

    //Post Data
    private void postData(String id,String vipPrice, String vipCount, String businessPrice, String businessCount,
                          String economicPrice, String economicCount,
                           String duration, String stopsNo,
                          String departDate, String returnDate, String meal, String refund,
                          CitiesModel source, CitiesModel destination, FlightCompaniesModel company) {


        final DocumentReference flightRef = mDb
                .collection("flights")
                .document(id);



        FlightModel newFlight = new FlightModel(flightRef.getId(),
                Integer.valueOf(vipPrice), Integer.valueOf(vipCount), Integer.valueOf(economicPrice), Integer.valueOf(economicCount),
                        Integer.valueOf(businessPrice), Integer.valueOf(businessCount), duration, stopsNo,
                departDate, returnDate, meal, refund,
                source, destination, company);
        flightRef.set(newFlight).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(EditFlightActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    /*Intent i = new Intent(AddTripActivity.this, EditFlightActivity.class);
                    startActivity(i);*/
                    finish();

                } else {


                }
            }
        });


    }


    //image methods
    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_IMG && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            final InputStream imageStream;

            try {
                //Picasso.get().load(imageUri).fit().centerInside().into(ivAddImg);
                tvAddImage.setText(Html.fromHtml("<u>Edit Image</u>"));
                imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                // this is the image Sting
                getEncodedImage = /*"data:image/jpeg;base64," + */encodeImage(selectedImage);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                //Glide.with(EditFlightActivity.this).load(decodeImageBm(getEncodedImage)).asBitmap().into(ivLogo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private byte[] decodeImageBm(String encodedImage) {
        String cleanImage = encodedImage.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "");
        byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }


    private void getCities(final String src, final String dest) {
        CollectionReference collectionReference = mDb
                .collection("city");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("EditFlightActivity", "onEvent: Listen failed.", e);
                    return;
                }
                Log.e("EditFlightActivity", "onEvent:2 ");

                if (queryDocumentSnapshots != null) {
                    Log.e("EditFlightActivity", "onEvent: ");
                    // Clear the list and add all the users again
                    citiesList.clear();
                    sourceList.clear();
                    destinationList.clear();

                    sourceList.add(0, src);
                    destinationList.add(0, dest);
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

    private void getCompany(final String init) {
        CollectionReference collectionReference = mDb
                .collection("companies");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("EditFlightActivity", "onEvent: Listen failed.", e);
                    return;
                }
                Log.e("EditFlightActivity", "onEvent:2 ");

                if (queryDocumentSnapshots != null) {
                    Log.e("EditFlightActivity", "onEvent: ");
                    // Clear the list and add all the users again
                    flightCompanies.clear();
                    companyList.clear();

                    companyList.add(0, init);
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
