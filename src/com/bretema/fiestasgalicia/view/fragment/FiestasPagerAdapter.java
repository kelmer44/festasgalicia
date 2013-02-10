package com.bretema.fiestasgalicia.view.fragment;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.bretema.fiestasgalicia.model.Evento;

public class FiestasPagerAdapter extends MultiPagerAdapter {

	private static final String	LOG_TAG	= FiestasPagerAdapter.class.getSimpleName();

	private static final String[]	CONTENT	= new String[] { "Favoritos", "Población", "Próximas" };
	
	private Context				mContext;
	List<Evento>				listaEventos;

	public FiestasPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		//return ShowsFragment.newInstance();
		//return null;
		return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
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
	public int getItemPosition(Object object) {
		/*if (object instanceof ShowsFragment) {
			return POSITION_UNCHANGED;
		} else {
			return POSITION_NONE;
		}*/
		return 1;
	}

}
