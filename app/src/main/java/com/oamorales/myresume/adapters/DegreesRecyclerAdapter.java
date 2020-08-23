package com.oamorales.myresume.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.oamorales.myresume.R;
import com.oamorales.myresume.fragments.DegreesFragmentDirections;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.utils.DBManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class DegreesRecyclerAdapter extends RecyclerView.Adapter <DegreesRecyclerAdapter.ViewHolder> {

    private Activity activity;
    private int layout;
    private List<Degree> list;

    public DegreesRecyclerAdapter(Activity activity, int layout, List<Degree> list) {
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
        return this.list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private TextView textViewDegree;
        private ImageView imageViewDegree;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewDegree = itemView.findViewById(R.id.degreesCardViewText);
            this.imageViewDegree = itemView.findViewById(R.id.degreesCardViewImage);
            /** Assign context menu listener */
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(Degree currentDegree){
            this.textViewDegree.setText(currentDegree.getDegreeTittle());
            if (currentDegree.getImageLogo()!= null){
                Uri uri = Uri.fromFile(new File(currentDegree.getImageLogo()));
                Picasso.get().load(uri).fit().into(this.imageViewDegree);
            }
            /** Se crea la acción para cambiar de fragment y se pasan los parámetros */
            assert currentDegree.getImageLogo() != null;
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
            if (item.getItemId() == R.id.deleteDegreeButton){
                confirmDeletion();
            }
            return true;
        }

        private void confirmDeletion(){
            Degree currentDegree = list.get(getAdapterPosition());
            MaterialAlertDialogBuilder materialBuilder = new MaterialAlertDialogBuilder(activity);
            materialBuilder.setTitle(currentDegree.getDegreeTittle())
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBManager.delete(list.get(getAdapterPosition()), activity);
                            notifyItemRemoved(getAdapterPosition());
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }
}
