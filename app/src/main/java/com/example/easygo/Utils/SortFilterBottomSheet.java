package com.example.easygo.Utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.Models.DbModels.FlightCompaniesModel;
import com.example.easygo.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SortFilterBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private RadioGroup rgPrice, rgOrder;
    private Button saveBtn;
    private String stCompany = "null", stPrice = "null", stOrder = "null";
    private Spinner companySpinner;


    private FirebaseFirestore mDb;
    private List<String> companyList;
    private List<FlightCompaniesModel> flightCompanies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sort_filter_layout, container, false);
        mDb = FirebaseFirestore.getInstance();
        companyList = new ArrayList<>();
        flightCompanies = new ArrayList<>();

        companySpinner = v.findViewById(R.id.rgCompany);
        rgPrice = v.findViewById(R.id.rgPrice);
        rgOrder = v.findViewById(R.id.rgOrder);
        saveBtn = v.findViewById(R.id.savFilter);


        rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.radio_LTHPrice:
                        stPrice = "lth";
                        break;
                    case R.id.radio_HTLPrice:
                        stPrice = "htl";
                        break;
                }
            }

        });

        rgOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.radio_a2z:
                        stOrder = "a2z";
                        break;
                    case R.id.radio_z2a:
                        stOrder = "z2a";
                        break;
                }
            }

        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSaveButtonClicked(stCompany, stPrice, stOrder);
                dismiss();
            }
        });

        getCompany();

        return v;
    }

    public interface BottomSheetListener {
        void onSaveButtonClicked(String company, String price, String order);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }


    private void getCompany() {
        CollectionReference collectionReference = mDb
                .collection("companies");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
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

    //Spinners

    //Spinners
    private void setCompanySpinner(final List<FlightCompaniesModel> fcm) {
        if (getContext() != null) {
            final ArrayAdapter<String> CompanySpinnerArrayAdapter = new ArrayAdapter<String>(
                    getContext(), R.layout.spinner_item, companyList) {
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
                    // If user change the default selection
                    // First item is disable and it is used for hint
                    if (position > 0) {
                        // Notify the selected item text

                        //cityFrom
                        // stCompany = selectedItemText;
                        stCompany = fcm.get(position - 1).getId();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
