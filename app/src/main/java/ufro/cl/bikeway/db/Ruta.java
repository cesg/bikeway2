package ufro.cl.bikeway.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

//@Table(name = "rutas")
public class Ruta  /*extends Model*/{
    //@Column(name = "overviewPolyne")
    private String overviewPolyline;

    private List<LatLng> route;

    //@Column(name = "distancia")
    private double distancia;

    //@Column(name = "tiempo")
    private double tiempo;

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
