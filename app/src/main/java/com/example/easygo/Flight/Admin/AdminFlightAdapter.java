package com.example.easygo.Flight.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdminFlightAdapter extends RecyclerView.Adapter<AdminFlightAdapter.ViewHolder> {

    private Context context;
    private List<FlightModel> mList;

    private AdminFlightAdapter.AdapterListener onClickListener;


    public AdminFlightAdapter() {
    }

    public AdminFlightAdapter(Context context, List<FlightModel> mList, AdapterListener onClickListener) {
        this.context = context;
        this.mList = mList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AdminFlightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trip, parent, false);
        return new AdminFlightAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminFlightAdapter.ViewHolder holder, final int position) {

        holder.titleTV.setText(mList.get(position).getCompany().getTitle());
        holder.priceTV.setText(mList.get(position).getVipPrice() + " , " + mList.get(position).getBusinessPrice() + " , " +mList.get(position).getEconomicPrice() + " L.E " );
        Picasso.get().load(mList.get(position).getCompany().getLogo()).fit().centerInside().into(holder.logoIV);

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
        private TextView titleTV, priceTV;
        private ImageView logoIV,editIV,deleteIV;
        private LinearLayout detailsLinear;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.row_title);
            logoIV = itemView.findViewById(R.id.row_logo);
            priceTV = itemView.findViewById(R.id.row_price);
            editIV = itemView.findViewById(R.id.row_edit);
            deleteIV = itemView.findViewById(R.id.row_delete);
            detailsLinear = itemView.findViewById(R.id.row_getDetails);

            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     onClickListener.edit(v,mList.get(getAdapterPosition()));
                }
            });
            detailsLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.details(v,mList.get(getAdapterPosition()));
                }
            });
            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     onClickListener.delete(v,mList.get(getAdapterPosition()).getId());
                }
            });

        }
    }


    public interface AdapterListener {
        void details(View v, FlightModel model);

        void edit(View v, FlightModel model);

        void delete(View v, String id);

    }


}
