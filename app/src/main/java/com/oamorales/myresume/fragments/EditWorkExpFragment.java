package com.oamorales.myresume.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.oamorales.myresume.databinding.FragmentEditWorkExpBinding;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.viewmodels.WorkExpViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;


public class EditWorkExpFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private FragmentEditWorkExpBinding binding;
    private WorkExpViewModel viewModel;
    private String required;
    private String invalid;
    private int id, startYear, endYear;
    private Calendar begin, end, currentDate;

    public EditWorkExpFragment() {/** Required empty public constructor */}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Enable toolbar */
        setHasOptionsMenu(true);
        /** Get id sent */
        EditWorkExpFragmentArgs args = EditWorkExpFragmentArgs.fromBundle(requireArguments());
        id = args.getId();
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
        binding = FragmentEditWorkExpBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(WorkExpViewModel.class);
        required = getString(R.string.required);
        invalid = getString(R.string.invalid);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getWorkExp(id).observe(getViewLifecycleOwner(), new Observer<WorkExp>() {
            @Override
            public void onChanged(WorkExp workExp) {
                setContentUI(workExp);
                getDatesToCompare(workExp);
            }
        });
        setListeners();
    }

    /***/
    private void setContentUI(WorkExp workExp){
        binding.editPosition.setText(workExp.getPosition());
        binding.editCompanyName.setText(workExp.getCompanyName());
        binding.editPositionDescription.setText(workExp.getPositionDesc());
        binding.editWorkExpStartYear.setText(workExp.getStartDate());
        binding.editWorkExpEndYear.setText(workExp.getEndDate());
        startYear = workExp.getStartYear();
        endYear = workExp.getEndYear();
    }

    private void getDatesToCompare(WorkExp workExp) {
        int startYear = Integer.parseInt(workExp.getStartDate().substring(6));
        int startMonth = Integer.parseInt(workExp.getStartDate().substring(3,5));
        int startDay = Integer.parseInt(workExp.getStartDate().substring(0,2));
        begin.set(startYear, startMonth-1, startDay);
        int endYear = Integer.parseInt(workExp.getEndDate().substring(6));
        int endMonth = Integer.parseInt(workExp.getEndDate().substring(3,5));
        int endDay = Integer.parseInt(workExp.getEndDate().substring(0,2));
        end.set(endYear, endMonth-1, endDay);
    }

    /** Listeners events configs */
    private void setListeners() {
        binding.saveBtn.setOnClickListener(this);
        binding.editWorkExpStartYear.setOnClickListener(this);
        binding.editWorkExpEndYear.setOnClickListener(this);
        binding.editCompanyName.setOnEditorActionListener(this);
        binding.editPosition.setOnEditorActionListener(this);
        binding.editPositionDescription.setOnEditorActionListener(this);
    }

    /** Save button and dates event */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                String companyName = Objects.requireNonNull(binding.editCompanyName.getText()).toString();
                String position = Objects.requireNonNull(binding.editPosition.getText()).toString();
                String positionDesc = Objects.requireNonNull(binding.editPositionDescription.getText()).toString();
                String startDate = binding.editWorkExpStartYear.getText().toString();
                String endDate = binding.editWorkExpEndYear.getText().toString();
                if (fieldsAreValid(companyName, position, positionDesc, startDate, endDate)){
                    WorkExp newWorkExp = new WorkExp(companyName, position, positionDesc, startDate, endDate, startYear, endYear);
                    viewModel.updateWorkExp(newWorkExp, id);
                    Toast.makeText(getContext(), getText(R.string.work_exp_saved), Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
                break;
            case R.id.editWorkExpStartYear:
                showDatePickerDialog(binding.editWorkExpStartYear, true);
                break;
            case R.id.editWorkExpEndYear:
                showDatePickerDialog(binding.editWorkExpEndYear, false);
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
                binding.editWorkExpStartYear.setError(invalid);
                binding.editWorkExpEndYear.setError(invalid);
                return false;
            }
        }else{
            binding.editCompanyName.setError(value1.isEmpty() ? required : null);
            binding.editPosition.setError(value2.isEmpty() ? required : null);
            binding.editPositionDescription.setError(value3.isEmpty() ? required : null);
            binding.editWorkExpStartYear.setError(value4.isEmpty() ? required : null);
            binding.editWorkExpEndYear.setError(value5.isEmpty() ? required : null);
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
                /** Store calendar values */
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

    /** Remove error icon */
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