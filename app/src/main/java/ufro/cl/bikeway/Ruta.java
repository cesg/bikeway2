package ufro.cl.bikeway;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class Ruta {
    private String overviewPolyline;
    private List<LatLng> route;
    private double distancia;
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
