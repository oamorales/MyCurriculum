package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentAddEditLanguageBinding;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.utils.DBManager;
import com.oamorales.myresume.utils.Levels;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddEditLanguageFragment extends Fragment implements View.OnClickListener {

    private FragmentAddEditLanguageBinding binding;
    private Realm realm;
    private Language editLanguage;
    private AddEditLanguageFragmentArgs args;
    private String language;
    private boolean edit = false;
    private int level = 0;

    public AddEditLanguageFragment() {/** Required empty public constructor */ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = AddEditLanguageFragmentArgs.fromBundle(requireArguments());

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        binding = FragmentAddEditLanguageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (args.getLanguage() != null){
            language = args.getLanguage();
            edit = true;
            setUI();
        }
        setListeners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

    private void setUI(){
        editLanguage = realm.where(Language.class).equalTo("language", language).findFirst();
        assert editLanguage != null;
        level = editLanguage.getLevel();
        binding.languageEditText.setText(editLanguage.getLanguage());
        binding.addLanguageRatingBar.setRating(editLanguage.getLevel());
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
                Toast.makeText(requireActivity(), level + "STARS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Save btn event */
    @Override
    public void onClick(View v) {
        language = Objects.requireNonNull(binding.languageEditText.getText()).toString();
        if (checkParams(language, level)){
            if (edit){
                realm.beginTransaction();
                //editLanguage.setLanguage(language);
                editLanguage.setLevel(level);
                realm.copyToRealmOrUpdate(editLanguage);
                realm.commitTransaction();
                requireActivity().onBackPressed();
            } else if (verifyIfExists(language)) {
                Language addLanguage = new Language(language, level);
                realm.beginTransaction();
                realm.copyToRealm(addLanguage);
                realm.commitTransaction();
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(requireActivity(), getString(R.string.language_exists), Toast.LENGTH_LONG).show();
            }
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
        RealmResults<Language> checkLanguage = realm.where(Language.class).equalTo("language", language).findAll();
        return (checkLanguage.isEmpty());
    }

}