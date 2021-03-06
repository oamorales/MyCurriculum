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
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.oamorales.myresume.R;
import com.oamorales.myresume.viewmodels.DegreesViewModel;
import com.oamorales.myresume.databinding.FragmentNewDegreeBinding;
import com.oamorales.myresume.models.Degree;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class NewDegreeFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private DegreesViewModel viewModel;
    private FragmentNewDegreeBinding binding;
    private Calendar begin, end, currentDate;
    private String required;
    private String invalid;
    private int yearB, yearE;

    public NewDegreeFragment() {
        // Required empty public constructor
    }

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
        binding = FragmentNewDegreeBinding.inflate(inflater, container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(DegreesViewModel.class);
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
    private void setListeners(){
        binding.newDegreeSaveBtn.setOnClickListener(this);
        binding.newDegreeStartDate.setOnClickListener(this);
        binding.newDegreeEndDate.setOnClickListener(this);
        binding.newDegreeDiscipline.setOnEditorActionListener(this);
        binding.newDegreeTitle.setOnEditorActionListener(this);
        binding.newDegreeUniversity.setOnEditorActionListener(this);
        binding.newDegreeGradeAverage.setOnEditorActionListener(this);
    }

    /** Evento botón guardar */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newDegreeSaveBtn:
                String tittle = Objects.requireNonNull(binding.newDegreeTitle.getText()).toString();
                String university = Objects.requireNonNull(binding.newDegreeUniversity.getText()).toString();
                String discipline = Objects.requireNonNull(binding.newDegreeDiscipline.getText()).toString();
                String gradeAverage = Objects.requireNonNull(binding.newDegreeGradeAverage.getText()).toString();
                String startDate = Objects.requireNonNull(binding.newDegreeStartDate.getText()).toString();
                String endDate = Objects.requireNonNull(binding.newDegreeEndDate.getText()).toString();
                if (fieldsAreValid(tittle, university, discipline, gradeAverage, startDate, endDate)){
                    //Se guardan los datos si no hay campos vacíos
                    Degree newDegree = new Degree(tittle, university, discipline, startDate, endDate, yearB, yearE, Float.parseFloat(gradeAverage));
                    //Se guarda el nuevo degree
                    viewModel.createDegree(newDegree, requireContext());
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
                break;
            case R.id.newDegreeStartDate:
                showDatePickerDialog(binding.newDegreeStartDate, true);
                break;
            case R.id.newDegreeEndDate:
                showDatePickerDialog(binding.newDegreeEndDate, false);
        }
    }

    /** Validate fields */
    private boolean fieldsAreValid(String value1, String value2, String value3, String value4, String value5, String value6){
        /** Se valida que no hay campos vacíos */
        if (!value1.isEmpty() && !value2.isEmpty() && !value3.isEmpty()
                && !value4.isEmpty() && !value5.isEmpty() && !value6.isEmpty()){
            if (begin.compareTo(end) <=0 && begin.compareTo(currentDate) <= 0 && end.compareTo(currentDate) <= 0){
                return true;
            }else{
                binding.newDegreeStartDate.setError(invalid);
                binding.newDegreeEndDate.setError(invalid);
                return false;
            }
        }else{
            binding.newDegreeTitle.setError(value1.isEmpty() ? required : null);
            binding.newDegreeUniversity.setError(value2.isEmpty() ? required : null);
            binding.newDegreeDiscipline.setError(value3.isEmpty() ? required : null);
            binding.newDegreeGradeAverage.setError(value4.isEmpty() ? required : null);
            binding.newDegreeStartDate.setError(value5.isEmpty() ? required : null);
            binding.newDegreeEndDate.setError(value6.isEmpty() ? required : null);
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
        InputMethodManager im = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(v.getWindowToken(),0);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}