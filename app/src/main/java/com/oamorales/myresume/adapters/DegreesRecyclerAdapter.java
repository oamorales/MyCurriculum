package com.oamorales.myresume.adapters;

import android.content.Context;
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
        holder.bind(list.get(position).getDegreeTittle(),list.get(position).getImageLogo());
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

        public void bind(String text, int logoResource){
            this.textViewDegree.setText(text);
            Picasso.get().load(logoResource).fit().into(this.imageViewDegree);

            /** Se crea la acción para cambiar de fragment y se pasan los parámetros */
            DegreesFragmentDirections.ActionDegreesFragmentToDegreeDetailsFragment directions = DegreesFragmentDirections
                    .actionDegreesFragmentToDegreeDetailsFragment(textViewDegree.getText().toString());

            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
        }
    }

}
