package com.oamorales.myresume.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.oamorales.myresume.R;
import com.oamorales.myresume.fragments.DegreesFragmentDirections;
import com.oamorales.myresume.models.Degree;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class DegreesRecyclerAdapter extends RecyclerView.Adapter <DegreesRecyclerAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private List<Degree> list;

    public DegreesRecyclerAdapter(Context context, int layout, List<Degree> list) {
        this.context = context;
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


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDegree;
        private ImageView imageViewDegree;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewDegree = itemView.findViewById(R.id.degreesListViewText);
            this.imageViewDegree = itemView.findViewById(R.id.degreesListViewImage);
        }

        public void bind(Degree currentDegree){
            this.textViewDegree.setText(currentDegree.getDegreeTittle());
            if (currentDegree.getImageLogo()!= null){
                Uri uri = Uri.fromFile(new File(currentDegree.getImageLogo()));
                Picasso.get().load(uri).fit().into(this.imageViewDegree);
            }
            /** Se crea la acción para cambiar de fragment y se pasan los parámetros */
            assert currentDegree.getImageLogo() != null;
            /*DegreesFragmentDirections.ActionDegreesFragmentToDegreeDetailsFragment directions = DegreesFragmentDirections
                    .actionDegreesFragmentToDegreeDetailsFragment(currentDegree.getId(), currentDegree.getImageLogo(), currentDegree.getDegreeTittle(),
                            currentDegree.getUniversity(), currentDegree.getDiscipline(), currentDegree.getYearBegin(),
                            currentDegree.getYearEnd(), currentDegree.getGradeAverage());*/
            DegreesFragmentDirections.ActionDegreesFragmentToDegreeDetailsFragment directions = DegreesFragmentDirections
                    .actionDegreesFragmentToDegreeDetailsFragment(currentDegree.getId());
            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
        }

    }
}
