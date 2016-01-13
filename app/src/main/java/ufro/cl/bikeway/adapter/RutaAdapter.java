package ufro.cl.bikeway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import ufro.cl.bikeway.R;
import ufro.cl.bikeway.db.Ruta;

public class RutaAdapter extends ArrayAdapter<Ruta> {

    public RutaAdapter(Context context, List<Ruta> rutas) {
        super(context, R.layout.custom_row, rutas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater rutasInflater = LayoutInflater.from(getContext());
        View customView = rutasInflater.inflate(R.layout.custom_row, parent, false);
        Ruta ruta = getItem(position);
        TextView nombreRuta = (TextView) customView.findViewById(R.id.rutaNombre);
        TextView distancia = (TextView) customView.findViewById(R.id.rutaDistancia);
        ImageView rutaImage = (ImageView) customView.findViewById(R.id.imageView);
        nombreRuta.setText(ruta.getNombre());
        distancia.setText(ruta.getDistancia()/100 + " KM");
        rutaImage.setImageResource(R.drawable.map_icon);

        return customView;
    }
}
