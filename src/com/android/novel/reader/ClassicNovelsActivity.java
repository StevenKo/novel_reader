package com.android.novel.reader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ClassicNovelsActivity extends SherlockFragmentActivity {

	private Bundle mBundle;
	private String title;
	private int classicInt;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_classic);

        final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        title = mBundle.getString("ClassTitle");
        classicInt = mBundle.getInt("ClassicId");
        
        ab.setTitle(title);
        ab.setDisplayHomeAsUpEnabled(true);

        FragmentStatePagerAdapter adapter = new ClassicNovelPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.classic_pager);
        pager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);

        menu.add("設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    class ClassicNovelPagerAdapter extends FragmentStatePagerAdapter {
        public ClassicNovelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment kk = new Fragment();
            if (position == 0) {
            	kk = ClassicFragment.newInstance(classicInt);
            } 
            return kk;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

}
