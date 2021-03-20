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
import com.oamorales.myresume.fragments.WorkExpFragmentDirections;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.viewmodels.WorkExpViewModel;

import java.util.List;

public class WorkExpRecyclerAdapter extends RecyclerView.Adapter <WorkExpRecyclerAdapter.ViewHolder> {

    private Activity activity;
    private int layout;
    private List<WorkExp> list;
    private WorkExpViewModel viewModel;

    public WorkExpRecyclerAdapter(Activity activity, int layout, List<WorkExp> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WorkExpViewModel.class);
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
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private MaterialTextView companyName;
        private MaterialTextView position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.workExpCardViewCompany);
            position = itemView.findViewById(R.id.workExpCardViewPosition);
            itemView.setOnCreateContextMenuListener(this);
        }

        private void bind(WorkExp workExp){
            companyName.setText(workExp.getCompanyName());
            position.setText(workExp.getPosition());
            /** Se crea la acción para cambiar de fragment y se pasan los parámetros */
            WorkExpFragmentDirections.ActionWorkExpFragmentToWorkExpDetailsFragment directions = WorkExpFragmentDirections
                    .actionWorkExpFragmentToWorkExpDetailsFragment(workExp.getId());
            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            WorkExp currentWorkExp = list.get(getAdapterPosition());
            menu.setHeaderTitle(currentWorkExp.getPosition());
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

        private void confirmDeletion() {
            final WorkExp currentWorkExp = list.get(getAdapterPosition());
            MaterialAlertDialogBuilder materialBuilder = new MaterialAlertDialogBuilder(activity);
            materialBuilder.setTitle(currentWorkExp.getPosition())
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.removeWorkExp(currentWorkExp);
                            notifyItemRemoved(getAdapterPosition());
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }
}
