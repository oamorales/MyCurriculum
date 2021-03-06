package com.oamorales.myresume.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.oamorales.myresume.R;
import com.oamorales.myresume.fragments.DegreesFragmentDirections;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.viewmodels.DegreesViewModel;

import java.util.List;

public class DegreesRecyclerAdapter extends RecyclerView.Adapter <DegreesRecyclerAdapter.ViewHolder> {

    private Activity activity;
    private int layout;
    private List<Degree> list;
    private DegreesViewModel viewModel;

    public DegreesRecyclerAdapter(Activity activity, int layout, List<Degree> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(DegreesViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private MaterialTextView textViewDegree;
        private MaterialTextView university;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewDegree = itemView.findViewById(R.id.degreesCardViewDegree);
            this.university = itemView.findViewById(R.id.degreesCardViewUniversity);
            /** Assign context menu listener */
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(Degree currentDegree){
            this.textViewDegree.setText(currentDegree.getDegreeTittle());
            this.university.setText(currentDegree.getUniversity());
            /** Se crea la acción para cambiar de fragment y se pasan los parámetros */
            DegreesFragmentDirections.ActionDegreesFragmentToDegreeDetailsFragment directions = DegreesFragmentDirections
                    .actionDegreesFragmentToDegreeDetailsFragment(currentDegree.getId());
            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Degree currentDegree = list.get(getAdapterPosition());
            menu.setHeaderTitle(currentDegree.getDegreeTittle());
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            /** Assign onClick event to every menu item */
            for (int i=0; i<menu.size(); i++)
                menu.getItem(i).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.deleteButton){
                confirmDeletion();
            }
            return true;
        }

        private void confirmDeletion(){
            final Degree currentDegree = list.get(getAdapterPosition());
            MaterialAlertDialogBuilder materialBuilder = new MaterialAlertDialogBuilder(activity);
            materialBuilder.setTitle(currentDegree.getDegreeTittle())
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.removeDegree(currentDegree);
                            notifyItemRemoved(getAdapterPosition());
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }
}
