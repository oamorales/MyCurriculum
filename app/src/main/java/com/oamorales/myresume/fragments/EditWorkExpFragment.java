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
import com.oamorales.myresume.databinding.FragmentEditWorkExpBinding;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.utils.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;


public class EditWorkExpFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private FragmentEditWorkExpBinding binding;
    private List<Integer> years = new ArrayList<>();
    private Realm realm;
    private int startYear, endYear, id;

    public EditWorkExpFragment() {/** Required empty public constructor */}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        EditWorkExpFragmentArgs args = EditWorkExpFragmentArgs.fromBundle(requireArguments());
        id = args.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditWorkExpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        int year = 2020;
        for (int i = 0; i<15; i++){
            years.add(year);
            year--;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_years, years);
        binding.editWorkExpStartYear.setAdapter(adapter);
        binding.editWorkExpEndYear.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WorkExp workExp = (WorkExp) DBManager.getObjectById(WorkExp.class, id);
        setContentUI(workExp);
        setListeners();
    }

    /***/
    private void setContentUI(WorkExp workExp){
        binding.editPosition.setText(workExp.getPosition());
        binding.editCompanyName.setText(workExp.getCompanyName());
        binding.editPositionDescription.setText(workExp.getPositionDesc());
        binding.editWorkExpStartYear.setText(String.valueOf(workExp.getStartYear()));
        binding.editWorkExpEndYear.setText(String.valueOf(workExp.getEndYear()));
    }

    /** Listeners events configs */
    private void setListeners() {
        binding.saveBtn.setOnClickListener(this);
        binding.editCompanyName.setOnEditorActionListener(this);
        binding.editPosition.setOnEditorActionListener(this);
        binding.editPositionDescription.setOnEditorActionListener(this);
        binding.editWorkExpStartYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.editWorkExpStartYear.setError(null);
                binding.editWorkExpStartYear.setText(String.valueOf(years.get(position)));
            }
        });
        binding.editWorkExpEndYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.editWorkExpEndYear.setError(null);
                binding.editWorkExpEndYear.setText(String.valueOf(years.get(position)));
            }
        });

    }

    @Override
    public void onClick(View v) {
        String companyName = Objects.requireNonNull(binding.editCompanyName.getText()).toString();
        String position = Objects.requireNonNull(binding.editPosition.getText()).toString();
        String positionDesc = Objects.requireNonNull(binding.editPositionDescription.getText()).toString();
        String startY = binding.editWorkExpStartYear.getText().toString();
        String endY = binding.editWorkExpEndYear.getText().toString();
        if (fieldsAreValid(companyName, position, positionDesc, startY, endY)){
            WorkExp newWorkExp = new WorkExp(companyName, position, positionDesc, startYear, endYear);
            DBManager.updateWorkExp(newWorkExp, id, requireContext());
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
                binding.editWorkExpStartYear.setError("invalid");
                binding.editWorkExpEndYear.setError("invalid");
                return false;
            }
        }else{
            binding.editCompanyName.setError(value1.isEmpty() ? "required" : null);
            binding.editPosition.setError(value2.isEmpty() ? "required" : null);
            binding.editPositionDescription.setError(value3.isEmpty() ? "required" : null);
            binding.editWorkExpStartYear.setError((isNumeric(value4)) ? null : "required");
            binding.editWorkExpEndYear.setError((isNumeric(value5)) ? null : "required");
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        v.setError(null);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}