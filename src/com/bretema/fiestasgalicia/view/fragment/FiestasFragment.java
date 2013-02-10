package com.bretema.fiestasgalicia.view.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.battlelancer.seriesguide.provider.SeriesContract.Shows;
import com.bretema.fiestasgalicia.R;

public class FiestasFragment extends SherlockFragment implements
	LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {

    
	private static final String LOG_TAG = FiestasFragment.class.getSimpleName();

    public static final int LOADER_ID = R.layout.shows_fragment;

    public static final String FILTER_ID = "filterid";
    
    
    // Show Filter Ids
    private static final int SHOWFILTER_ALL = 0;

    private static final int SHOWFILTER_FAVORITES = 1;

    private static final int SHOWFILTER_UNSEENEPISODES = 2;

    private static final int SHOWFILTER_HIDDEN = 3;
    
    
    
	private SlowAdapter mAdapter;
    private GridView mGrid;

    
	public static FiestasFragment newInstance(){
		FiestasFragment f = new FiestasFragment();
		return f;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fiestas_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	
    	mGrid = (GridView) getView().findViewById(R.id.showlist);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(this);
        
        View emptyView = getView().findViewById(R.id.empty);
        if (emptyView != null) {
            mGrid.setEmptyView(emptyView);
        }
        
//        // start loading data, use saved show filter
//        int showfilter = prefs.getInt(SeriesGuidePreferences.KEY_SHOWFILTER, 0);
//        Bundle args = new Bundle();
//        args.putInt(FILTER_ID, showfilter);
//        getLoaderManager().initLoader(LOADER_ID, args, this);
    }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	 private class SlowAdapter extends CursorAdapter {

	        private LayoutInflater mLayoutInflater;

	        private final int LAYOUT = R.layout.fiestas_row;

	        public SlowAdapter(Context context, Cursor c, int flags) {
	            super(context, c, flags);
	            mLayoutInflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            if (!mDataValid) {
	                throw new IllegalStateException(
	                        "this should only be called when the cursor is valid");
	            }
	            if (!mCursor.moveToPosition(position)) {
	                throw new IllegalStateException("couldn't move cursor to position " + position);
	            }

	            final ViewHolder viewHolder;

	            if (convertView == null) {
	                convertView = mLayoutInflater.inflate(LAYOUT, null);

	                viewHolder = new ViewHolder();
	                viewHolder.name = (TextView) convertView.findViewById(R.id.seriesname);
	                viewHolder.network = (TextView) convertView
	                        .findViewById(R.id.TextViewShowListNetwork);
	                viewHolder.episode = (TextView) convertView
	                        .findViewById(R.id.TextViewShowListNextEpisode);
	                viewHolder.episodeTime = (TextView) convertView.findViewById(R.id.episodetime);
	                viewHolder.airsTime = (TextView) convertView
	                        .findViewById(R.id.TextViewShowListAirtime);
	                viewHolder.poster = (ImageView) convertView.findViewById(R.id.showposter);
	                viewHolder.favorited = convertView.findViewById(R.id.favoritedLabel);

	                convertView.setTag(viewHolder);
	            } else {
	                viewHolder = (ViewHolder) convertView.getTag();
	            }

	            // set text properties immediately
	            viewHolder.name.setText(mCursor.getString(ShowsQuery.TITLE));
	            viewHolder.network.setText(mCursor.getString(ShowsQuery.NETWORK));

	            final boolean isFavorited = mCursor.getInt(ShowsQuery.FAVORITE) == 1;
	            viewHolder.favorited.setVisibility(isFavorited ? View.VISIBLE : View.GONE);

	            // next episode info
	            String fieldValue = mCursor.getString(ShowsQuery.NEXTTEXT);
	            if (fieldValue.length() == 0) {
	                // show show status if there are currently no more
	                // episodes
	                int status = mCursor.getInt(ShowsQuery.STATUS);

	                // Continuing == 1 and Ended == 0
	                if (status == 1) {
	                    viewHolder.episodeTime.setText(getString(R.string.show_isalive));
	                } else if (status == 0) {
	                    viewHolder.episodeTime.setText(getString(R.string.show_isnotalive));
	                } else {
	                    viewHolder.episodeTime.setText("");
	                }
	                viewHolder.episode.setText("");
	            } else {
	                viewHolder.episode.setText(fieldValue);
	                fieldValue = mCursor.getString(ShowsQuery.NEXTAIRDATETEXT);
	                viewHolder.episodeTime.setText(fieldValue);
	            }

	            // airday
	            final String[] values = Utils.parseMillisecondsToTime(
	                    mCursor.getLong(ShowsQuery.AIRSTIME),
	                    mCursor.getString(ShowsQuery.AIRSDAYOFWEEK), mContext);
                viewHolder.airsTime.setText(values[1] + " " + values[0]);


	            // set poster
	            final String imagePath = mCursor.getString(ShowsQuery.POSTER);
	            ImageProvider.getInstance(mContext).loadPosterThumb(viewHolder.poster, imagePath);

	            return convertView;
	        }

	        @Override
	        public void bindView(View view, Context context, Cursor cursor) {
	            // do nothing here
	        }

	        @Override
	        public View newView(Context context, Cursor cursor, ViewGroup parent) {
	            return mLayoutInflater.inflate(LAYOUT, parent, false);
	        }
	    }
	
	    static class ViewHolder {

	        public TextView name;

	        public TextView network;

	        public TextView episode;

	        public TextView episodeTime;

	        public TextView airsTime;

	        public ImageView poster;

	        public View favorited;

	    }
	    
	    private interface ShowsQuery {

	        String[] PROJECTION = {
	                BaseColumns._ID, Shows.TITLE, Shows.NEXTTEXT, Shows.AIRSTIME, Shows.NETWORK,
	                Shows.POSTER, Shows.AIRSDAYOFWEEK, Shows.STATUS, Shows.NEXTAIRDATETEXT,
	                Shows.FAVORITE, Shows.NEXTEPISODE, Shows.IMDBID
	        };

	        // int _ID = 0;

	        int TITLE = 1;

	        int NEXTTEXT = 2;

	        int AIRSTIME = 3;

	        int NETWORK = 4;

	        int POSTER = 5;

	        int AIRSDAYOFWEEK = 6;

	        int STATUS = 7;

	        int NEXTAIRDATETEXT = 8;

	        int FAVORITE = 9;

	        int NEXTEPISODE = 10;

	        int IMDB_ID = 11;
	    }
}
