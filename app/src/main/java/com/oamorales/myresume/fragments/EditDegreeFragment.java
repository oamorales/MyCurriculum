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
import com.oamorales.myresume.viewmodels.DegreesViewModel;
import com.oamorales.myresume.databinding.FragmentEditDegreeBinding;
import com.oamorales.myresume.models.Degree;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class EditDegreeFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private DegreesViewModel viewModel;
    private FragmentEditDegreeBinding binding;
    private Calendar begin, end, currentDate;
    private String required;
    private String invalid;
    private int yearB, yearE, id;

    public EditDegreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Enable toolbar */
        setHasOptionsMenu(true);
        /** Recuperar ID */
        EditDegreeFragmentArgs args = EditDegreeFragmentArgs.fromBundle(requireArguments());
        id = args.getDegreeId();
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
        binding = FragmentEditDegreeBinding.inflate(inflater, container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(DegreesViewModel.class);
        required = getString(R.string.required);
        invalid = getString(R.string.invalid);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getDegree(id).observe(getViewLifecycleOwner(), new Observer<Degree>() {
            @Override
            public void onChanged(Degree degree) {
                setContentUI(degree);
                getDatesToCompare(degree);
            }
        });
        setListeners();
    }

    /** Set UI content */
    private void setContentUI(Degree degree){
        binding.editDegreeTitle.setText(degree.getDegreeTittle());
        binding.editDegreeUniversity.setText(degree.getUniversity());
        binding.editDegreeDiscipline.setText(degree.getDiscipline());
        yearB = degree.getYearBegin();
        binding.editDegreeStartDate.setText(degree.getStartDate());
        yearE = degree.getYearEnd();
        binding.editDegreeEndDate.setText(degree.getEndDate());
        binding.editDegreeGradeAverage.setText(String.valueOf(degree.getGradeAverage()));
    }

    private void getDatesToCompare(Degree degree){
        int startYear = Integer.parseInt(degree.getStartDate().substring(6));
        int startMonth = Integer.parseInt(degree.getStartDate().substring(3,5));
        int startDay = Integer.parseInt(degree.getStartDate().substring(0,2));
        begin.set(startYear, startMonth-1, startDay);
        int endYear = Integer.parseInt(degree.getEndDate().substring(6));
        int endMonth = Integer.parseInt(degree.getEndDate().substring(3,5));
        int endDay = Integer.parseInt(degree.getEndDate().substring(0,2));
        end.set(endYear, endMonth-1, endDay);
    }

    /** Listeners events configs */
    private void setListeners(){
        binding.editDegreeDiscipline.setOnEditorActionListener(this);
        binding.editDegreeTitle.setOnEditorActionListener(this);
        binding.editDegreeUniversity.setOnEditorActionListener(this);
        binding.editDegreeGradeAverage.setOnEditorActionListener(this);
        binding.editDegreeEndDate.setOnClickListener(this);
        binding.editDegreeStartDate.setOnClickListener(this);
        binding.editDegreeSaveBtn.setOnClickListener(this);
    }

    /** Save button and dates event */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editDegreeSaveBtn:
                String tittle = Objects.requireNonNull(binding.editDegreeTitle.getText()).toString();
                String university = Objects.requireNonNull(binding.editDegreeUniversity.getText()).toString();
                String discipline = Objects.requireNonNull(binding.editDegreeDiscipline.getText()).toString();
                String gradeAverage = Objects.requireNonNull(binding.editDegreeGradeAverage.getText()).toString();
                String startDate = Objects.requireNonNull(binding.editDegreeStartDate.getText()).toString();
                String endDate = Objects.requireNonNull(binding.editDegreeEndDate.getText()).toString();
                if (fieldsAreValid(tittle, university, discipline, gradeAverage, startDate, endDate)){
                    //Se guardan los datos si no hay campos vacíos
                    Degree newDegree = new Degree(tittle, university, discipline, startDate, endDate, yearB, yearE, Float.parseFloat(gradeAverage));
                    //Se guarda el nuevo degree
                    viewModel.updateDegree(newDegree, id, requireContext());
                    Toast.makeText(getContext(), getText(R.string.saved), Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
                break;
            case R.id.editDegreeStartDate:
                Toast.makeText(requireContext(), "START DATE", Toast.LENGTH_SHORT).show();
                showDatePickerDialog(binding.editDegreeStartDate, true);
            case R.id.editDegreeEndDate:
                Toast.makeText(requireContext(), "END DATE", Toast.LENGTH_SHORT).show();
                showDatePickerDialog(binding.editDegreeEndDate, false);
        }

    }

    private boolean fieldsAreValid(String value1, String value2, String value3, String value4, String value5, String value6){
        /** Se valida que no hay campos vacíos */
        if (!value1.isEmpty() && !value2.isEmpty() && !value3.isEmpty()
                && !value4.isEmpty() && !value5.isEmpty() && !value6.isEmpty()){
            if (begin.compareTo(end) <=0 && begin.compareTo(currentDate) <= 0 && end.compareTo(currentDate) <= 0){
                return true;
            }else{
                binding.editDegreeStartDate.setError(invalid);
                binding.editDegreeEndDate.setError(invalid);
                return false;
            }
        }else{
            binding.editDegreeTitle.setError(value1.isEmpty() ? required : null);
            binding.editDegreeUniversity.setError(value2.isEmpty() ? required : null);
            binding.editDegreeDiscipline.setError(value3.isEmpty() ? required : null);
            binding.editDegreeGradeAverage.setError(value4.isEmpty() ? required : null);
            binding.editDegreeStartDate.setError(value5.isEmpty() ? required : null);
            binding.editDegreeEndDate.setError(value6.isEmpty() ? required : null);
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
                    yearB = year;
                    begin.set(year, month, dayOfMonth);
                }else {
                    yearE = year;
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

    /** Eliminar icono de error al escribir en los EditText */
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