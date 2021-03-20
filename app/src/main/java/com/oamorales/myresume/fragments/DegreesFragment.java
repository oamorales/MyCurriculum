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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.adapters.DegreesRecyclerAdapter;
import com.oamorales.myresume.databinding.FragmentDegreesBinding;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.viewmodels.DegreesViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DegreesFragment extends Fragment {

    private FragmentDegreesBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private DegreesViewModel degreesViewModel;
    private DegreesRecyclerAdapter adapter;

    public DegreesFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDegreesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        /** Se crea el layout manager */
        layoutManager = new LinearLayoutManager(requireActivity());
        /** Se configura el viewModel */
        degreesViewModel = new ViewModelProvider(requireActivity()).get(DegreesViewModel.class);
        degreesViewModel.init();
        degreesViewModel.getDegreesList().observe(getViewLifecycleOwner(), new Observer<List<Degree>>() {
            @Override
            public void onChanged(List<Degree> degrees) {
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /** Se añade el layout manager */
        binding.degreesRecyclerView.setLayoutManager(layoutManager);
        /** Se crea el adaptador y se le añade al recyclerView */
        adapter = new DegreesRecyclerAdapter(requireActivity(),R.layout.degrees_card_view, degreesViewModel.getDegreesList().getValue());
        binding.degreesRecyclerView.setAdapter(adapter);
        /** Se captura la acción y se añade al evento onClick del FAB */
        NavDirections navDirections = DegreesFragmentDirections.actionDegreesFragmentToNewDegreeFragment();
        binding.fabNewDegree.setOnClickListener(Navigation.createNavigateOnClickListener(navDirections));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
