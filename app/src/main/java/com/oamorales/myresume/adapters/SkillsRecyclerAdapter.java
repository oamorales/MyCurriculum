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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.oamorales.myresume.R;
import com.oamorales.myresume.models.Skill;
import com.oamorales.myresume.viewmodels.SkillsViewModel;

import java.util.List;
import java.util.Objects;

public class SkillsRecyclerAdapter extends RecyclerView.Adapter<SkillsRecyclerAdapter.ViewHolder> {

    private Activity activity;
    private int layout;
    private List<Skill> list;
    private SkillsViewModel viewModel;

    public SkillsRecyclerAdapter(Activity activity, int layout, List<Skill> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(SkillsViewModel.class);
    }

    @NonNull
    @Override
    public SkillsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsRecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private MaterialTextView ability;
        private Skill skill;
        private AppCompatEditText skillEdited;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ability = itemView.findViewById(R.id.abilityTextView);
            itemView.setOnCreateContextMenuListener(this);
        }

        private void bind(final Skill skill){
            this.ability.setText(skill.getSkill());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSkill(skill);
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            skill = list.get(getAdapterPosition());
            menu.setHeaderTitle(skill.getSkill());
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            /** Assign onClick event to every menu item */
            for (int i=0; i<menu.size(); i++)
                menu.getItem(i).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.deleteButton)
                confirmDeletion(skill);
            return true;
        }

        private void confirmDeletion(final Skill skill) {
            MaterialAlertDialogBuilder materialBuilder = new MaterialAlertDialogBuilder(activity);
            materialBuilder.setTitle(skill.getSkill())
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.removeSkill(skill);
                            notifyItemRemoved(getAdapterPosition());
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }

        private void editSkill(final Skill skill){
            skillEdited = new AppCompatEditText(activity);
            MaterialAlertDialogBuilder materialBuilder = new MaterialAlertDialogBuilder(activity);
            materialBuilder.setTitle(skill.getSkill()).setView(skillEdited)
                    .setMessage(R.string.edit_message)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String value = Objects.requireNonNull(skillEdited.getText()).toString();
                            if (value.isEmpty())
                                dialog.dismiss();
                            else{
                                viewModel.updateSkill(skill, value);
                                notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }
}
