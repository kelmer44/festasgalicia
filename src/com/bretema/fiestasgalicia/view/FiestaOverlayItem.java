package com.bretema.fiestasgalicia.view;

import android.graphics.drawable.Drawable;

import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.model.Evento;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class FiestaOverlayItem extends OverlayItem {

	private Evento	fiesta;

	public FiestaOverlayItem(GeoPoint arg0, String arg1, String arg2, Evento fiesta) {
		super(arg0, arg1, arg2);
		this.fiesta = fiesta;
	}

	public static FiestaOverlayItem createOverlayItem(Evento f) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

		String snippet = "Del " + sdf.format(f.getFechaInicio()) + " al " + sdf.format(f.getFechaInicio()) + " en " + f.getMunicipio().getNombre();
		GeoPoint point = new GeoPoint((int) (f.getLatitud() * 1E6), (int) (f.getLongitud() * 1E6));
		FiestaOverlayItem foi = new FiestaOverlayItem(point, f.getNombre(), snippet, f);

		return foi;
	}

	public Evento getFiesta() {
		return fiesta;
	}

	public void setFiesta(Evento fiesta) {
		this.fiesta = fiesta;
	}

}
