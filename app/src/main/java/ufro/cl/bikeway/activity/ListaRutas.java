package ufro.cl.bikeway.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ufro.cl.bikeway.R;
import ufro.cl.bikeway.adapter.RutaAdapter;
import ufro.cl.bikeway.db.Ruta;

public class ListaRutas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rutas);
        List<Ruta> queryResuts = new Select().from(Ruta.class).limit(100).execute();
        ListView rutaListWiew = (ListView) findViewById(R.id.listaDeRutas);
        ListAdapter rutasAdapter = new RutaAdapter(this, queryResuts);
        rutaListWiew.setAdapter(rutasAdapter);

        rutaListWiew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ruta ruta = (Ruta) parent.getItemAtPosition(position);
                Intent intentPassRuta = new Intent(getApplicationContext(), RutaActivity.class);
                intentPassRuta.putExtra("ruta_id", ruta.getId());
                startActivity(intentPassRuta);
            }
        });
    }
}
