package ufro.cl.bikeway.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

@Table(name = "rutas")
public class Ruta  extends Model {

    @Column(name = "overviewPolyne")
    private String overviewPolyline;

    @Column(name = "nombre")
    private String nombre;

    private List<LatLng> route;

    @Column(name = "distancia")
    private double distancia;

    @Column(name = "tiempo")
    private double tiempo;

    @Column(name = "dificultad_id")
    private int dificultad;

    public Ruta() {
    }

    public Ruta(String nombre, double distancia, double tiempo, int dificultad, String overviewPolyline) {
        this.nombre = nombre;
        this.distancia = distancia;
        this.tiempo = tiempo;
        this.dificultad = dificultad;
        this.overviewPolyline = overviewPolyline;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public Ruta(String overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public String getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(String overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public List<LatLng> getPoints() {
        if (route == null) {
            this.route = PolyUtil.decode(this.overviewPolyline);
        }
        return route;
    }
}
