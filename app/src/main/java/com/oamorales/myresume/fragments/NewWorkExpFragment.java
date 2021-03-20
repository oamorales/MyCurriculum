package com.oamorales.myresume.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentNewWorkExpBinding;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.viewmodels.WorkExpViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class NewWorkExpFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private FragmentNewWorkExpBinding binding;
    private WorkExpViewModel viewModel;
    private String required, invalid;
    private int startYear, endYear;
    private Calendar begin, end, currentDate;

    public NewWorkExpFragment() { /** Required empty public constructor */  }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Enable toolbar */
        setHasOptionsMenu(true);
        begin = Calendar.getInstance();
        end = Calendar.getInstance();
        currentDate = Calendar.getInstance();
    }

    /** Handle toolbar menu */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        /** Disable menu button */
        menu.findItem(R.id.generatePdf).setVisible(false);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewWorkExpBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(WorkExpViewModel.class);
        required = getString(R.string.required);
        invalid = getString(R.string.invalid);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    /** Listeners events configs */
    private void setListeners() {
        binding.saveBtn.setOnClickListener(this);
        binding.newWorkExpStartYear.setOnClickListener(this);
        binding.newWorkExpEndYear.setOnClickListener(this);
        binding.newCompanyName.setOnEditorActionListener(this);
        binding.newPosition.setOnEditorActionListener(this);
        binding.newPositionDescription.setOnEditorActionListener(this);
    }

    /** Button save event */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                String companyName = Objects.requireNonNull(binding.newCompanyName.getText()).toString();
                String position = Objects.requireNonNull(binding.newPosition.getText()).toString();
                String positionDesc = Objects.requireNonNull(binding.newPositionDescription.getText()).toString();
                String startDate = binding.newWorkExpStartYear.getText().toString();
                String endDate = binding.newWorkExpEndYear.getText().toString();
                if (fieldsAreValid(companyName, position, positionDesc, startDate, endDate)){
                    WorkExp newWorkExp = new WorkExp(companyName, position, positionDesc, startDate, endDate, startYear, endYear);
                    viewModel.createWorkExp(newWorkExp);
                    Toast.makeText(getContext(), getText(R.string.work_exp_saved), Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
                break;
            case R.id.newWorkExpStartYear:
                showDatePickerDialog(binding.newWorkExpStartYear, true);
                break;
            case R.id.newWorkExpEndYear:
                showDatePickerDialog(binding.newWorkExpEndYear, false);
        }

    }

    private boolean fieldsAreValid(String value1, String value2, String value3, String value4, String value5){
        /** Se valida que no hay campos vacíos */
        if (!value1.isEmpty() && !value2.isEmpty() && !value3.isEmpty()
                && !value4.isEmpty() && !value5.isEmpty()){
            /** Compare dates */
            if (begin.compareTo(end) <=0 && begin.compareTo(currentDate) <= 0 && end.compareTo(currentDate) <= 0){
                return true;
            }else{
                binding.newWorkExpStartYear.setError(invalid);
                binding.newWorkExpEndYear.setError(invalid);
                return false;
            }
        }else{
            binding.newCompanyName.setError(value1.isEmpty() ? required : null);
            binding.newPosition.setError(value2.isEmpty() ? required : null);
            binding.newPositionDescription.setError(value3.isEmpty() ? required : null);
            binding.newWorkExpStartYear.setError(value4.isEmpty() ? required : null);
            binding.newWorkExpEndYear.setError(value5.isEmpty() ? required : null);
            return false;
        }
    }


    /** Show calendar */
    private void showDatePickerDialog(final TextInputEditText editText, final boolean start) {
        String tag = "datePicker";
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = twoDigits(dayOfMonth) + "/" + twoDigits(month+1) + "/" + year;
                if (start){
                    startYear = year;
                    begin.set(year, month, dayOfMonth);
                }else {
                    endYear = year;
                    end.set(year, month, dayOfMonth);
                }
                editText.setError(null);
                editText.setText(selectedDate);
            }
        });
        newFragment.show(requireActivity().getSupportFragmentManager(), tag);
    }

    private String twoDigits(int n){
        return (n<=9) ? "0"+n : String.valueOf(n);
    }

    /** Delete error icon by typing */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        v.setError(null);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}