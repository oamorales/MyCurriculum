package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentNewWorkExpBinding;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.utils.DBManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewWorkExpFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private FragmentNewWorkExpBinding binding;
    private List<Integer> years = new ArrayList<>();
    private int startYear, endYear;

    public NewWorkExpFragment() { /** Required empty public constructor */  }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewWorkExpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        int year = 2020;
        for (int i = 0; i<15; i++){
            years.add(year);
            year--;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_years, years);
        binding.newWorkExpStartYear.setAdapter(adapter);
        binding.newWorkExpEndYear.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    /** Listeners events configs */
    private void setListeners() {
        binding.saveBtn.setOnClickListener(this);
        binding.newCompanyName.setOnEditorActionListener(this);
        binding.newPosition.setOnEditorActionListener(this);
        binding.newPositionDescription.setOnEditorActionListener(this);
        binding.newWorkExpStartYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.newWorkExpStartYear.setError(null);
                binding.newWorkExpStartYear.setText(String.valueOf(years.get(position)));
            }
        });
        binding.newWorkExpEndYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.newWorkExpEndYear.setError(null);
                binding.newWorkExpEndYear.setText(String.valueOf(years.get(position)));
            }
        });
    }

    /** Button save event */
    @Override
    public void onClick(View v) {
        String companyName = Objects.requireNonNull(binding.newCompanyName.getText()).toString();
        String position = Objects.requireNonNull(binding.newPosition.getText()).toString();
        String positionDesc = Objects.requireNonNull(binding.newPositionDescription.getText()).toString();
        String startY = binding.newWorkExpStartYear.getText().toString();
        String endY = binding.newWorkExpEndYear.getText().toString();
        if (fieldsAreValid(companyName, position, positionDesc, startY, endY)){
            WorkExp newWorkExp = new WorkExp(companyName, position, positionDesc, startYear, endYear);
            DBManager.insert(newWorkExp, requireContext());
            Toast.makeText(getContext(), "Working Exp Saved", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }

    private boolean fieldsAreValid(String value1, String value2, String value3, String value4, String value5){
        /** Se valida que no hay campos vacíos */
        if (!value1.isEmpty() && !value2.isEmpty() && !value3.isEmpty()
                && isNumeric(value4) && isNumeric(value5)){
            startYear = Integer.parseInt(value4);
            endYear = Integer.parseInt(value5);
            if (endYear >= startYear && startYear >= years.get(0)-years.size() && startYear <= years.get(0)
                    && endYear >= years.get(0)-years.size() && endYear <= years.get(0)){
                return true;
            }else{
                binding.newWorkExpStartYear.setError("invalid");
                binding.newWorkExpEndYear.setError("invalid");
                return false;
            }
        }else{
            binding.newCompanyName.setError(value1.isEmpty() ? "required" : null);
            binding.newPosition.setError(value2.isEmpty() ? "required" : null);
            binding.newPositionDescription.setError(value3.isEmpty() ? "required" : null);
            binding.newWorkExpStartYear.setError((isNumeric(value4)) ? null : "required");
            binding.newWorkExpEndYear.setError((isNumeric(value5)) ? null : "required");
            return false;
        }
    }

    private boolean isNumeric(String string){
        try {
            Integer.parseInt(string);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /** Delete error icon by typing */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        v.setError(null);
        return true;
    }
}