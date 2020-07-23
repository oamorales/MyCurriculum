package com.oamorales.myresume.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oamorales.myresume.R;
import com.oamorales.myresume.adapters.DegreesAdapter;
import com.oamorales.myresume.models.Degree;

import io.realm.Realm;
import io.realm.RealmResults;

public class DegreesFragment extends Fragment {

    private Realm realm;
    private ListView listView;
    private FloatingActionButton fab;

    public DegreesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_degrees, container, false);
        /** Se accede a la base de datos */
        realm = Realm.getDefaultInstance();
        listView = view.findViewById(R.id.degreesListView);
        fab = view.findViewById(R.id.fabNewDegree);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /** Se crea la lista de degrees y se agregan los elementos obtenidos de la base de datos */
        RealmResults<Degree> degrees = realm.where(Degree.class).findAll();
        /** Se crea el adaptador y se le añade al listView */
        DegreesAdapter adapter = new DegreesAdapter(getContext(),R.layout.degrees_list_view,degrees);
        listView.setAdapter(adapter);

        /** Se captura la acción y se añade al evento onClick del FAB */
        NavDirections navDirections = DegreesFragmentDirections.actionDegreesFragmentToNewDegreeFragment();
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(navDirections));

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Funciona botón", Toast.LENGTH_SHORT).show();
                DegreesFragmentDirections.ActionDegreesFragmentToDegreeDetailsFragment directions = DegreesFragmentDirections
                        .actionDegreesFragmentToDegreeDetailsFragment("INGENIERO");
                //NavHostFragment.findNavController();
                Navigation.findNavController(v).navigate(directions);
            }
        });*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
