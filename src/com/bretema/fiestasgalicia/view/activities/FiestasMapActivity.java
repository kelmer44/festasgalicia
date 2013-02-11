package com.bretema.fiestasgalicia.view.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.model.Evento;
import com.bretema.fiestasgalicia.model.Municipio;
import com.bretema.fiestasgalicia.model.Subtipo;
import com.bretema.fiestasgalicia.util.JSONParser;
import com.bretema.fiestasgalicia.util.ServiceConstants;
import com.bretema.fiestasgalicia.view.FiestaOverlay;
import com.bretema.fiestasgalicia.view.FiestaOverlayItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class FiestasMapActivity extends SherlockMapActivity {

	// Progress Dialog
	private ProgressDialog	pDialog;

	// JSON parser class
	private JSONParser		jParser	= new JSONParser();

	private MapView	mapView;

	private MapController	controller;

	private FiestaOverlay	fiestasOverlay;

	private ArrayList<Evento>	fiestasList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_layout);

     	//fetch the map view from the layout
		mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        //get the MapController object
        controller = mapView.getController();
        
        //latitude and longitude of Santiago
        double lat = 42.875461;
        double lon = -8.545406;
        //create geo point
        GeoPoint point = new GeoPoint((int)(lat * 1E6), (int)(lon *1E6));
        
        //animate to the desired point
        controller.setCenter(point);
        
        //set the map zoom to 13
        // zoom 1 is top world view
        controller.setZoom(10);
        
        Drawable drawableFiesta = this.getResources().getDrawable(R.drawable.marker_red);
        
        fiestasOverlay = new FiestaOverlay(drawableFiesta, mapView, this);
       
        mapView.getOverlays().add(fiestasOverlay);
        
        mapView.invalidate();
        
        fiestasList = new ArrayList<Evento>();
        
        
        new LoadFiestas().execute();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	/**
	 * Background Async Task to Load all lines by making HTTP Request
	 * */
	class LoadFiestas extends AsyncTask<Void, Void, Boolean> {

		private String			TAG	= LoadFiestas.class.getSimpleName();

		public ProgressDialog	pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(FiestasMapActivity.this);
			pDialog.setMessage("Cargando datos... por favor, espere.");
			pDialog.setIndeterminate(false);
			pDialog.setTitle("Obteniendo información");
			pDialog.setCancelable(true);
			pDialog.show();

			pDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					LoadFiestas.this.cancel(true);
				}
			});
		}

		/**
		 * getting All lines. Return true if ok, false if not
		 * */
		protected Boolean doInBackground(Void... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			try {
				// getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(ServiceConstants.url_evento, "GET", params);

				// Check your log cat for JSON reponse
				Log.d("JSON FULL RESPONSE: ", json.toString());
				// Checking for SUCCESS TAG
				boolean success = json.getBoolean(ServiceConstants.TAG_SUCCESS);
				String message = json.getString(ServiceConstants.TAG_MESSAGE);

				Log.d("Server message: ", message);

				// Getting data
				JSONObject dataObject = json.getJSONObject(ServiceConstants.TAG_DATA);
				int totalCount = dataObject.getInt(ServiceConstants.TAG_DATA_COUNT);
				Log.d("Total objects retrieved: ", "" + totalCount);

				if (success) {
					// products found
					// Getting Array of Products

					// lines JSONArray
					JSONArray eventos = dataObject.getJSONArray(ServiceConstants.TAG_ARRAY_EVENTO);

					// looping through All Products
					for (int i = 0; i < eventos.length(); i++) {
						JSONObject c = eventos.getJSONObject(i);

						// Storing each json item in variable
						int id = c.getInt(ServiceConstants.TAG_EVENTO_ID_EVENTO);

						String nombre = "";
						String descripcion = "";
						String otros = "";
						try {
							nombre = URLDecoder.decode(c.getString(ServiceConstants.TAG_EVENTO_NOMBRE), "UTF-8");
							descripcion = URLDecoder.decode(c.getString(ServiceConstants.TAG_EVENTO_DESCRIPCION), "UTF-8");
							otros = URLDecoder.decode(c.getString(ServiceConstants.TAG_EVENTO_OTROS), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String imagenPrincipal = c.getString(ServiceConstants.TAG_EVENTO_IMAGEN_PRINCIPAL);
						String imagenLista = c.getString(ServiceConstants.TAG_EVENTO_IMAGEN_LISTA);

						Evento evento = new Evento();
						evento.setId(id);
						evento.setNombre(nombre);
						evento.setDescripcion(descripcion);
						evento.setOtros(otros);
						evento.setImagenPrincipal(imagenPrincipal);
						evento.setImagenLista(imagenLista);
						evento.setFechaFin(new Date());
						evento.setFechaInicio(new Date());

						Municipio m = new Municipio();

						JSONObject municipioJson = c.getJSONObject(ServiceConstants.TAG_EVENTO_MUNICIPIO);

						int idMunicipio = municipioJson.getInt(ServiceConstants.TAG_MUNICIPIO_ID_MUNICIPIO);
						int idProvincia = municipioJson.getInt(ServiceConstants.TAG_MUNICIPIO_ID_PROVINCIA);
						double latitud = municipioJson.getDouble(ServiceConstants.TAG_MUNICIPIO_LATITUD);
						double longitud = municipioJson.getDouble(ServiceConstants.TAG_MUNICIPIO_LONGITUD);
						String nombreMunicipio = "";

						try {
							nombreMunicipio = URLDecoder.decode(municipioJson.getString(ServiceConstants.TAG_MUNICIPIO_NOMBRE), "UTF-8");
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						m.setId(idMunicipio);
						m.setNombre(nombreMunicipio);
						m.setLatitud(latitud);
						m.setLongitud(longitud);

						Subtipo s = new Subtipo();

						JSONObject subtipoJson = c.getJSONObject(ServiceConstants.TAG_EVENTO_SUBTIPO_EVENTO);

						int idSubtipo = subtipoJson.getInt(ServiceConstants.TAG_SUBTIPO_ID_SUBTIPO);

						String nombreSubtipo = "";

						try {
							nombreSubtipo = URLDecoder.decode(subtipoJson.getString(ServiceConstants.TAG_SUBTIPO_DESC_SUBTIPO_EVENTO), "UTF-8");
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						s.setId(idSubtipo);
						s.setDescripcion(nombreSubtipo);

						evento.setSubtipo(s);
						evento.setMunicipio(m);
						evento.setLatitud(latitud);
						evento.setLongitud(longitud);
						// adding HashList to ArrayList
						fiestasList.add(evento);
					}
					return true;
				} else {
					return false;
				}
			} catch (JSONException e) {
				Log.d(TAG, e.toString());
			} catch (ClientProtocolException e) {
				Log.d(TAG, e.toString());
			} catch (IOException e) {
				Log.d(TAG, e.toString());
			}

			return false;
		}

		@Override
		protected void onCancelled() {
			pDialog.dismiss();
			// Toast.makeText(LineListActivity.this, "Tarea cancelada!",
			// Toast.LENGTH_SHORT).show();
			// FiestasVistaListaActivity.this.finish();
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Boolean result) {

			// dismiss the dialog after processing
			pDialog.dismiss();
			if (result) {
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						/*
						 * ListAdapter adapter = new
						 * LineListAdapter(LineListActivity.this, lineList); //
						 * updating listview setListAdapter(adapter);
						 */
						for(Evento p: fiestasList){
							GeoPoint point = new GeoPoint((int) (p.getLatitud()*1E6),(int)(p.getLongitud() *1E6));
					        // create and add an OverlayItem to the MyItemizedOverlay list
					        FiestaOverlayItem overlayItem = FiestaOverlayItem.createOverlayItem(p);
					        fiestasOverlay.addOverlay(overlayItem);
					    
						}
						
						mapView.invalidate();

					}
				});
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(FiestasMapActivity.this);
				builder.setMessage("No se pudieron obtener los datos");
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//getActivity().finish();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		}

	}

}
