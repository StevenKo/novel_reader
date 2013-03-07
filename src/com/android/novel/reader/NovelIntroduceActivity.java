package com.android.novel.reader;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Article;
import com.taiwan.imageload.ImageLoader;
import com.taiwan.imageload.ListArticleAdapter;

public class NovelIntroduceActivity extends SherlockFragmentActivity {
	
// 	API: 
//	搜索: searchNovels(String), 
//	取某篇章節: getArticle(Article), 
//	取所有章節: getNovelArticles(int novelId, int page, boolean isOrderUp),
	
	private EditText search;
	private Bundle mBundle;
	private String novelName;
	private int novelId;
	private String novelAuthor;
	private String novelDescription;
	private String novelUpdate;
	private String novelPicUrl;
	private String novelArticleNum;
	private ImageView novelImageView;
	private TextView novelTextName;
	private TextView novelTextAuthor;
	private TextView novelTextDescription;
	private TextView novelTextUpdate;
	private Button novelButton;
	private ImageLoader mImageLoader;
	private LinearLayout novelLayoutProgress;
	private ArrayList<Article> articleList = new ArrayList<Article>();
	private ListView novelListView;
	private ListArticleAdapter mListAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_novel_introduce);
        
        
        final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        novelName = mBundle.getString("NovelName");
        novelId = mBundle.getInt("NovelId");
        novelAuthor = mBundle.getString("NovelAuthor");
        novelDescription = mBundle.getString("NovelDescription");
        novelUpdate = mBundle.getString("NovelUpdate");
        novelPicUrl = mBundle.getString("NovelPicUrl");
        novelArticleNum = mBundle.getString("NovelArticleNum");
        
        ab.setTitle("小說介紹");
        ab.setDisplayHomeAsUpEnabled(true);
        
        setViews();
        new DownloadArticlesTask().execute();
        
    }

	private void setViews() {
		novelImageView = (ImageView) findViewById (R.id.novel_image);
		novelTextName = (TextView) findViewById (R.id.novel_name);
		novelTextAuthor = (TextView) findViewById (R.id.novel_author);
		novelTextDescription = (TextView) findViewById (R.id.novel_description);
		novelTextUpdate = (TextView) findViewById (R.id.novel_update);
		novelListView = (ListView) findViewById (R.id.novel_artiles_list);
		novelButton = (Button) findViewById (R.id.novel_button);
		novelLayoutProgress = (LinearLayout) findViewById (R.id.novel_layout_progress);
		
		novelTextName.setText(novelName + "(" + novelArticleNum + ")");
		novelTextAuthor.setText("作者:"+novelAuthor);
		novelTextDescription.setText(novelDescription);
		novelTextUpdate.setText("更新:"+ novelUpdate );
		
		mImageLoader = new ImageLoader(NovelIntroduceActivity.this, 70);
		mImageLoader.DisplayImage(novelPicUrl, novelImageView);
		
		novelButton.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				if(novelButton.getText().equals("由後往前")){
					novelButton.setText("由前往後");
				}else{
					novelButton.setText("由後往前");
				}
			}
		});
		
		novelListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {														
				Intent intent = new Intent(NovelIntroduceActivity.this, ArticleActivity.class);
				mBundle.putInt("ArticleId", articleList.get(position).getId());
				mBundle.putString("ArticleTitle", articleList.get(position).getTitle());
				intent.putExtras(mBundle);
				startActivity(intent);		
			}
		});
		
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
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    case 0: // setting
	    		Intent intent = new Intent(NovelIntroduceActivity.this, SettingActivity.class);
	    		startActivity(intent); 
	        break;
	        
	        
	    }
	    return true;
	}
	
	 private class DownloadArticlesTask extends AsyncTask {

	        @Override
	        protected Object doInBackground(Object... params) {
	            // TODO Auto-generated method stub
	        	articleList = NovelAPI.getNovelArticles(novelId, 1, true, NovelIntroduceActivity.this);
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Object result) {
	            // TODO Auto-generated method stub
	            super.onPostExecute(result);
	            novelLayoutProgress.setVisibility(View.GONE);
	            if(articleList!= null && articleList.size()!= 0){
		            mListAdapter = new ListArticleAdapter(NovelIntroduceActivity.this, articleList);
		            novelListView.setAdapter(mListAdapter);
	            }

	        }
	 }
    

}
