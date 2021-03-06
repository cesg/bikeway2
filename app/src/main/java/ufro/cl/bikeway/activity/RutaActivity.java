package ufro.cl.bikeway.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import ufro.cl.bikeway.R;
import ufro.cl.bikeway.db.Ruta;

public class RutaActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    public static final String TAG = RutaActivity.class.getSimpleName();
    private LocationManager locationManager;
    private Ruta ruta;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long rutaID = getIntent().getExtras().getLong("ruta_id");
        this.ruta = Ruta.load(Ruta.class, rutaID);
        setContentView(R.layout.activity_ruta);

        TextView txtNombreRuta = (TextView) this.findViewById(R.id.textNombreRuta);
        TextView txtDistancia = (TextView) findViewById(R.id.txtDistancia);
        Button focoRuta = (Button) findViewById(R.id.btnFocoRuta);
        focoRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixZoom(ruta);
            }
        });
        txtNombreRuta.setText(ruta.getNombre());
        txtDistancia.setText( ruta.getDistancia()/100 + " KM");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (!isGPSEnable && !isNetEnable) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Servicio de unbicación no activo");
            builder.setMessage("Por favor es necesario activar el GPS");
            builder.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent goGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goGPS);
                }
            });

            Dialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            Log.w(TAG, "No gps o network enable");
        } else if (isNetEnable) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 5, this);
        } else if (isGPSEnable) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 5, this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.w(TAG, "ruta way_points : "+ ruta.getOverviewPolyline());
        // Agrega el find me
        mMap.setMyLocationEnabled(true);

        // Tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Criterios para selecionar proveedor de ubicación
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        // Mi última posición conocida
        Location myLocation = locationManager.getLastKnownLocation(provider);

        if (myLocation != null) {
            double lat = myLocation.getLatitude();
            double lon = myLocation.getLongitude();

            LatLng latLng = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(ruta.getPoints());
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(4);
        polylineOptions.geodesic(true);

        mMap.addPolyline(polylineOptions);
        fixZoom(ruta);
    }

    private void fixZoom(Ruta route) {
        final List<LatLng> points = route.getPoints();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder bc = new LatLngBounds.Builder();

                for (LatLng item : points) {
                    bc.include(item);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 15));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
