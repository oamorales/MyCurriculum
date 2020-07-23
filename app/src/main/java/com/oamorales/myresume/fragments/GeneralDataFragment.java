package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.oamorales.myresume.R;

public class GeneralDataFragment extends Fragment {

    private MaterialTextView personName, lastDegree, currentJob, mothLanguage;
    private AppCompatImageView personImage;

    public GeneralDataFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_data, container, false);
        personImage = view.findViewById(R.id.generalPersonImage);
        personName = view.findViewById(R.id.generalPersonName);
        lastDegree = view.findViewById(R.id.generalLastDegree);
        currentJob = view.findViewById(R.id.generalCurrentJob);
        mothLanguage = view.findViewById(R.id.generalMotherLanguage);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        personImage.setImageResource(R.drawable.foto_fondo_blanco);
        personName.setText(R.string.my_name);
        lastDegree.setText(R.string.degree);
        currentJob.setText("Android Developer Freelance");
        mothLanguage.setText(R.string.language_level_5);
    }
}