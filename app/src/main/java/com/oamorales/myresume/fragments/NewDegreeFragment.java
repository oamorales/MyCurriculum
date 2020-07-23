package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.oamorales.myresume.R;

import java.util.Objects;

public class NewDegreeFragment extends Fragment {

    private TextInputEditText newDegreeTittle, newDegreeUniversity,
            newDegreeDiscipline, newDegreeYearBegin, newDegreeYearEnd, newDegreeAverage;
    private AppCompatImageView newDegreeLogo;

    public NewDegreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_degree, container, false);
        setID(view);
        /** Cambiar título del toolbar */
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Agregar Estudio");

        String tittle = Objects.requireNonNull(newDegreeTittle.getText()).toString();
        String university = newDegreeUniversity.getText().toString();
        String discipline = newDegreeDiscipline.getText().toString();
        String yearBegin = newDegreeYearBegin.getText().toString();

        return view;
    }

    private void setID(View view){
        newDegreeLogo = view.findViewById(R.id.newDegreeLogo);
        newDegreeTittle = view.findViewById(R.id.newDegreeTitle);
        newDegreeUniversity = view.findViewById(R.id.newDegreeUniversity);
        newDegreeDiscipline = view.findViewById(R.id.newDegreeDiscipline);
        newDegreeYearBegin = view.findViewById(R.id.newDegreeYearBegin);
        newDegreeYearEnd = view.findViewById(R.id.newDegreeYearEnd);
        newDegreeAverage = view.findViewById(R.id.newDegreeGradeAverage);
    }
}