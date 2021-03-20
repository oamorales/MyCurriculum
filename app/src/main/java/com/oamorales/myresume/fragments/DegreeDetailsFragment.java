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
import com.oamorales.myresume.databinding.FragmentDegreeDetailsBinding;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.viewmodels.DegreesViewModel;

import org.jetbrains.annotations.NotNull;

public class DegreeDetailsFragment extends Fragment {

    private FragmentDegreeDetailsBinding binding;
    private DegreesViewModel viewModel;
    private int id;

    public DegreeDetailsFragment() { /** Required */  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Enable toolbar */
        setHasOptionsMenu(true);
        /** Se recuperan los valores suministrados por el fragment anterior */
        DegreeDetailsFragmentArgs args = DegreeDetailsFragmentArgs.fromBundle(requireArguments());
        id = args.getDegreeId();
        viewModel = new ViewModelProvider(requireActivity()).get(DegreesViewModel.class);
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
        binding = FragmentDegreeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /**FAB onClick event*/
        DegreeDetailsFragmentDirections.ActionDegreeDetailsFragmentToEditDegreeFragment directions = DegreeDetailsFragmentDirections
                .actionDegreeDetailsFragmentToEditDegreeFragment(id);
        binding.fabEditDegree.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
        /***/
        viewModel.getDegree(id).observe(getViewLifecycleOwner(), new Observer<Degree>() {
            @Override
            public void onChanged(Degree degree) {
                setContentUI(degree);
            }
        });
    }

    private void setContentUI(Degree degree){
        binding.degreeTitle.setText(degree.getDegreeTittle());
        binding.degreeDetailUnivName.setText(degree.getUniversity());
        binding.degreeDiscipline.setText(degree.getDiscipline());
        binding.degreeStartDate.setText(degree.getStartDate());
        binding.degreeEndDate.setText(degree.getEndDate());
        binding.degreeGradeAverage.setText(String.valueOf(degree.getGradeAverage()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}