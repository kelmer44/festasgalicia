
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
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.util.CompatActionBarNavHandler;
import com.bretema.fiestasgalicia.util.CompatActionBarNavListener;
import com.bretema.fiestasgalicia.util.JSONParser;
import com.bretema.fiestasgalicia.util.ServiceConstants;
import com.bretema.fiestasgalicia.view.fragment.TestFragment;
import com.viewpagerindicator.TabPageIndicator;

public class FestasListActivity extends BaseActivity implements CompatActionBarNavListener{

    private final static String LOG_TAG = FestasListActivity.class.getSimpleName();
    private static final String[] CONTENT = new String[] {
            "Favoritos", "Población", "Próximas"
    };
    // Pager
    private ViewPager mPager;
    // Indicator
    private TabPageIndicator mIndicator;
	
    
    public ProgressDialog	pDialog;

	private JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festas_list);

        FragmentPagerAdapter adapter = new FestasListAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.festasListPager);
        mPager.setAdapter(adapter);

        mIndicator = (TabPageIndicator) findViewById(R.id.festasListPagerIndicator);
        mIndicator.setViewPager(mPager);

        pDialog = new ProgressDialog(this);
        
        
        setupActionBar();
    }

    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        /* setup navigation */
        CompatActionBarNavHandler handler = new CompatActionBarNavHandler(this);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<CharSequence> mActionBarList = ArrayAdapter.createFromResource(this,
                R.array.showfilter_list, R.layout.sherlock_spinner_item);
        mActionBarList.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(mActionBarList, handler);
    }

    public class FestasListAdapter extends FragmentPagerAdapter {

        public FestasListAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public Fragment getItem(int position) {
            return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        }

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
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCategorySelected(int catIndex) {
        // TODO Auto-generated method stub
        
    }

    
    /**
	 * Background Async Task to Load all lines by making HTTP Request
	 * */
	class LoadCategoryData extends AsyncTask<Void, Void, Boolean> {

		private String TAG = LoadCategoryData.class.getSimpleName();
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
				Log.d("All Subtipos: ", json.toString());
				// Checking for SUCCESS TAG
				boolean success = json.getBoolean(ServiceConstants.TAG_SUCCESS);

				if (success) {
					// products found
					// Getting Array of Products

					// lines JSONArray
					JSONArray lineas = json.getJSONArray(ServiceConstants.TAG_LINEAS);

					// looping through All Products
					for (int i = 0; i < lineas.length(); i++) {
						JSONObject c = lineas.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(ServiceConstants.TAG_ID_LINEA);
						String number = c.getString(ServiceConstants.TAG_NUMERO_LINEA);
						String name = "";
						try {
							name = URLDecoder.decode(c.getString(ServiceConstants.TAG_NOMBRE_LINEA), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Linea linea = new Linea();
						
						linea.setId(Integer.parseInt(id));
						linea.setNombre(name);
						linea.setNumero(number);
						
						

						// adding HashList to ArrayList
						lineList.add(linea);
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
	            //Toast.makeText(LineListActivity.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
	            LineListActivity.this.finish();
	        }

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Boolean result) {

			// dismiss the dialog after processing
			pDialog.dismiss();
			if(result){
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						ListAdapter adapter = new LineListAdapter(LineListActivity.this, lineList);
						// updating listview
						setListAdapter(adapter);
						
					}
				});
			}
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(LineListActivity.this);
				builder.setMessage(getResources().getString(R.string.errorfetching));
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	LineListActivity.this.finish();
		            }
		        }
				);
				AlertDialog alert = builder.create();
				alert.show();
			}
				
		}

	}
}
