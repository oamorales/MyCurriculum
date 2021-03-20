package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.adapters.LanguagesRecyclerAdapter;
import com.oamorales.myresume.databinding.FragmentLanguagesBinding;
import com.oamorales.myresume.models.Language;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class LanguagesFragment extends Fragment {

    private Realm realm;
    private FragmentLanguagesBinding binding;

    public LanguagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        binding = FragmentLanguagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireActivity());
        binding.languagesRecyclerView.setLayoutManager(manager);
        RealmResults<Language> languages = realm.where(Language.class).findAll().sort("level", Sort.DESCENDING);
        binding.languagesRecyclerView.setAdapter(new LanguagesRecyclerAdapter(requireActivity(), R.layout.language_card_view, languages));
        LanguagesFragmentDirections.ActionLanguagesFragmentToAddEditLanguageFragment directions =
                LanguagesFragmentDirections.actionLanguagesFragmentToAddEditLanguageFragment(null);
        binding.fabNewLanguage.setOnClickListener(Navigation.createNavigateOnClickListener(directions));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
