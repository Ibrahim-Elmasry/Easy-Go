package com.example.task;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SortFilterBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private RadioGroup rgCompany, rgPrice, rgOrder;
    private Button saveBtn;
    private String stCompany = "null", stPrice = "null", stOrder = "null";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sort_filter_layout, container, false);


        rgCompany = v.findViewById(R.id.rgCompany);
        rgPrice = v.findViewById(R.id.rgPrice);
        rgOrder = v.findViewById(R.id.rgOrder);
        saveBtn = v.findViewById(R.id.savFilter);


        rgCompany.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.radio_c1:
                        stCompany = "c1";
                        break;
                    case R.id.radio_c2:
                        stCompany = "c2";
                        break;
                    case R.id.radio_c3:
                        stCompany = "c3";
                        break;
                }
            }

        });

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
}
