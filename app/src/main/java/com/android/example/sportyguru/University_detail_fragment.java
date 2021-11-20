package com.android.example.sportyguru;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class University_detail_fragment extends Fragment {

    private ImageView universityImage;
    private TextView universityName, universityAddress, universityWebSite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_university_detail_fragment, container, false);
        University university = (University) getArguments().getSerializable("key");
        init(view);
        universityName.setText(university.getUniversity_Name());
        universityAddress.setText(university.getUniversity_Address());
        universityWebSite.setText(university.getUniversity_Web_Page());
        universityWebSite.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(university.getUniversity_Web_Page()));
            startActivity(i);
        });
        return view;
    }

    public void init(View view) {
        universityImage = view.findViewById(R.id.university_image);
        universityName = view.findViewById(R.id.university_name);
        universityAddress = view.findViewById(R.id.university_address);
        universityWebSite = view.findViewById(R.id.website);



    }
}