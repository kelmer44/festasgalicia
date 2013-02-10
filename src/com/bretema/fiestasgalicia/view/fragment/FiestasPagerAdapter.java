package com.bretema.fiestasgalicia.view.fragment;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bretema.fiestasgalicia.model.Evento;

public class FiestasPagerAdapter extends FragmentPagerAdapter {
	private static final int		DEFAULT_TABCOUNT		= 3;

	private static final int		FAVORITOS_TAB_POSITION	= 0;
	private static final int		PROXIMAS_TAB_POSITION	= 1;
	private static final int		CERCANOS_TAB_POSITION	= 2;

	private static final String		LOG_TAG					= FiestasPagerAdapter.class.getSimpleName();

	private static final String[]	CONTENT					= new String[] { "Favoritos", "Población", "Próximas" };

	private Context					mContext;
	List<Evento>					listaEventos;

	public FiestasPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		return new FiestasFragment();
	}

	@Override
	public int getCount() {
		return DEFAULT_TABCOUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case FAVORITOS_TAB_POSITION:
				return "FAVORITOS";
			case PROXIMAS_TAB_POSITION:
				return "PRÓXIMAS";
			case CERCANOS_TAB_POSITION:
				return "CERCANAS";
		}
		return "";
	}

}
