package com.example.easygo.BasicData.Programs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easygo.Models.DbModels.CitiesModel;
import com.example.easygo.R;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    private Context context;
    private List<CitiesModel> mList;

    private ProgramAdapter.AdapterListener onClickListener;


    public ProgramAdapter() {
    }

    public ProgramAdapter(Context context, List<CitiesModel> mList, AdapterListener onClickListener) {
        this.context = context;
        this.mList = mList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trip, parent, false);
        return new ProgramAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProgramAdapter.ViewHolder holder, final int position) {

        holder.titleTV.setText(mList.get(position).getName());
        holder.priceTV.setVisibility(View.GONE);

        holder.logoIV.setVisibility(View.GONE);
        holder.editIV.setVisibility(View.GONE);
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
        private ImageView editIV,deleteIV,logoIV;
        private LinearLayout detailsLinear;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.row_title);
            priceTV = itemView.findViewById(R.id.row_price);
            editIV = itemView.findViewById(R.id.row_edit);
            logoIV = itemView.findViewById(R.id.row_logo);

            deleteIV = itemView.findViewById(R.id.row_delete);
            detailsLinear = itemView.findViewById(R.id.row_getDetails);

            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.editTrip(v,mList.get(getAdapterPosition()),mList.get(getAdapterPosition()).getId());
                }
            });
            detailsLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.detailsTrip(v,mList.get(getAdapterPosition()),mList.get(getAdapterPosition()).getId());
                }
            });
            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.deleteTrip(v,mList.get(getAdapterPosition()).getId());
                }
            });

        }
    }


    public interface AdapterListener {
        void detailsTrip(View v, CitiesModel model, String id);

        void editTrip(View v, CitiesModel model, String id);

        void deleteTrip(View v, String id);

    }


    private byte[] decodeImageBm(String encodedImage){
        String cleanImage = encodedImage.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
        byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

}
