package com.oamorales.myresume.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.oamorales.myresume.R;
import com.oamorales.myresume.fragments.DegreesFragmentDirections;
import com.oamorales.myresume.models.Degree;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DegreesAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Degree> list;

    public DegreesAdapter(Context context, int layout, List<Degree> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(this.layout, null);
            holder = new ViewHolder();
            holder.imageViewDegree = convertView.findViewById(R.id.degreesListViewImage);
            holder.textViewDegree = convertView.findViewById(R.id.degreesListViewText);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Degree current = list.get(position);
        Picasso.get().load(current.getImageLogo()).fit().into(holder.imageViewDegree);
        holder.textViewDegree.setText(current.getDegreeTittle());

        /** Se crea la acción para cambiar de fragment y se pasan los parámetros */
        DegreesFragmentDirections.ActionDegreesFragmentToDegreeDetailsFragment directions = DegreesFragmentDirections
                .actionDegreesFragmentToDegreeDetailsFragment("TSU EN ELECTRONICA Y MAS ALLA");

        /** Se asigna evento onClick a cada elemento de la lista */
        convertView.setOnClickListener(Navigation.createNavigateOnClickListener(directions));

        return convertView;
    }

    static class ViewHolder{
        TextView textViewDegree;
        ImageView imageViewDegree;
    }
}
