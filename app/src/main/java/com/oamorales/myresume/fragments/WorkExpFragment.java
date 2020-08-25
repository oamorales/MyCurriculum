package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oamorales.myresume.R;
import com.oamorales.myresume.adapters.WorkExpRecyclerAdapter;
import com.oamorales.myresume.databinding.FragmentWorkExpBinding;
import com.oamorales.myresume.models.WorkExp;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class WorkExpFragment extends Fragment {

    private Realm realm;
    private FragmentWorkExpBinding binding;
    private RecyclerView.LayoutManager layoutManager;

    public WorkExpFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** Se accede a la base de datos */
        realm = Realm.getDefaultInstance();
        binding = FragmentWorkExpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        layoutManager = new LinearLayoutManager(requireActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /** Se crea la lista de WorkExp y se agregan los elementos obtenidos de la base de datos */
        RealmResults<WorkExp> workExps = realm.where(WorkExp.class).findAllAsync().sort("endYear", Sort.DESCENDING);
        /** Se añade el layout manager */
        binding.workExpRecyclerView.setLayoutManager(layoutManager);
        /** Se crea el adaptador y se le añade al RecyclerView */
        binding.workExpRecyclerView.setAdapter(new WorkExpRecyclerAdapter(requireActivity(), R.layout.work_exp_card_view, workExps));
        /** Se captura la acción y se añade al evento onClick del FAB */
        NavDirections navDirections = WorkExpFragmentDirections.actionWorkExpFragmentToNewWorkExpFragment();
        binding.fabNewWorkExp.setOnClickListener(Navigation.createNavigateOnClickListener(navDirections));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /** Close DB connection */
        realm.close();
    }
}
