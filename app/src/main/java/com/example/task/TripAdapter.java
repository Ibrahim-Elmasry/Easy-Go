package com.example.task;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private Context context;
    private List<TripModel> mList;
    private List<String> mIdList;

    private TripAdapter.AdapterListener onClickListener;


    public TripAdapter() {
    }

    public TripAdapter(Context context, List<TripModel> mList, List<String> mIdList, AdapterListener onClickListener) {
        this.context = context;
        this.mList = mList;
        this.mIdList = mIdList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trip, parent, false);
        return new TripAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripAdapter.ViewHolder holder, final int position) {

        holder.titleTV.setText(mList.get(position).getTrip_title() + " to " + mList.get(position).getDestination());
        holder.priceTV.setText(mList.get(position).getPrice() + " L.E");

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
        private ImageView editIV,deleteIV;
        private LinearLayout detailsLinear;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.row_title);
            priceTV = itemView.findViewById(R.id.row_price);
            editIV = itemView.findViewById(R.id.row_edit);
            deleteIV = itemView.findViewById(R.id.row_delete);
            detailsLinear = itemView.findViewById(R.id.row_getDetails);

            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.editTrip(v,mList.get(getAdapterPosition()),mIdList.get(getAdapterPosition()));
                }
            });
            detailsLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.detailsTrip(v,mList.get(getAdapterPosition()),mIdList.get(getAdapterPosition()));
                }
            });
            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.deleteTrip(v,mIdList.get(getAdapterPosition()));
                }
            });

        }
    }


    public interface AdapterListener {
        void detailsTrip(View v, TripModel model, String  id);

        void editTrip(View v, TripModel model, String  id);

        void deleteTrip(View v, String  id);

    }


}
