package com.example.task;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        holder.titleTV.setText(mList.get(position).getTrip_title());
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
        private View v;

        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            titleTV = itemView.findViewById(R.id.row_title);
            priceTV = itemView.findViewById(R.id.row_price);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.editTrip(v,mList.get(getAdapterPosition()),mIdList.get(getAdapterPosition()));
                }
            });
        }
    }


    public interface AdapterListener {

        void editTrip(View v, TripModel model, String  id);


    }


}
