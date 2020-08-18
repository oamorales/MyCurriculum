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
import com.oamorales.myresume.adapters.DegreesRecyclerAdapter;
import com.oamorales.myresume.databinding.FragmentDegreesBinding;
import com.oamorales.myresume.models.Degree;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DegreesFragment extends Fragment {

    private Realm realm;
    private FragmentDegreesBinding binding;
    private RecyclerView.LayoutManager layoutManager;

    public DegreesFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Se accede a la base de datos */
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDegreesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        layoutManager = new LinearLayoutManager(requireActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /** Se crea la lista de degrees y se agregan los elementos obtenidos de la base de datos */
        RealmResults<Degree> degrees = realm.where(Degree.class).findAll().sort("yearEnd", Sort.DESCENDING);
        /** Se añade el layout manager */
        binding.degreesListView.setLayoutManager(layoutManager);
        /** Se crea el adaptador y se le añade al listView */
        binding.degreesListView.setAdapter(new DegreesRecyclerAdapter(requireActivity(),R.layout.degrees_list_view, degrees));
        /** Se captura la acción y se añade al evento onClick del FAB */
        NavDirections navDirections = DegreesFragmentDirections.actionDegreesFragmentToNewDegreeFragment();
        binding.fabNewDegree.setOnClickListener(Navigation.createNavigateOnClickListener(navDirections));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /** Close DB connection */
        realm.close();
    }
}
