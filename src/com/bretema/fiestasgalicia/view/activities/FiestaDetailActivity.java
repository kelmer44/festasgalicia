package com.bretema.fiestasgalicia.view.activities;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.model.Evento;
import com.bretema.fiestasgalicia.model.Municipio;
import com.bretema.fiestasgalicia.model.Subtipo;
import com.bretema.fiestasgalicia.util.JSONParser;
import com.bretema.fiestasgalicia.util.ServiceConstants;
public class FiestaDetailActivity extends SherlockActivity {

	private int	id;


	// JSON parser class
	private JSONParser jsonParser = new JSONParser();
	

	private Evento	fiesta;
	
	private Bitmap imagen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fiesta_details);
		
		
		// obtenemos la llamada a esta activity
		Intent i = getIntent();
		id = i.getIntExtra("id", 0);
		
		
		new GetFiestaDetails().execute();
	}

	
	
	/**
   	 * Background Async Task to Get complete product details
   	 * */
   	class GetFiestaDetails extends AsyncTask<Void, Void, Boolean> {

   		private String TAG = GetFiestaDetails.class.getSimpleName();
		private ProgressDialog	pDialog;
   		/**
   		 * Before starting background thread Show Progress Dialog
   		 * */
   		@Override
   		protected void onPreExecute() {
   			super.onPreExecute();
   			pDialog = new ProgressDialog(FiestaDetailActivity.this);
   			pDialog.setMessage("Por favor, espere...");
   			pDialog.setIndeterminate(false);
   			pDialog.setTitle("Obteniendo información");
   			pDialog.setCancelable(true);
   			pDialog.show();
   		}

   		/**
   		 * Getting line details 
   		 * Return true if ok, false if not
   		 * */
   		protected Boolean doInBackground(Void... args) {

				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();

   			try {
   				
   				// getting product details by making HTTP request
   				// Note that product details url will use GET request
   				JSONObject json = jsonParser.makeHttpRequest(com.bretema.fiestasgalicia.util.ServiceConstants.url_evento + "/" + id, "GET", params);
				
   				// Check your log cat for JSON reponse
				Log.d("JSON FULL RESPONSE: ", json.toString());
				// Checking for SUCCESS TAG
				boolean success = json.getBoolean(ServiceConstants.TAG_SUCCESS);
				String message = json.getString(ServiceConstants.TAG_MESSAGE);
				
				Log.d("Server message: ", message);
				
   				// check your log for json response
   				Log.d("Single Line Details", json.toString());
   				// Getting data
				JSONObject dataObject = json.getJSONObject(ServiceConstants.TAG_DATA);
				int totalCount = dataObject.getInt(ServiceConstants.TAG_DATA_COUNT);
				
				Log.d("Total objects retrieved: ", "" + totalCount);

   				if (success) {
   					// lines JSONArray
   					JSONObject c = dataObject.getJSONObject(ServiceConstants.TAG_ARRAY_EVENTO);
					
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
					
					s.setDescripcion(nombreSubtipo);
					
					evento.setSubtipo(s);
					evento.setMunicipio(m);
					evento.setLatitud(latitud);
					evento.setLongitud(longitud);
					
					fiesta = evento;
   					return true;
   				}else{
   					//Toast.makeText(ViewLineActivity.this, "No se encontró la línea", Toast.LENGTH_LONG).show();
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

           protected void onCancelled() {
   		 	pDialog.dismiss();
               //Toast.makeText(LineListActivity.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
               FiestaDetailActivity.this.finish();
           }
   		/**
   		 * After completing background task Dismiss the progress dialog
   		 * **/
   		protected void onPostExecute(Boolean result) {
			pDialog.dismiss();
   			if(result){
   				// updating UI from Background Thread
   				runOnUiThread(new Runnable() {
   					public void run() {
   						/**
   						 * Updating parsed JSON data into ListView
   						 * */
   	
   						// product with this pid found
   						// Edit Text
   						
   						TextView nombre = (TextView) findViewById(R.id.nombreFiesta);
   						TextView descripcion = (TextView) findViewById(R.id.description);
   						TextView subtitulo = (TextView) findViewById(R.id.subtitleTextView);
   						java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
   						
   						
   						subtitulo.setText("Del " + sdf.format(fiesta.getFechaInicio()) + " al " +  sdf.format(fiesta.getFechaInicio()) + " en " + fiesta.getMunicipio().getNombre());
   						if(fiesta!=null){
   							nombre.setText(fiesta.getNombre());
   							descripcion.setText(Html.fromHtml(fiesta.getDescripcion()));
   						}
   						descripcion.setMovementMethod(new ScrollingMovementMethod());
   						new GetImagen().execute(fiesta.getImagenPrincipal());
   					}
   				});
   			}
   			else {
   				pDialog.dismiss();
   				AlertDialog.Builder builder = new AlertDialog.Builder(FiestaDetailActivity.this);
   				builder.setMessage("Error al adquirir información");
   				builder.setCancelable(false);
   				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
   		            public void onClick(DialogInterface dialog, int id) {
   		            	FiestaDetailActivity.this.finish();
   		            }
   		        }
   				);
   				AlertDialog alert = builder.create();
   				alert.show();
   			}
   				
   		}
   		
   		
   		
   	}
   	
   	class GetImagen extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {

			imagen = loadBitmap(params[0]);
			return true;
		}
		protected void onPostExecute(Boolean result) {
			if(result){
				ImageView imageView = (ImageView) findViewById(R.id.mainImage);
				ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar1);
				imageView.setImageBitmap(imagen);
				imageView.setVisibility(View.VISIBLE);
				pBar.setVisibility(View.GONE);
			}
		}
		
		public Bitmap loadBitmap(String url)
   		{
   		    Bitmap bm = null;
   		    InputStream is = null;
   		    BufferedInputStream bis = null;
   		    try 
   		    {
   		        URLConnection conn = new URL(url).openConnection();
   		        conn.connect();
   		        is = conn.getInputStream();
   		        bis = new BufferedInputStream(is, 8192);
   		        bm = BitmapFactory.decodeStream(bis);
   		    }
   		    catch (Exception e) 
   		    {
   		        e.printStackTrace();
   		    }
   		    finally {
   		        if (bis != null) 
   		        {
   		            try 
   		            {
   		                bis.close();
   		            }
   		            catch (IOException e) 
   		            {
   		                e.printStackTrace();
   		            }
   		        }
   		        if (is != null) 
   		        {
   		            try 
   		            {
   		                is.close();
   		            }
   		            catch (IOException e) 
   		            {
   		                e.printStackTrace();
   		            }
   		        }
   		    }
   		    return bm;
   		}
   		
   	}

}
