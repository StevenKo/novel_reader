package com.android.novel.reader;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.novel.reader.R;
import com.android.novel.reader.R.drawable;
import com.android.novel.reader.R.id;
import com.android.novel.reader.R.layout;
import com.android.novel.reader.R.string;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Article;
import com.android.novel.reader.entity.Novel;
import com.kosbrother.tool.Group;
import com.taiwan.imageload.ImageLoader;
import com.taiwan.imageload.ListArticleAdapter;

public class MyDownloadArticleActivity extends SherlockFragmentActivity {
	
	private static final int ID_SETTING = 0;
    private static final int ID_RESPONSE = 1;
    private static final int ID_ABOUT_US = 2;
    private static final int ID_GRADE = 3;
    private static final int ID_DELETE_DOWNLOAD = 4;

  	
	private Bundle mBundle;
	private String novelName;
	private int novelId;
	private String novelAuthor;
	private String novelPicUrl;
	private String novelArticleNum;
	private ImageView novelImageView;
	private TextView novelTextName;
	private TextView novelTextAuthor;
	private TextView downloadedCount;
	private ImageLoader mImageLoader;
	private LinearLayout novelLayoutProgress;
	private ArrayList<Article> articleList = new ArrayList<Article>();
	private ListView novelListView;
	
	private ArrayList<Group> mGroups = new ArrayList<Group>();
	private AlertDialog.Builder deleteDialog;
	private AlertDialog.Builder aboutUsDialog;	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_novel_downloaded);
        
        
        final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        novelName = mBundle.getString("NovelName");
        novelId = mBundle.getInt("NovelId");
        novelAuthor = mBundle.getString("NovelAuthor");
        novelPicUrl = mBundle.getString("NovelPicUrl");
        novelArticleNum = mBundle.getString("NovelArticleNum");
        
        ab.setTitle(" 我的下載");
        ab.setDisplayHomeAsUpEnabled(true);
        
        setViews();
        setAboutUsDialog();
        
        new DownloadArticlesTask().execute();
        
    }

	private void setViews() {
		novelImageView = (ImageView) findViewById (R.id.novel_image);
		novelTextName = (TextView) findViewById (R.id.novel_name);
		novelTextAuthor = (TextView) findViewById (R.id.novel_author);
		novelListView = (ListView) findViewById (R.id.novel_download_artiles_list);
		downloadedCount =  (TextView) findViewById (R.id.text_downloaded_count);
		novelLayoutProgress = (LinearLayout) findViewById (R.id.novel_layout_progress);
		
		novelTextName.setText(novelName + "(" + novelArticleNum + ")");
		novelTextAuthor.setText("作者:"+novelAuthor);
		
		mImageLoader = new ImageLoader(MyDownloadArticleActivity.this, 70);
		mImageLoader.DisplayImage(novelPicUrl, novelImageView);
		
		deleteDialog = new AlertDialog.Builder(this).setTitle("刪除")
				.setMessage("是否刪除此小說")
				.setIcon(R.drawable.app_icon)
				.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Novel myNovel = new Novel(novelId, novelName, novelAuthor, "", novelPicUrl, 0, novelArticleNum, "", false, false,true);
						NovelAPI.removeNovelFromDownload(myNovel, MyDownloadArticleActivity.this);
				}
				})
				.setNegativeButton("不刪除", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
				}
		});
		
	}
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_main, menu);
	
		menu.add(0, ID_SETTING, 0, "設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_RESPONSE, 1, "意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_ABOUT_US, 2, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_GRADE, 3, "為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_DELETE_DOWNLOAD, 5, "刪除").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
	    		Intent intent = new Intent(MyDownloadArticleActivity.this, SettingActivity.class);
	    		startActivity(intent); 
	        break;
	    case ID_RESPONSE: // response
	    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	    	emailIntent.setType("plain/text");
	    	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"brotherkos@gmail.com"});
	    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "意見回餽 from 小說王");
	    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
	    	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    		break;
	    case ID_ABOUT_US: // response
	    	aboutUsDialog.show();
			break;
	    case ID_GRADE: // response
	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=KosBrother"));
			startActivity(browserIntent);
			break;
	    case ID_DELETE_DOWNLOAD: // response
	    	deleteDialog.show();
			break;
	    	
	    }
	    return true;
	}
	
	 private class DownloadArticlesTask extends AsyncTask {

	        @Override
	        protected Object doInBackground(Object... params) {	            
	        	articleList = NovelAPI.getDownloadedNovelArticles(novelId, false, MyDownloadArticleActivity.this);
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Object result) {
	          
	            super.onPostExecute(result);
	            novelLayoutProgress.setVisibility(View.GONE);
	            if(articleList!= null && articleList.size()!= 0){
	            	
	            	ListArticleAdapter mAdapter = new ListArticleAdapter( MyDownloadArticleActivity.this, articleList, novelName);
	            	novelListView.setAdapter(mAdapter);
	            
	            }
	            String txtCount = Integer.toString(articleList.size());
	            downloadedCount.setText("共 "+txtCount+" 個下載");

	        }
	 }
    
	 private void setAboutUsDialog() {
			// TODO Auto-generated method stub
	    	aboutUsDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.about_us_string))
					.setIcon(R.drawable.play_store_icon)
					.setMessage(getResources().getString(R.string.about_us))
					.setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
		}

}
