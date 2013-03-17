package com.android.novel.reader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;
import com.kosbrother.fragments.CategoryNewNovelsFragment;
import com.kosbrother.fragments.CategoryRecommendFragment;
import com.kosbrother.fragments.CategoryWeekFragment;
import com.kosbrother.fragments.CategroyHotNovelsFragment;

public class CategoryActivity extends SherlockFragmentActivity {
	
	private static final int ID_SETTING = 0;
    private static final int ID_RESPONSE = 1;
    private static final int ID_ABOUT_US = 2;
    private static final int ID_GRADE = 3;
    private static final int ID_DOWNLOAD = 4;
    private static final int ID_SEARCH = 5;
	
	private String[] CONTENT;
	private EditText search;
	private Bundle mBundle;
	private String categoryName;
	private int categoryId;
	private MenuItem  itemSearch;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_titles);
        
        
        final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        categoryName = mBundle.getString("CategoryName");
        categoryId = mBundle.getInt("CategoryId");
        
        ab.setTitle(categoryName);
        ab.setDisplayHomeAsUpEnabled(true);
        
        
        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.category_sections);
        
        FragmentPagerAdapter adapter = new NovelPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
		
		menu.add(0, ID_SETTING, 0, "設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_RESPONSE, 1, "意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_ABOUT_US, 2, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_GRADE, 3, "為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		itemSearch = menu.add(0, ID_SEARCH, 4, "搜索").setIcon(R.drawable.ic_search_inverse).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            private EditText search;

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                search = (EditText) item.getActionView();
                search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                search.setInputType(InputType.TYPE_CLASS_TEXT);
                search.requestFocus();
                search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    	if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {                    	  
                          Bundle bundle = new Bundle();
                          bundle.putString("SearchKeyword", v.getText().toString());
                          Intent intent = new Intent();
                          intent.setClass(CategoryActivity.this, SearchActivity.class);
                          intent.putExtras(bundle);
                          startActivity(intent);
                          itemSearch.collapseActionView();
                          return true;
                      }
                        return false;
                    }
                });
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // TODO Auto-generated method stub
                search.setText("");
                return true;
            }
        }).setActionView(R.layout.collapsible_edittext);
		itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        return true;
    }
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    case ID_SETTING: // setting
	    		Intent intent = new Intent(CategoryActivity.this, SettingActivity.class);
	    		startActivity(intent); 
	        break;
	    case ID_RESPONSE: // response
    			Toast.makeText(CategoryActivity.this, "RESPONESE", Toast.LENGTH_SHORT).show();
    		break;
	    case ID_ABOUT_US: // response
			Toast.makeText(CategoryActivity.this, "ABOUT_US", Toast.LENGTH_SHORT).show();
			break;
	    case ID_GRADE: // response
			Toast.makeText(CategoryActivity.this, "GRADE", Toast.LENGTH_SHORT).show();
			break;
	    case ID_SEARCH: // response
			break;	
	    }
	    return true;
	}
    
    class NovelPagerAdapter extends FragmentPagerAdapter {
        public NovelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {        	
        	Fragment kk = new Fragment();
        	if( position == 0){
        		kk = CategroyHotNovelsFragment.newInstance(categoryId);
        	}else if(position == 1){
        		kk = CategoryRecommendFragment.newInstance(categoryId);
        	}else if(position == 2) {
        		kk = CategoryWeekFragment.newInstance(categoryId);
        	}else if(position == 3){
        		kk = CategoryNewNovelsFragment.newInstance(categoryId);
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