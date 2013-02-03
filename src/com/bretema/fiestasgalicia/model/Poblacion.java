package com.bretema.fiestasgalicia.model;

import com.google.android.maps.GeoPoint;

public class Poblacion {

    private String nombre;
    
    private GeoPoint posicion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public GeoPoint getPosicion() {
        return posicion;
    }

    public void setPosicion(GeoPoint posicion) {
        this.posicion = posicion;
    }

    public Poblacion(String nombre, GeoPoint posicion) {
        super();
        this.nombre = nombre;
        this.posicion = posicion;
    }
    
    
}
