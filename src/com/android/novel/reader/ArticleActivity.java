package com.android.novel.reader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.api.Setting;
import com.android.novel.reader.entity.Article;
import com.android.novel.reader.entity.Bookmark;
import com.kosbrother.tool.DetectScrollView;
import com.kosbrother.tool.DetectScrollView.DetectScrollViewListener;

public class ArticleActivity extends SherlockFragmentActivity implements DetectScrollViewListener {
	
	private static final int ID_SETTING = 0;
    private static final int ID_RESPONSE = 1;
    private static final int ID_ABOUT_US = 2;
    private static final int ID_GRADE = 3;
    private static final int ID_Bookmark = 4;

	private int textSize;
	private int textLanguage; // 0 for 繁體, 1 for 簡體
	private int readingDirection; // 0 for 直向, 1 for 橫向
	private int clickToNextPage; // 0 for yes, 1 for no
	private int stopSleeping;  // 0 for yes, 1 for no
	
	private TextView articleTextView;
	private DetectScrollView articleScrollView;
	private Button articleButtonUp;
	private Button articleButtonDown;
	private TextView articlePercent;
	private Article myAricle; // uset to get article text
	private Boolean downloadBoolean;	
	private AlertDialog.Builder addBookMarkDialog;
	private Bundle mBundle;
	private String novelName;
	private String articleTitle;
	private int articleId;
	private String novelPic;
	private int novelId;
	private int yRate;
	private Bookmark mBookmark;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_article); 
        restorePreValues();
        setViews();
        
        final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        novelName = mBundle.getString("NovelName");
        articleTitle = mBundle.getString("ArticleTitle");
        articleId = mBundle.getInt("ArticleId");
        downloadBoolean = mBundle.getBoolean("ArticleDownloadBoolean",false);
        novelPic = mBundle.getString("NovelPic");
        novelId = mBundle.getInt("NovelId");
        yRate = mBundle.getInt("ReadingRate",0);
        
        ab.setTitle(novelName);
        ab.setDisplayHomeAsUpEnabled(true);
        
        if(downloadBoolean){
        	myAricle = new Article(articleId, 0, "","","",true);
        }else{
        	myAricle = new Article(articleId, 0, "","","",false);
        }
        
        new DownloadArticleTask().execute();
        
        
    }

	private void restorePreValues() {
		// TODO Auto-generated method stub
		textSize = Setting.getSetting(Setting.keyTextSize, ArticleActivity.this);
        textLanguage = Setting.getSetting(Setting.keyTextLanguage, ArticleActivity.this);
        readingDirection = Setting.getSetting(Setting.keyReadingDirection, ArticleActivity.this);
        clickToNextPage = Setting.getSetting(Setting.keyClickToNextPage, ArticleActivity.this);
        stopSleeping = Setting.getSetting(Setting.keyStopSleeping, ArticleActivity.this);
        
        if(readingDirection == 0){
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else if (readingDirection == 1){
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        
        if(stopSleeping == 0){
        	ArticleActivity.this.findViewById(android.R.id.content).setKeepScreenOn(true);
        }
	}

	private void setViews() {
		// TODO Auto-generated method stub
		articleTextView = (TextView) findViewById (R.id.article_text);
        articleScrollView = (DetectScrollView) findViewById (R.id.article_scrollview);
        articleButtonUp = (Button) findViewById (R.id.article_button_up);
        articleButtonDown = (Button) findViewById (R.id.article_button_down);
        articlePercent = (TextView) findViewById (R.id.article_percent);
        
        articleScrollView.setScrollViewListener(ArticleActivity.this);
        
        articleTextView.setTextSize(textSize);
        
        articleButtonUp.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				
			}
		});
        
        articleButtonDown.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				
			}
		});
        
        
        if(clickToNextPage == 0){
        	articleTextView.setOnClickListener(new OnClickListener() {			 
    			@Override
    			public void onClick(View arg0) {
    				
    			}
    		});
        }
        
        addBookMarkDialog = new AlertDialog.Builder(this).setTitle("加書籤")
				.setMessage("要加入書籤嗎?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 NovelAPI.insertBookmark(new Bookmark(0, novelId, articleId, yRate, novelName, articleTitle, novelPic, true), ArticleActivity.this);
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
		});
        
	}
	
	@Override
	public void onScrollChanged(DetectScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		int kk = articleScrollView.getHeight();
		int tt = articleTextView.getHeight();
		
		yRate = (int)(((double)(y)/(double)(tt))*100);
		int xx = (int)(((double)(y+kk)/(double)(tt))*100);
		String yPositon = Integer.toString(xx);
		articlePercent.setText(yPositon+"%");
	}
	
	
	public static int dip2px(Context context, float dpValue) {
  	  final float scale = context.getResources().getDisplayMetrics().density;
  	  return (int) (dpValue * scale + 0.5f);
  	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
		
		menu.add(0, ID_SETTING, 0, "設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_RESPONSE, 1, "意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_ABOUT_US, 2, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_GRADE, 3, "為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_Bookmark, 4, "加入書籤").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
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
	    		Intent intent = new Intent(ArticleActivity.this, SettingActivity.class);
	    		startActivity(intent); 
	        break;
	    case ID_RESPONSE: // response
    			Toast.makeText(ArticleActivity.this, "RESPONESE", Toast.LENGTH_SHORT).show();
    		break;
	    case ID_ABOUT_US: // response
			Toast.makeText(ArticleActivity.this, "ABOUT_US", Toast.LENGTH_SHORT).show();
			break;
	    case ID_GRADE: // response
			Toast.makeText(ArticleActivity.this, "GRADE", Toast.LENGTH_SHORT).show();
			break;
	    case ID_Bookmark: // response
	    	addBookMarkDialog.show();
			break;
	    }
	    return true;
	}
	
	private class DownloadArticleTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
        	Article theArticle = NovelAPI.getArticle(myAricle, ArticleActivity.this);
        	myAricle = theArticle;
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            
            articleTextView.setText(articleTitle + "\n\n" + myAricle.getText());           
            new GetLastPositionTask().execute();
       
            
        }
	}

	private class GetLastPositionTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            
           
            int kk = articleScrollView.getHeight();
    		int tt = articleTextView.getHeight();
    		
    		if(yRate == 0){
    			int xx = (int)(((double)(kk)/(double)(tt))*100);
    			String yPositon = Integer.toString(xx);
    			articlePercent.setText(yPositon+"%");
    		}else{  	
    			String yPositon = Integer.toString(yRate);
    			articlePercent.setText(yPositon+"%");
    			articleScrollView.scrollTo(0, yRate*tt/100);
    		}
            
        }
	}
	
	@Override
	 protected void onResume() {
		super.onResume();
		textSize = Setting.getSetting(Setting.keyTextSize, ArticleActivity.this);
        textLanguage = Setting.getSetting(Setting.keyTextLanguage, ArticleActivity.this);
        readingDirection = Setting.getSetting(Setting.keyReadingDirection, ArticleActivity.this);
        clickToNextPage = Setting.getSetting(Setting.keyClickToNextPage, ArticleActivity.this);
        stopSleeping = Setting.getSetting(Setting.keyStopSleeping, ArticleActivity.this);
        
		articleTextView.setTextSize(textSize);
		if(stopSleeping == 0){
        	ArticleActivity.this.findViewById(android.R.id.content).setKeepScreenOn(true);
        }		
	 }
	
}