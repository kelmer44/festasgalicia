
package com.bretema.fiestasgalicia.model;

import java.util.Date;

public class Evento {

	private int id;
	
    private String nombre;

    private String descripcion;

    private Date fechaInicio;

    private Date fechaFin;
    
    private Poblacion poblacion;
    
    private Municipio municipio;
    
    private double latitud;
    
    private double longitud;
    
    private String otros;
    
    private String imagenPrincipal;
    
    private String imagenLista;
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
    }

  
	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public String getOtros() {
		return otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
	}

	public String getImagenPrincipal() {
		return imagenPrincipal;
	}

	public void setImagenPrincipal(String imagenPrincipal) {
		this.imagenPrincipal = imagenPrincipal;
	}

	public String getImagenLista() {
		return imagenLista;
	}

	public void setImagenLista(String imagenLista) {
		this.imagenLista = imagenLista;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Evento(int id, String nombre, String descripcion, Date fechaInicio, Date fechaFin, Poblacion poblacion, Municipio municipio, double latitud, double longitud, String otros,
			String imagenPrincipal, String imagenLista) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.poblacion = poblacion;
		this.municipio = municipio;
		this.latitud = latitud;
		this.longitud = longitud;
		this.otros = otros;
		this.imagenPrincipal = imagenPrincipal;
		this.imagenLista = imagenLista;
	}

	public Evento() {
		super();
	}

	
}
