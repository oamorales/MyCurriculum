package com.oamorales.myresume.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentDegreeDetailsBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DegreeDetailsFragment extends Fragment {

    private FragmentDegreeDetailsBinding binding;
    private DegreeDetailsFragmentArgs args;
    private AppCompatTextView degreeTittle;

    public DegreeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Se recuperan los valores suministrados por el fragment anterior */
        args = DegreeDetailsFragmentArgs.fromBundle(requireArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDegreeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.degreeTitle.setText(args.getDegreeTittle());
        Uri uri = Uri.fromFile(new File(args.getDegreeLogo()));
        Picasso.get().load(uri).fit().into(binding.degreeDetailLogo);
        binding.degreeDetailUnivName.setText(args.getDegreeUniversity());
        binding.degreeDiscipline.setText(args.getDegreeDiscipline());
        binding.degreeBeginYear.setText((String.valueOf(args.getDegreeYearBegin())));
        binding.degreeEndYear.setText(String.valueOf(args.getDegreeYearEnd()));
        binding.degreeGradeAverage.setText(String.valueOf(args.getDegreeGradeAverage()));
    }
}