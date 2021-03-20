package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentNewLanguageBinding;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.utils.Levels;
import com.oamorales.myresume.viewmodels.LanguagesViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NewLanguageFragment extends Fragment implements View.OnClickListener{

    private FragmentNewLanguageBinding binding;
    private LanguagesViewModel viewModel;
    private int level = 0;

    public NewLanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        binding = FragmentNewLanguageBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(LanguagesViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners(){
        binding.saveBtn.setOnClickListener(this);
        binding.newLanguageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                v.setError(null);
                return true;
            }
        });
        binding.newLanguageRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                level = (int) rating;
                switch (level){
                    case 1:
                        binding.newLanguageTextRating.setText(getText(R.string.language_level_1));
                        break;
                    case 2:
                        binding.newLanguageTextRating.setText(getText(R.string.language_level_2));
                        break;
                    case 3:
                        binding.newLanguageTextRating.setText(getText(R.string.language_level_3));
                        break;
                    case 4:
                        binding.newLanguageTextRating.setText(getText(R.string.language_level_4));
                        break;
                    case 5:
                        binding.newLanguageTextRating.setText(getText(R.string.language_level_5));
                        break;
                }
            }
        });
    }

    /** Save btn event */
    @Override
    public void onClick(View v) {
        String language = Objects.requireNonNull(binding.newLanguageEditText.getText()).toString();
        if (checkParams(language, level)){
            Language addLanguage = new Language(language, level);
            if (verifyIfExists(language)) {
                viewModel.saveLanguage(addLanguage, requireContext());
                Toast.makeText(requireContext(), getText(R.string.saved), Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(requireActivity(), getString(R.string.language_exists), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkParams(String string, int value){
        if (string.isEmpty()){
            binding.newLanguageEditText.setError(getString(R.string.required));
            return false;
        }else if (value < Levels.LANGUAGE_BASIC_LEVEL){
            binding.newLanguageRatingBar.setRating(1);
            return false;
        }else {
            return true;
        }
    }

    private boolean verifyIfExists(String language){
        return viewModel.exist(language);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}