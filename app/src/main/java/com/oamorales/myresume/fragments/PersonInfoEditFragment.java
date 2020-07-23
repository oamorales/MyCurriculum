package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.oamorales.myresume.R;

public class PersonInfoEditFragment extends Fragment {

    private TextInputEditText editText;

    public PersonInfoEditFragment() {    }

    // TODO: Rename and change types and number of parameters
    public static PersonInfoEditFragment newInstance(String param1, String param2) {
        PersonInfoEditFragment fragment = new PersonInfoEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_info_edit, container, false);
        editText = view.findViewById(R.id.editPersonName);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}