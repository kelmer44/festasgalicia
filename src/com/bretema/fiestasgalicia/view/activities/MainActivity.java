package com.bretema.fiestasgalicia.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.bretema.fiestasgalicia.R;

public class MainActivity extends Activity {
	private static final String	LOG_TAG		= MainActivity.class.getSimpleName();
	private static final int	DELAY_TIME	= 200;									// millis

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		Handler handler = new Handler();

		// insertData();
		Runnable r = new Runnable() {

			@Override
			public void run() {
				finish();
				// start the home Screen

				Intent intent = new Intent(MainActivity.this, FestasListActivity.class);
				MainActivity.this.startActivity(intent);

			}
		};
		// run a thread after 2 seconds to start the home screen
		handler.postDelayed(r, DELAY_TIME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
