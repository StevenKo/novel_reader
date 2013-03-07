package com.android.novel.reader;

import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class SettingActivity extends SherlockFragmentActivity {
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        
        
        final ActionBar ab = getSupportActionBar();		             
        ab.setTitle("設定");
        ab.setDisplayHomeAsUpEnabled(true);
        
        
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);    
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

}