package com.oamorales.myresume.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oamorales.myresume.R;


public class PersonInfoFragment extends Fragment {

    private FloatingActionButton fab;

    public PersonInfoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_info, container, false);
        fab = view.findViewById(R.id.personInfoFAB);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavDirections directions = PersonInfoFragmentDirections.actionPersonInfoFragmentToPersonInfoEditFragment();
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }
}
