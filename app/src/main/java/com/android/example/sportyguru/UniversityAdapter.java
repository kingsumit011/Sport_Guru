package com.android.example.sportyguru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.ViewHolder> {
    private static final String LOG_TAG = UniversityAdapter.class.getName();
    private final OnItemCustomClickListner<University> onItemCustomClickListner;
    private Context mContext;
    private List<University> mUniversityList;

    public UniversityAdapter(Context mContext, List<University> mUniversityList, OnItemCustomClickListner<University> onItemCustomClickListner) {
        this.mContext = mContext;
        this.mUniversityList = mUniversityList;
        this.onItemCustomClickListner = onItemCustomClickListner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.university_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mUniversityList.get(position), onItemCustomClickListner);
        University university = mUniversityList.get(position);
        holder.universityName.setText(university.getUniversity_Name());
    }

    @Override
    public int getItemCount() {
        return mUniversityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView universityName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initWidget(itemView);
        }

        private void initWidget(View view) {

            universityName = view.findViewById(R.id.university_name);
        }

        public void bind(University item, OnItemCustomClickListner<University> listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.OnItemClick(item);

                }
            });
        }
    }
}
