package com.example.easygo.BasicData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easygo.R;

import java.util.List;


public class BasicDataAdapter extends RecyclerView.Adapter<BasicDataAdapter.ViewHolder> {

    private Context context;
    private List<String> mList;
    private BasicDataAdapter.AdapterListener onClickListener;


    public BasicDataAdapter() {
    }

    public BasicDataAdapter(Context context, List<String> mList, AdapterListener onClickListener) {
        this.context = context;
        this.mList = mList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public BasicDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_basic_data, parent, false);
        return new BasicDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BasicDataAdapter.ViewHolder holder, final int position) {

        holder.tvTitle.setText(mList.get(position));

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

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.row_basicData_title);


            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onClickListener.basicData(v,mList.get(getAdapterPosition()));
                }
            });


        }
    }


    public interface AdapterListener {
        void basicData(View v, String data);
    }


}
