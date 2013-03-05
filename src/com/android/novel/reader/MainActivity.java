package com.android.novel.reader;




import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends SherlockFragmentActivity {

	private String[] CONTENT;
	private EditText search;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_titles);
        
        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.sections);
        
        FragmentStatePagerAdapter adapter = new NovelPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
		
		menu.add("設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
        menu.add("Search")
        .setIcon(R.drawable.ic_search_inverse)
        .setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
             search = (EditText) item.getActionView();
           	 search.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// TODO Auto-generated method stub
				search.setText("");
				return true;
			}})
        .setActionView(R.layout.collapsible_edittext)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        return true;
    }
    
    class NovelPagerAdapter extends FragmentStatePagerAdapter {
        public NovelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {        	
        	Fragment kk = new Fragment();
        	if( position == 0){
        		kk = CategoryListFragment.newInstance();
        	}else if(position == 1){
        		kk = MyNovelFragment.newInstance();
        	}else{
        		kk = TestListFragment.newInstance();
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
