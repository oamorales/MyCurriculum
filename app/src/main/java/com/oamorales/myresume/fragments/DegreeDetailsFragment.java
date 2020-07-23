package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;

public class DegreeDetailsFragment extends Fragment {

    private DegreeDetailsFragmentArgs args;
    private AppCompatTextView degreeTittle;

    public DegreeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Se recuperan los valores suministrados por el fragment anterior */
        args = DegreeDetailsFragmentArgs.fromBundle(requireArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_degree_details, container, false);
        degreeTittle = view.findViewById(R.id.degreeTitle);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String tittle = args.getDegreeTittle();
        degreeTittle.setText(tittle);
    }
}