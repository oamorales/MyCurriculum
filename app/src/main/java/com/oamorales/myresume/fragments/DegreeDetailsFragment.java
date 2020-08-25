package com.oamorales.myresume.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.databinding.FragmentDegreeDetailsBinding;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.utils.DBManager;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DegreeDetailsFragment extends Fragment {

    private FragmentDegreeDetailsBinding binding;
    private DegreeDetailsFragmentArgs args;
    private int id;

    public DegreeDetailsFragment() { /** Required */  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Se recuperan los valores suministrados por el fragment anterior */
        args = DegreeDetailsFragmentArgs.fromBundle(requireArguments());
        id = args.getDegreeId();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDegreeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DegreeDetailsFragmentDirections.ActionDegreeDetailsFragmentToEditDegreeFragment directions = DegreeDetailsFragmentDirections
                .actionDegreeDetailsFragmentToEditDegreeFragment(id);
        binding.fabEditDegree.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    @Override
    public void onResume() {
        super.onResume();
        Degree degree = (Degree) DBManager.getObjectById(Degree.class, id);
        setContentUI(degree);
    }

    private void setContentUI(Degree degree){
        binding.degreeTitle.setText(degree.getDegreeTittle());
        Uri uri = Uri.fromFile(new File(degree.getImageLogo()));
        Picasso.get().load(uri).fit().into(binding.degreeDetailLogo);
        binding.degreeDetailUnivName.setText(degree.getUniversity());
        binding.degreeDiscipline.setText(degree.getDiscipline());
        binding.degreeBeginYear.setText((String.valueOf(degree.getYearBegin())));
        binding.degreeEndYear.setText(String.valueOf(degree.getYearEnd()));
        binding.degreeGradeAverage.setText(String.valueOf(degree.getGradeAverage()));
    }
}