package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentNewWorkExpBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewWorkExpFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private FragmentNewWorkExpBinding binding;
    private List<Integer> years = new ArrayList<>();

    public NewWorkExpFragment() { /** Required empty public constructor */  }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewWorkExpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        int year = 2020;
        for (int i = 0; i<15; i++){
            years.add(year);
            year--;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_years, years);
        binding.newWorkExpYearBegin.setAdapter(adapter);
        binding.newWorkExpYearEnd.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}