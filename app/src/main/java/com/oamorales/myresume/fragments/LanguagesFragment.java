package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.adapters.LanguagesRecyclerAdapter;
import com.oamorales.myresume.databinding.FragmentLanguagesBinding;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.viewmodels.LanguagesViewModel;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import io.realm.RealmResults;

public class LanguagesFragment extends Fragment {

    private Realm realm;
    private FragmentLanguagesBinding binding;
    private LanguagesViewModel viewModel;
    private LanguagesRecyclerAdapter adapter;

    public LanguagesFragment() {  }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        binding = FragmentLanguagesBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(LanguagesViewModel.class);
        viewModel.init();
        viewModel.getLanguageList().observe(getViewLifecycleOwner(), new Observer<RealmResults<Language>>() {
            @Override
            public void onChanged(RealmResults<Language> languages) {
                adapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.languagesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new LanguagesRecyclerAdapter(requireActivity(), R.layout.language_card_view, viewModel.getLanguageList().getValue());
        binding.languagesRecyclerView.setAdapter(adapter);
        binding.fabNewLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView())
                        .navigate(LanguagesFragmentDirections.actionLanguagesFragmentToNewLanguageFragment());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        binding = null;
    }
}
