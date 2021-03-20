package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.adapters.WorkExpRecyclerAdapter;
import com.oamorales.myresume.databinding.FragmentWorkExpBinding;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.viewmodels.WorkExpViewModel;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmResults;

public class WorkExpFragment extends Fragment {

    private FragmentWorkExpBinding binding;
    private WorkExpRecyclerAdapter adapter;
    private WorkExpViewModel viewModel;

    public WorkExpFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** Se accede a la base de datos */
        binding = FragmentWorkExpBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(WorkExpViewModel.class);
        viewModel.init();
        viewModel.getWorkExpList().observe(getViewLifecycleOwner(), new Observer<RealmResults<WorkExp>>() {
            @Override
            public void onChanged(RealmResults<WorkExp> workExps) {
                adapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /** Se añade el layout manager */
        binding.workExpRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        /** Se crea el adaptador y se le añade al RecyclerView */
        adapter = new WorkExpRecyclerAdapter(requireActivity(), R.layout.work_exp_card_view, viewModel.getWorkExpList().getValue());
        binding.workExpRecyclerView.setAdapter(adapter);
        /** Se captura la acción y se añade al evento onClick del FAB */
        NavDirections navDirections = WorkExpFragmentDirections.actionWorkExpFragmentToNewWorkExpFragment();
        binding.fabNewWorkExp.setOnClickListener(Navigation.createNavigateOnClickListener(navDirections));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
