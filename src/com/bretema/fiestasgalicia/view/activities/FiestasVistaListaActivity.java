package com.bretema.fiestasgalicia.view.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.model.Evento;
import com.bretema.fiestasgalicia.model.Subtipo;
import com.bretema.fiestasgalicia.util.CompatActionBarNavHandler;
import com.bretema.fiestasgalicia.util.CompatActionBarNavListener;
import com.bretema.fiestasgalicia.util.JSONParser;
import com.bretema.fiestasgalicia.util.ServiceConstants;
import com.bretema.fiestasgalicia.view.fragment.FiestasPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class FiestasVistaListaActivity extends BaseActivity implements CompatActionBarNavListener {

	private final static String	LOG_TAG	= FiestasVistaListaActivity.class.getSimpleName();
	// Pager
	private ViewPager			mPager;
	// Indicator
	private TabPageIndicator	mIndicator;

	private JSONParser			jParser	= new JSONParser();
	public List<Subtipo>		subtipoList;
	public List<Evento>			listaEventos;
	private FiestasPagerAdapter	mFiestasAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_festas_list);

		mFiestasAdapter = new FiestasPagerAdapter(getSupportFragmentManager(), this);

		subtipoList = new ArrayList<Subtipo>();
		listaEventos = new ArrayList<Evento>();

		// View pager indicator
		mPager = (ViewPager) findViewById(R.id.festasListPager);
		//mPager.setAdapter(mFiestasAdapter);

		// set up action bar
		setupActionBar();

		// set up view pager
		onChangePagerAdapter(0);

		mIndicator = (TabPageIndicator) findViewById(R.id.festasListPagerIndicator);
		mIndicator.setViewPager(mPager);

		new LoadCategoryData().execute();
		// new LoadFiestas().execute();

	}

	private void setupActionBar() {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);

		/* setup navigation */
		CompatActionBarNavHandler handler = new CompatActionBarNavHandler(this);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<CharSequence> mActionBarList = ArrayAdapter.createFromResource(this, R.array.showfilter_list, R.layout.sherlock_spinner_item);
		mActionBarList.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(mActionBarList, handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.festas_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menu_search) {
			onSearchRequested();
			return true;
		} 
		else if (itemId == R.id.menu_map){
			Intent in = new Intent(this, FiestasMapActivity.class);
			startActivity(in);
			return true;
		}		
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCategorySelected(int catIndex) {
		// TODO Auto-generated method stub

	}


	private void onChangePagerAdapter(int navItem) {

		mPager.setAdapter(mFiestasAdapter);
	}
	
	/**
	 * Background Async Task to Load all lines by making HTTP Request
	 * */
	class LoadCategoryData extends AsyncTask<Void, Void, Boolean> {

		private String	TAG	= LoadCategoryData.class.getSimpleName();

		private ProgressDialog pDialog;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(FiestasVistaListaActivity.this);
			pDialog.setMessage("Cargando datos... por favor, espere.");
			pDialog.setIndeterminate(false);
			pDialog.setTitle("Obteniendo información");
			pDialog.setCancelable(true);
			pDialog.show();

			pDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					LoadCategoryData.this.cancel(true);
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
				JSONObject json = jParser.makeHttpRequest(ServiceConstants.url_subtipoevento, "GET", params);

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
					JSONArray subtipos = dataObject.getJSONArray(ServiceConstants.TAG_ARRAY_SUBTITPO);

					// looping through All Products
					for (int i = 0; i < subtipos.length(); i++) {
						JSONObject c = subtipos.getJSONObject(i);

						// Storing each json item in variable
						int id = c.getInt(ServiceConstants.TAG_SUBTIPO_ID_SUBTIPO);
						String desc = "";
						try {
							desc = URLDecoder.decode(c.getString(ServiceConstants.TAG_SUBTIPO_DESC_SUBTIPO_EVENTO), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// adding HashList to ArrayList
						subtipoList.add(new Subtipo(id, desc));
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
			FiestasVistaListaActivity.this.finish();
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

					}
				});
			} else {
				/*AlertDialog.Builder builder = new AlertDialog.Builder(FiestasVistaListaActivity.this);
				builder.setMessage("No se pudieron obtener los datos");
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//FiestasVistaListaActivity.this.finish();
						
					}
				});
				AlertDialog alert = builder.create();
				alert.show();*/
			}

		}

	}

}
