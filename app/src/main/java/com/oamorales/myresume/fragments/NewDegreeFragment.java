package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.oamorales.myresume.R;
import com.oamorales.myresume.models.Degree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NewDegreeFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private Realm realm;
    private TextInputEditText newDegreeTittle, newDegreeUniversity,
            newDegreeDiscipline, newDegreeAverage ;
    private AutoCompleteTextView newDegreeYearEnd, newDegreeYearBegin;
    private MaterialButton saveBtn;
    private AppCompatImageView newDegreeLogo;
    private int yearB, yearE;

    private List<Integer> years = new ArrayList<>();
    private final int DEFAULT_VALUE = -8;


    public NewDegreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_degree, container, false);
        setID(view);
        int year = 2020;
        for (int i = 0; i<15; i++){
            years.add(year);
            year--;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), R.layout.degrees_list_years, years);
        newDegreeYearBegin.setAdapter(adapter);
        newDegreeYearEnd.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        saveBtn.setOnClickListener(this);
        newDegreeDiscipline.setOnEditorActionListener(this);
        newDegreeTittle.setOnEditorActionListener(this);
        newDegreeUniversity.setOnEditorActionListener(this);
        newDegreeAverage.setOnEditorActionListener(this);

        newDegreeYearBegin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newDegreeYearBegin.setError(null);
                yearB = years.get(position);
            }
        });

        newDegreeYearEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.getId();
                newDegreeYearEnd.setError(null);
                yearE = years.get(position);
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void setID(View view){
        newDegreeLogo = view.findViewById(R.id.newDegreeLogo);
        newDegreeTittle = view.findViewById(R.id.newDegreeTitle);
        newDegreeUniversity = view.findViewById(R.id.newDegreeUniversity);
        newDegreeDiscipline = view.findViewById(R.id.newDegreeDiscipline);
        newDegreeYearBegin = view.findViewById(R.id.newDegreeYearBegin);
        newDegreeYearEnd = view.findViewById(R.id.newDegreeYearEnd);
        newDegreeAverage = view.findViewById(R.id.newDegreeGradeAverage);
        saveBtn = view.findViewById(R.id.saveBtn);

    }

    /** Evento botón guardar */
    @Override
    public void onClick(View v) {
        String tittle = Objects.requireNonNull(newDegreeTittle.getText()).toString();
        String university = Objects.requireNonNull(newDegreeUniversity.getText()).toString();
        String discipline = Objects.requireNonNull(newDegreeDiscipline.getText()).toString();
        String gradeAverage = Objects.requireNonNull(newDegreeAverage.getText()).toString();


        if (!tittle.isEmpty() && !university.isEmpty() && !discipline.isEmpty()
                && !gradeAverage.isEmpty() && yearB!=0 && yearE!=0){
            if (yearE >= yearB){
                Degree newDegree = new Degree(R.drawable.usb_logo,tittle, university, discipline, yearB, 2019, Double.parseDouble(gradeAverage));
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(newDegree);
                realm.commitTransaction();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }else{
                newDegreeYearBegin.setError("invalid");
                newDegreeYearEnd.setError("invalid");
            }

        }else{
            newDegreeTittle.setError(tittle.isEmpty() ? "required" : null);
            newDegreeUniversity.setError(university.isEmpty() ? "required" : null);
            newDegreeDiscipline.setError(discipline.isEmpty() ? "required" : null);
            newDegreeAverage.setError(gradeAverage.isEmpty() ? "required" : null);
            newDegreeYearBegin.setError((yearB == 0) ? "required" : null);
            newDegreeYearEnd.setError((yearE == 0) ? "required" : null);

        }


    }

    /** Eliminar icono de error al escribir en los EditText */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        v.setError(null);
        return true;
    }


}