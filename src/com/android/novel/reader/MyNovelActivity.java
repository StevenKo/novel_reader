package com.android.novel.reader;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

public class MyNovelActivity extends SherlockFragmentActivity {
	
    private String[] CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_titles);
        
        final ActionBar ab = getSupportActionBar();
        ab.setTitle(" 我的書櫃");
        ab.setDisplayHomeAsUpEnabled(true);
        
        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.collections);

        FragmentStatePagerAdapter adapter = new NovelPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

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
    
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;	    	
	    }
	    return true;
	}

    class NovelPagerAdapter extends FragmentStatePagerAdapter {
        public NovelPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        
        // 這邊要寫兩個不同的Fragment, 分別取書櫃資 & 下載資料
        @Override
        public Fragment getItem(int position) {
            Fragment kk = new Fragment();
            if (position == 0) {
            	kk = MyBookcaseFragment.newInstance();
            } else if (position == 1) {
                kk = MyDownloadFragment.newInstance();
            }
            return kk;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

}
