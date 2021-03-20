package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.oamorales.myresume.databinding.FragmentEditLanguageBinding;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.utils.Levels;
import com.oamorales.myresume.viewmodels.LanguagesViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EditLanguageFragment extends Fragment implements View.OnClickListener {

    private FragmentEditLanguageBinding binding;
    private LanguagesViewModel viewModel;
    /** Language to edit */
    private Language languageToEdit;
    private EditLanguageFragmentArgs args;
    private String language;
    private int level = 0;

    public EditLanguageFragment() {/** Required empty public constructor */ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Enable toolbar */
        setHasOptionsMenu(true);
        args = EditLanguageFragmentArgs.fromBundle(requireArguments());
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
        binding = FragmentEditLanguageBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(LanguagesViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        language = args.getLanguage();
        viewModel.getLanguage(language).observe(getViewLifecycleOwner(), new Observer<Language>() {
            @Override
            public void onChanged(Language language) {
                languageToEdit = language;
                level = language.getLevel();
                binding.languageEditText.setText(language.getLanguage());
                binding.addLanguageRatingBar.setRating(language.getLevel());
            }
        });
        setListeners();
    }

    private void setListeners(){
        binding.saveBtn.setOnClickListener(this);
        binding.languageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                v.setError(null);
                return true;
            }
        });
        binding.addLanguageRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                level = (int) rating;
                switch (level){
                    case 1:
                        binding.addLanguageTextRating.setText(getText(Levels.LANGUAGE_LEVEL_1));
                        break;
                    case 2:
                        binding.addLanguageTextRating.setText(getText(Levels.LANGUAGE_LEVEL_2));
                        break;
                    case 3:
                        binding.addLanguageTextRating.setText(getText(Levels.LANGUAGE_LEVEL_3));
                        break;
                    case 4:
                        binding.addLanguageTextRating.setText(getText(Levels.LANGUAGE_LEVEL_4));
                        break;
                    case 5:
                        binding.addLanguageTextRating.setText(getText(Levels.LANGUAGE_LEVEL_5));
                        break;
                }
            }
        });
    }

    /** Save btn event */
    @Override
    public void onClick(View v) {
        language = Objects.requireNonNull(binding.languageEditText.getText()).toString();
        if (checkParams(language, level)){
            Language addLanguage = new Language(language, level);
            viewModel.deleteLanguage(languageToEdit, requireContext());
            viewModel.saveLanguage(addLanguage, requireContext());
            Toast.makeText(requireContext(), getText(R.string.saved), Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }

    private boolean checkParams(String string, int value){
        if (string.isEmpty()){
            binding.languageEditText.setError(getString(R.string.required));
            return false;
        }else if (value < Levels.LANGUAGE_BASIC_LEVEL){
            binding.addLanguageRatingBar.setRating(1);
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