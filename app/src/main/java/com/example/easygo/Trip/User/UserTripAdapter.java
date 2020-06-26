package com.example.easygo.Trip.User;

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
import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.Models.UserFlightChoiceSorting;
import com.example.easygo.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserTripAdapter extends RecyclerView.Adapter<UserTripAdapter.ViewHolder> {

    private Context context;
    private List<TripModel> mList;
    private UserTripAdapter.AdapterListener onClickListener;


    public UserTripAdapter() {
    }

    public UserTripAdapter(Context context, List<TripModel> mList, AdapterListener onClickListener) {
        this.context = context;
        this.mList = mList;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public UserTripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_trip, parent, false);
        return new UserTripAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserTripAdapter.ViewHolder holder, final int position) {

        holder.tvTitle.setText(mList.get(position).getTitle().toUpperCase());
        Picasso.get().load(mList.get(position).getImageUrl()).fit().into(holder.ivMainImg);

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
        private TextView tvTitle;
        private ImageView ivMainImg;
        private RelativeLayout rlDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.row_userTrip_title);
            ivMainImg = itemView.findViewById(R.id.row_userTrip_img);
            rlDetails = itemView.findViewById(R.id.row_userTrip_card);

            rlDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onClickListener.details(v,mList.get(getAdapterPosition()));
                }
            });


        }
    }


    public interface AdapterListener {
        void details(View v, TripModel model);
    }


}
