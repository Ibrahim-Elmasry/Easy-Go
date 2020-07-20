package com.example.easygo.Flight.User;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.easygo.Models.DbModels.BookedFlights;
import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.Models.UserFlightChoice;
import com.example.easygo.Models.UserFlightChoiceSorting;
import com.example.easygo.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class FlightResultAdapter extends RecyclerView.Adapter<FlightResultAdapter.ViewHolder> {

    private Context context;
    private List<UserFlightChoiceSorting> mList;
    private FlightResultAdapter.AdapterListener onClickListener;


    public FlightResultAdapter() {
    }

    public FlightResultAdapter(Context context, List<UserFlightChoiceSorting> mList, AdapterListener onClickListener) {
        this.context = context;
        this.mList = mList;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public FlightResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_flight_result, parent, false);
        return new FlightResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FlightResultAdapter.ViewHolder holder, final int position) {

       holder.tvTitle.setText(mList.get(position).getModel().getCompany().getTitle().toUpperCase());
       holder.tvFromTo.setText(mList.get(position).getModel().getDepartDate() + " - "+mList.get(position).getModel().getReturnDate());
       Picasso.get().load(mList.get(position).getModel().getCompany().getLogo()).fit().centerInside().into(holder.ivMainImg);
       holder.tvPrice.setText(mList.get(position).getChoice().getTotalPrice() + " L.E");

    }

    @Override
    public int getItemCount() {
        try {
            return mList.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvFromTo, tvPrice;
        private ImageView ivMainImg;
        private RelativeLayout rlDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.row_flight_title);
            tvFromTo = itemView.findViewById(R.id.row_flight_duration);
            tvPrice = itemView.findViewById(R.id.row_flight_price);
            ivMainImg = itemView.findViewById(R.id.row_flight_img);
            rlDetails = itemView.findViewById(R.id.row_flight_onclick);

            rlDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onClickListener.detailsFlight(v,mList.get(getAdapterPosition()));
                }
            });


        }
    }


    public interface AdapterListener {
        void detailsFlight(View v, UserFlightChoiceSorting model);
    }


}
