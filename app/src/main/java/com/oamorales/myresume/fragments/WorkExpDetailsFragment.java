package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentWorkExpDetailsBinding;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.viewmodels.WorkExpViewModel;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;

public class WorkExpDetailsFragment extends Fragment {

    private FragmentWorkExpDetailsBinding binding;
    private int id;
    private Realm realm;
    private WorkExpViewModel viewModel;

    public WorkExpDetailsFragment() { /** Required empty public constructor */ }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Enable toolbar */
        setHasOptionsMenu(true);
        WorkExpDetailsFragmentArgs args = WorkExpDetailsFragmentArgs.fromBundle(requireArguments());
        id = args.getId();
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
        realm = Realm.getDefaultInstance();
        binding = FragmentWorkExpDetailsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(WorkExpViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WorkExpDetailsFragmentDirections.ActionWorkExpDetailsFragmentToEditWorkExpFragment directions =
                WorkExpDetailsFragmentDirections.actionWorkExpDetailsFragmentToEditWorkExpFragment(id);
        binding.fabEditWorkExp.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
        viewModel.getWorkExp(id).observe(getViewLifecycleOwner(), new Observer<WorkExp>() {
            @Override
            public void onChanged(WorkExp workExp) {
                setContentUI(workExp);
            }
        });
    }

    private void setContentUI(WorkExp workExp) {
        binding.position.setText(workExp.getPosition());
        binding.companyName.setText(workExp.getCompanyName());
        binding.positionDesc.setText(workExp.getPositionDesc());
        binding.startYear.setText(String.valueOf(workExp.getStartDate()));
        binding.endYear.setText(String.valueOf(workExp.getEndDate()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        binding = null;
    }
}