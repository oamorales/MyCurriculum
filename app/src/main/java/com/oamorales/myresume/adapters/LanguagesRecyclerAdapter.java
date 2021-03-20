package com.oamorales.myresume.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.oamorales.myresume.R;
import com.oamorales.myresume.fragments.LanguagesFragmentDirections;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.utils.DBManager;
import com.oamorales.myresume.utils.Levels;

import java.util.List;

public class LanguagesRecyclerAdapter extends RecyclerView.Adapter<LanguagesRecyclerAdapter.ViewHolder>  {

    private Activity activity;
    private int layout;
    private List<Language> list;

    public LanguagesRecyclerAdapter(Activity activity, int layout, List<Language> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
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

        private MaterialTextView language;
        private MaterialTextView languageLevel;
        private AppCompatRatingBar ratingBar;
        private Language language1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.language = itemView.findViewById(R.id.language);
            this.languageLevel = itemView.findViewById(R.id.languageLevel);
            this.ratingBar = itemView.findViewById(R.id.languageRatingBar);
            itemView.setOnCreateContextMenuListener(this);
        }

        private void bind(Language currentLanguage){
            this.language.setText(currentLanguage.getLanguage());
            int level = currentLanguage.getLevel();
            this.ratingBar.setRating(level);
            this.ratingBar.setClickable(false);
            this.ratingBar.setEnabled(false);
            //this.ratingBar.setActivated(true);
            String textLevel = "";
            switch (level){
                case Levels.LANGUAGE_BASIC_LEVEL:
                    textLevel = "BASIC LEVEL";
                    this.languageLevel.setText(textLevel);
                    break;
                case Levels.LANGUAGE_BASIC_LIMITED_LEVEL:
                    textLevel = "BASIC LIMITED LEVEL";
                    this.languageLevel.setText(textLevel);
                    break;
                case Levels.LANGUAGE_BASIC_PROFESSIONAL_LEVEL:
                    textLevel = "BASIC PROFESSIONAL LEVEL";
                    this.languageLevel.setText(textLevel);
                    break;
                case Levels.LANGUAGE_PROFESSIONAL_LEVEL:
                    textLevel = "PROFESSIONAL LEVEL";
                    this.languageLevel.setText(textLevel);
                    break;
                case Levels.LANGUAGE_BILINGUAL_NATIVE:
                    textLevel = "BILINGUAL / NATIVE";
                    this.languageLevel.setText(textLevel);
                    break;
                default:
                    this.ratingBar.setRating(0);
                    textLevel = "BASIC LEVEL";
                    this.languageLevel.setText(textLevel);
            }

            LanguagesFragmentDirections.ActionLanguagesFragmentToAddEditLanguageFragment directions =
                    LanguagesFragmentDirections.actionLanguagesFragmentToAddEditLanguageFragment(currentLanguage.getLanguage());
            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            language1 = list.get(getAdapterPosition());
            menu.setHeaderTitle(language1.getLanguage());
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            /** Assign onClick event to every menu item */
            for (int i=0; i<menu.size(); i++)
                menu.getItem(i).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.deleteButton){
                confirmDeletion(language1);
            }
            return true;
        }

        private void confirmDeletion(final Language language) {
            MaterialAlertDialogBuilder materialBuilder = new MaterialAlertDialogBuilder(activity);
            materialBuilder.setTitle(language.getLanguage())
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBManager.delete(language, activity);
                            notifyItemRemoved(getAdapterPosition());
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }

    }/** End ViewHolder */
}
