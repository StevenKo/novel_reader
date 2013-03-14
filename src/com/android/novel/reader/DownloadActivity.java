package com.android.novel.reader;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Article;
import com.taiwan.imageload.ExpandListDownLoadAdapter;

public class DownloadActivity extends SherlockFragmentActivity {
	
// 	API: 
//	搜索: searchNovels(String), 
//	取某篇章節: getArticle(Article), 
//	取所有章節: getNovelArticles(int novelId, int page, boolean isOrderUp),
	
	private static final int ID_SELECT_ALL = 0;
	private static final int ID_SELECT_NONE = 1;
    
   
	private Bundle mBundle;
	private String novelName;
	private int novelId;
	private Button downLoadButton;
	public static TextView downLoadCountText;
	private LinearLayout novelLayoutProgress;
	private ArrayList<Article> articleList = new ArrayList<Article>();
	private ExpandableListView novelListView;
	
	private TreeMap<String, ArrayList<Article>> myData = new  TreeMap<String, ArrayList<Article>>();
	private ArrayList<String> groupTitleList = new ArrayList<String>();
	
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download);
        
        
        final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        novelName = mBundle.getString("NovelName");
        novelId = mBundle.getInt("NovelId");
        
        ab.setTitle(novelName);
        ab.setDisplayHomeAsUpEnabled(true);
        
        setViews();
        
        new DownloadArticlesTask().execute();
        
    }

	private void setViews() {
		
		novelLayoutProgress = (LinearLayout) findViewById (R.id.novel_layout_progress);
		novelListView = (ExpandableListView) findViewById (R.id.download_artiles_list);
		downLoadButton = (Button) findViewById (R.id.download_button);
		downLoadCountText = (TextView) findViewById (R.id.download_count_text);
		
		downLoadButton.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
			}
		});
	}	
		

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
		
		
		menu.add(0, ID_SELECT_ALL, 0, "全選").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, ID_SELECT_NONE, 1, "全取消").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
        
        return true;
    }
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    case ID_SELECT_ALL: // setting
	    		Toast.makeText(DownloadActivity.this, "全選", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_SELECT_NONE: // response
    			Toast.makeText(DownloadActivity.this, "全部取消", Toast.LENGTH_SHORT).show();
    		break;
	    }
	    return true;
	}
	
	 private class DownloadArticlesTask extends AsyncTask {

	        @Override
	        protected Object doInBackground(Object... params) {
	            // TODO Auto-generated method stub
	        	articleList = NovelAPI.getNovelArticles(novelId, true, DownloadActivity.this);
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Object result) {
	            // TODO Auto-generated method stub
	            super.onPostExecute(result);
	            novelLayoutProgress.setVisibility(View.GONE);
	            if(articleList!= null && articleList.size()!= 0){
	            	
	            	// use HashMap || TreeMap to make a parent key
	            	for(int i=0; i<articleList.size(); i++){
	            		if(myData.containsKey(articleList.get(i).getSubject())){
	            			myData.get(articleList.get(i).getSubject()).add(articleList.get(i));
	            		}else{
	            			groupTitleList.add(articleList.get(i).getSubject());
	            			myData.put(articleList.get(i).getSubject(), new ArrayList<Article>());
	            			myData.get(articleList.get(i).getSubject()).add(articleList.get(i));
	            		}
	            	}
	            		            	
	            	Set<String> keyArray =  myData.keySet();
	            	keyArray.size();
	            		            	
	            	ExpandListDownLoadAdapter mAdapter = new ExpandListDownLoadAdapter(DownloadActivity.this,myData, groupTitleList, novelName);
	            	novelListView.setAdapter(mAdapter);
	            	
	            }

	        }
	 }
    

}