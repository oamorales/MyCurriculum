package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentWorkExpDetailsBinding;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.utils.DBManager;

import io.realm.Realm;


public class WorkExpDetailsFragment extends Fragment {

    private FragmentWorkExpDetailsBinding binding;
    private WorkExpDetailsFragmentArgs args;
    private int id;
    private Realm realm;

    public WorkExpDetailsFragment() { /** Required empty public constructor */ }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = WorkExpDetailsFragmentArgs.fromBundle(requireArguments());
        id = args.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        binding = FragmentWorkExpDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WorkExpDetailsFragmentDirections.ActionWorkExpDetailsFragmentToEditWorkExpFragment directions =
                WorkExpDetailsFragmentDirections.actionWorkExpDetailsFragmentToEditWorkExpFragment(id);
        binding.fabEditWorkExp.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    @Override
    public void onResume() {
        super.onResume();
        WorkExp workExp = (WorkExp) DBManager.getObjectById(WorkExp.class, id);
        setContentUI(workExp);
    }

    private void setContentUI(WorkExp workExp) {
        binding.position.setText(workExp.getPosition());
        binding.companyName.setText(workExp.getCompanyName());
        binding.positionDesc.setText(workExp.getPositionDesc());
        binding.startYear.setText(String.valueOf(workExp.getStartYear()));
        binding.endYear.setText(String.valueOf(workExp.getEndYear()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}