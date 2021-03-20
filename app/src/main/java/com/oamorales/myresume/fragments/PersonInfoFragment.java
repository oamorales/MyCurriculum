package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.databinding.FragmentPersonInfoBinding;
import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.viewmodels.PersonInfoViewModel;

import org.jetbrains.annotations.NotNull;

public class PersonInfoFragment extends Fragment {

    private FragmentPersonInfoBinding binding;

    public PersonInfoFragment() { }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PersonInfoViewModel viewModel = new ViewModelProvider(requireActivity()).get(PersonInfoViewModel.class);
        viewModel.init();
        viewModel.getPersonInfo().observe(getViewLifecycleOwner(), new Observer<PersonInfo>() {
            @Override
            public void onChanged(PersonInfo personInfo) {
                setUI(personInfo);
            }
        });
        NavDirections directions = PersonInfoFragmentDirections.actionPersonInfoFragmentToPersonInfoEditFragment();
        binding.personInfoFAB.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    private void setUI(PersonInfo personInfo) {
        if (personInfo != null){
            binding.personNameTextView.setText(personInfo.getName());
            binding.addressTextView.setText(personInfo.getAddress());
            binding.birthDateTextView.setText(personInfo.getBirth());
            binding.emailTextView.setText(personInfo.getEmail());
            binding.phoneTextView.setText(personInfo.getPhone());
            binding.facebookTextView.setText(personInfo.getFacebook());
            binding.twitterTextView.setText(personInfo.getTwitter());
            binding.instagramTextView.setText(personInfo.getInstagram());
            binding.skypeTextView.setText(personInfo.getSkype());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
