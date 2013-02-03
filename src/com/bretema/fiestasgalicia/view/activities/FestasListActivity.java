
package com.bretema.fiestasgalicia.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bretema.fiestasgalicia.R;
import com.bretema.fiestasgalicia.util.CompatActionBarNavHandler;
import com.bretema.fiestasgalicia.util.CompatActionBarNavListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festas_list);

        FragmentPagerAdapter adapter = new FestasListAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.festasListPager);
        mPager.setAdapter(adapter);

        mIndicator = (TabPageIndicator) findViewById(R.id.festasListPagerIndicator);
        mIndicator.setViewPager(mPager);

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

}
