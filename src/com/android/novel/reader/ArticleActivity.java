package com.android.novel.reader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Article;
import com.kosbrother.tool.DetectScrollView;
import com.kosbrother.tool.DetectScrollView.DetectScrollViewListener;

public class ArticleActivity extends SherlockFragmentActivity implements DetectScrollViewListener {
	

	private Bundle mBundle;
	private String novelName;
	private String articleTitle;
	private int articleId;
	private TextView articleTextView;
	private DetectScrollView articleScrollView;
	private Button articleButtonUp;
	private Button articleButtonDown;
	private TextView articlePercent;
	private Article myAricle; // uset to get article text
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_article);      
        setViews();
        
        final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        novelName = mBundle.getString("NovelName");
        articleTitle = mBundle.getString("ArticleTitle");
        articleId = mBundle.getInt("ArticleId");
        
        ab.setTitle(novelName);
        ab.setDisplayHomeAsUpEnabled(true);
        
        myAricle = new Article(articleId, 0, "","","",false);
        
        new DownloadArticleTask().execute();
        
    }

	private void setViews() {
		// TODO Auto-generated method stub
		articleTextView = (TextView) findViewById (R.id.article_text);
        articleScrollView = (DetectScrollView) findViewById (R.id.article_scrollview);
        articleButtonUp = (Button) findViewById (R.id.article_button_up);
        articleButtonDown = (Button) findViewById (R.id.article_button_down);
        articlePercent = (TextView) findViewById (R.id.article_percent);
        
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
        
        articleScrollView.setScrollViewListener(ArticleActivity.this);
        
	}
	
	@Override
	public void onScrollChanged(DetectScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		int kk = articleScrollView.getHeight();
		int tt = articleTextView.getHeight();
		
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
        }
	}

	

}