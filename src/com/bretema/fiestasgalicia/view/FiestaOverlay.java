package com.bretema.fiestasgalicia.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.model.Evento;
import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class FiestaOverlay extends BalloonItemizedOverlay<FiestaOverlayItem> {
	
	
	private ArrayList<FiestaOverlayItem> mOverlays = new ArrayList<FiestaOverlayItem>();
	private Context	mContext;
	private Drawable	drawableGastro;
	private Drawable	drawablePatro;

	public ArrayList<FiestaOverlayItem> getmOverlays() {
		return mOverlays;
	}


	public FiestaOverlay(Drawable defaultMarker, MapView mapView, Context ctx) {
		super(boundCenterBottom(defaultMarker), mapView);
		this.mContext = ctx;

		drawableGastro = boundCenterBottom(mContext.getResources().getDrawable(R.drawable.marker_green));
		drawablePatro = boundCenterBottom(mContext.getResources().getDrawable(R.drawable.marker_green_sec));
	}
	
	
	public void addOverlay(FiestaOverlayItem overlay) {
		Evento f = overlay.getFiesta();
		if(f.getSubtipo().getId() == 1){
			overlay.setMarker(drawableGastro);
		}
		else if(f.getSubtipo().getId() == 2) {
			overlay.setMarker(drawablePatro);
		}
        mOverlays.add(overlay);
        populate();
    }

    public void clear() {

        mOverlays.clear();
        populate();
    }

    @Override
    protected FiestaOverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }
    
  

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView){
        return false;
    }


	@Override
	protected boolean onBalloonTap(int index, FiestaOverlayItem item) {
		// TODO Auto-generated method stub
		return super.onBalloonTap(index, item);
	}

}
