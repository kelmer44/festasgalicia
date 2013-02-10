package com.bretema.fiestasgalicia.view.fragment;

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

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.model.Evento;
import com.bretema.fiestasgalicia.model.Municipio;
import com.bretema.fiestasgalicia.model.Subtipo;
import com.bretema.fiestasgalicia.provider.FiestasContract.Fiestas;
import com.bretema.fiestasgalicia.util.JSONParser;
import com.bretema.fiestasgalicia.util.ServiceConstants;
import com.bretema.fiestasgalicia.view.activities.FiestasVistaListaActivity;

public class FiestasFragment extends SherlockFragment implements OnItemClickListener {

	private static final String		LOG_TAG	= FiestasFragment.class.getSimpleName();

	private FiestaListItemAdapter	mAdapter;
	private GridView				mGrid;


	private ViewHolder				viewHolder;

	private JSONParser				jParser	= new JSONParser();
	public List<Evento>				listaEventos;

	public FiestasFragment() {
		super();
		listaEventos = new ArrayList<Evento>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.fiestas_fragment, container, false);

		new LoadFiestas().execute();
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mGrid = (GridView) getView().findViewById(R.id.showlist);

		mGrid.setOnItemClickListener(this);

		View emptyView = getView().findViewById(R.id.empty);
		if (emptyView != null) {
			mGrid.setEmptyView(emptyView);
		}

		// // start loading data, use saved show filter
		// int showfilter = prefs.getInt(SeriesGuidePreferences.KEY_SHOWFILTER,
		// 0);
		// Bundle args = new Bundle();
		// args.putInt(FILTER_ID, showfilter);
		// getLoaderManager().initLoader(LOADER_ID, args, this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@TargetApi(16)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	private class FiestaListItemAdapter extends BaseAdapter {

		private LayoutInflater	mLayoutInflater;

		private final int		LAYOUT	= R.layout.fiestas_row;

		public FiestaListItemAdapter(Context context) {
			super();
			mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mLayoutInflater.inflate(LAYOUT, null);

				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView.findViewById(R.id.seriesname);
				viewHolder.network = (TextView) convertView.findViewById(R.id.TextViewShowListNetwork);
				viewHolder.episode = (TextView) convertView.findViewById(R.id.TextViewShowListNextEpisode);
				viewHolder.episodeTime = (TextView) convertView.findViewById(R.id.episodetime);
				viewHolder.airsTime = (TextView) convertView.findViewById(R.id.TextViewShowListAirtime);
				viewHolder.poster = (ImageView) convertView.findViewById(R.id.showposter);
				viewHolder.favorited = convertView.findViewById(R.id.favoritedLabel);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Evento e = listaEventos.get(position);
			// set text properties immediately
			viewHolder.name.setText(e.getNombre());
			viewHolder.episode.setText(e.getMunicipio().getNombre());
			viewHolder.episodeTime.setText(e.getSubtipo().getDescripcion());
			// final boolean isFavorited = mCursor.getInt(FiestasQuery.FAVORITE)
			// == 1;
			final boolean isFavorited = true;
			viewHolder.favorited.setVisibility(isFavorited ? View.VISIBLE : View.GONE);

			// next episode info
			String fieldValue = e.getFechaInicio().toString();
			// airday
			// final String[] values = Utils.parseMillisecondsToTime(
			// mCursor.getLong(ShowsQuery.AIRSTIME),
			// mCursor.getString(ShowsQuery.AIRSDAYOFWEEK), mContext);
			// viewHolder.airsTime.setText(values[1] + " " + values[0]);
			
			java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
			
			
			viewHolder.airsTime.setText("Del " + sdf.format(e.getFechaInicio()));

			viewHolder.network.setText("Al " + sdf.format(e.getFechaFin()));
			// set poster
			final String imagePath = e.getImagenPrincipal();
			// ImageProvider.getInstance(mContext).loadPosterThumb(viewHolder.poster,
			// imagePath);

			return convertView;
		}

		@Override
		public int getCount() {
			return listaEventos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static class ViewHolder {

		public TextView		name;

		public TextView		network;

		public TextView		episode;

		public TextView		episodeTime;

		public TextView		airsTime;

		public ImageView	poster;

		public View			favorited;

	}

	/**
	 * Background Async Task to Load all lines by making HTTP Request
	 * */
	class LoadFiestas extends AsyncTask<Void, Void, Boolean> {

		private String	TAG	= LoadFiestas.class.getSimpleName();

		public ProgressDialog			pDialog;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			

			pDialog = new ProgressDialog(getActivity());
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

				Log.d("Server message: ", json.toString());

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
						
						s.setDescripcion(nombreSubtipo);
						
						evento.setSubtipo(s);
						evento.setMunicipio(m);
						evento.setLatitud(latitud);
						evento.setLongitud(longitud);
						// adding HashList to ArrayList
						listaEventos.add(evento);
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
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						/*
						 * ListAdapter adapter = new
						 * LineListAdapter(LineListActivity.this, lineList); //
						 * updating listview setListAdapter(adapter);
						 */

						mAdapter = new FiestaListItemAdapter(getActivity());
						mGrid.setAdapter(mAdapter);

					}
				});
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("No se pudieron obtener los datos");
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						getActivity().finish();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		}

	}

}
