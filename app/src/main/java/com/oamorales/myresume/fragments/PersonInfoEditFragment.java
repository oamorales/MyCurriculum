package com.oamorales.myresume.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentPersonInfoEditBinding;
import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.viewmodels.PersonInfoViewModel;

import org.jetbrains.annotations.NotNull;


public class PersonInfoEditFragment extends Fragment implements View.OnClickListener {

    private FragmentPersonInfoEditBinding binding;
    private PersonInfoViewModel viewModel;

    public PersonInfoEditFragment() {    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Enable toolbar */
        setHasOptionsMenu(true);
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
        binding = FragmentPersonInfoEditBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PersonInfoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.init();
        viewModel.getPersonInfo().observe(getViewLifecycleOwner(), new Observer<PersonInfo>() {
            @Override
            public void onChanged(PersonInfo personInfo) {
                setUI(personInfo);
            }
        });

        binding.editPersonBirth.setOnClickListener(this);
        binding.personSaveBtn.setOnClickListener(this);
    }

    private void setUI(PersonInfo personInfo) {
        if (personInfo != null){
            binding.editPersonName.setText(personInfo.getName());
            binding.editPersonBirth.setText(personInfo.getBirth());
            binding.editPersonEmail.setText(personInfo.getEmail());
            binding.editPersonPhone.setText(personInfo.getPhone());
            binding.editPersonAddress.setText(personInfo.getAddress());
            binding.editPersonFacebook.setText(personInfo.getFacebook());
            binding.editPersonTwitter.setText(personInfo.getTwitter());
            binding.editPersonInstagram.setText(personInfo.getInstagram());
            binding.editPersonSkype.setText(personInfo.getSkype());
        }
    }

    /** Save btn event */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editPersonBirth:
                showDatePickerDialog();
                return;
            case R.id.personSaveBtn:
                PersonInfo personInfo =  createPerson();
                if (personInfo == null)
                    return;
                viewModel.savePersonInfo(personInfo);
                requireActivity().onBackPressed();
        }
    }

    /** Show calendar */
    private void showDatePickerDialog() {
        String tag = "datePicker";
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = twoDigits(dayOfMonth) + "/" + twoDigits(month+1) + "/" + year;
                binding.editPersonBirth.setError(null);
                binding.editPersonBirth.setText(selectedDate);
            }
        });
        newFragment.show(requireActivity().getSupportFragmentManager(), tag);
    }

    private String twoDigits(int n){
        return (n<=9) ? "0"+n : String.valueOf(n);
    }

    private PersonInfo createPerson(){
        String name = binding.editPersonName.getText().toString();
        String birth = binding.editPersonBirth.getText().toString();
        String email = binding.editPersonEmail.getText().toString();
        String phone = binding.editPersonPhone.getText().toString();
        String address = binding.editPersonAddress.getText().toString();
        String facebook = binding.editPersonFacebook.getText().toString();
        String twitter = binding.editPersonTwitter.getText().toString();
        String instagram = binding.editPersonInstagram.getText().toString();
        String skype = binding.editPersonSkype.getText().toString();

        if (name.isEmpty()){
            binding.editPersonName.setError("required");
            return null;
        }

        if (birth.isEmpty()){
            binding.editPersonBirth.setError("required");
            return null;
        }

        if (email.isEmpty()){
            binding.editPersonEmail.setError("required");
            return null;
        }else if (!isValidEmail(email)){
            binding.editPersonEmail.setError("invalid format");
            return null;
        }

        if (phone.isEmpty()){
            binding.editPersonPhone.setError("required");
            return null;
        }else if (!isNumeric(phone)){
            binding.editPersonPhone.setError("numeric field");
            return null;
        }else if (phone.length() < 11){
            binding.editPersonPhone.setError("number missing");
            return null;
        }

        if (address.isEmpty()){
            binding.editPersonAddress.setError("required");
            return null;
        }

        return new PersonInfo(name, birth,email,phone,address,facebook,twitter,instagram,skype);
    }

    private boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isNumeric(String string){
        try {
            Long.valueOf(string);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}