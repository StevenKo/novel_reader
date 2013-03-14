package com.android.novel.reader;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosbrother.tool.ChildArticle;
import com.kosbrother.tool.Group;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Article;
import com.android.novel.reader.entity.Novel;
import com.taiwan.imageload.ExpandListAdapter;
import com.taiwan.imageload.ImageLoader;

public class NovelIntroduceActivity extends SherlockFragmentActivity {
	
	private static final int ID_SETTING = 0;
    private static final int ID_RESPONSE = 1;
    private static final int ID_ABOUT_US = 2;
    private static final int ID_GRADE = 3;
    private static final int ID_DOWNLOAD = 4;
    private static final int ID_SEARCH = 5;
  	
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
	private LinearLayout layoutTextArrow;
	private CheckBox checkBoxAddBookcase;
	private ImageView imageArrow;
	private ArrayList<Article> articleList = new ArrayList<Article>();
	private ExpandableListView novelListView;
	private Novel theNovel;
	private Boolean descriptionExpand = false;
	private Boolean isNovelChecked;
	
	private TreeMap<String, ArrayList<Article>> myData = new  TreeMap<String, ArrayList<Article>>();
//	private ArrayList<String> groupTitleList = new ArrayList<String>();
	private ArrayList<Group> mGroups = new ArrayList<Group>();
	
	
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
        
        isNovelChecked = NovelAPI.isNovelCollected(NovelIntroduceActivity.this, novelId);
        
        setViews();
        
        new DownloadNovelTask().execute();
        new DownloadArticlesTask().execute();
        
    }

	private void setViews() {
		novelImageView = (ImageView) findViewById (R.id.novel_image);
		novelTextName = (TextView) findViewById (R.id.novel_name);
		novelTextAuthor = (TextView) findViewById (R.id.novel_author);
		novelTextDescription = (TextView) findViewById (R.id.novel_description);
		novelTextUpdate = (TextView) findViewById (R.id.novel_update);
		novelListView = (ExpandableListView) findViewById (R.id.novel_artiles_list);
		novelButton = (Button) findViewById (R.id.novel_button);
		novelLayoutProgress = (LinearLayout) findViewById (R.id.novel_layout_progress);
		layoutTextArrow = (LinearLayout) findViewById (R.id.layout_text_arrow);
		imageArrow = (ImageView) findViewById (R.id.image_arrow);
		checkBoxAddBookcase = (CheckBox) findViewById (R.id.checkbox_add_bookcase);
		if (isNovelChecked){
			checkBoxAddBookcase.setChecked(true);
		}else{
			checkBoxAddBookcase.setChecked(true);
		}
		
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
					reverseMGroups();
					ExpandListAdapter mAdapter = new ExpandListAdapter( NovelIntroduceActivity.this, mGroups, novelName);
	            	novelListView.setAdapter(mAdapter);
				}else{
					novelButton.setText("由後往前");
					reverseMGroups();	
					ExpandListAdapter mAdapter = new ExpandListAdapter( NovelIntroduceActivity.this, mGroups, novelName);
	            	novelListView.setAdapter(mAdapter);
				}
			}
			
		});
		
		novelTextDescription.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				if(descriptionExpand.equals(false)){
					novelTextDescription.setMaxLines(999);
					descriptionExpand = true;
					imageArrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_arrow_up));
				}else{
					novelTextDescription.setMaxLines(3);
					descriptionExpand = false;
					imageArrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_arrow_right));
				}
			}
		});
		
		layoutTextArrow.setOnClickListener(new OnClickListener() {			 
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				if(descriptionExpand.equals(false)){
					novelTextDescription.setMaxLines(999);
					descriptionExpand = true;
					imageArrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_arrow_up));
				}else{
					novelTextDescription.setMaxLines(3);
					descriptionExpand = false;
					imageArrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_arrow_right));
				}
			}
		});
		
		imageArrow.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				if(descriptionExpand.equals(false)){
					novelTextDescription.setMaxLines(999);
					descriptionExpand = true;
					imageArrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_arrow_up));
				}else{
					novelTextDescription.setMaxLines(3);
					descriptionExpand = false;
					imageArrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_arrow_right));
				}
			}
		});
		
		checkBoxAddBookcase.setOnClickListener((new OnClickListener(){  
            public void onClick(View v) { 
            	if(checkBoxAddBookcase.isChecked()){
            		Toast.makeText(NovelIntroduceActivity.this, NovelIntroduceActivity.this.getResources().getString(R.string.add_my_bookcase), Toast.LENGTH_SHORT).show();
            		NovelAPI.collecNovel(theNovel, NovelIntroduceActivity.this);
            	}else{
            		Toast.makeText(NovelIntroduceActivity.this, NovelIntroduceActivity.this.getResources().getString(R.string.remove_my_bookcase), Toast.LENGTH_SHORT).show();
            		// need remove api
            	}
            }  
        }));
	}
	
	private void reverseMGroups() {
		// TODO Auto-generated method stub
		ArrayList<Group> aGroups = new ArrayList<Group>(mGroups.size());
		for(int i=0; i<mGroups.size(); i++){
			int groupInt = mGroups.size()-i-1;
			aGroups.add(mGroups.get(groupInt));
			ArrayList<ChildArticle> theChildren = new ArrayList<ChildArticle>(mGroups.get(groupInt).getChildrenCount());
			for (int j=0; j<mGroups.get(groupInt).getChildrenCount(); j++){
				theChildren.add(mGroups.get(groupInt).getChildItem(mGroups.get(groupInt).getChildrenCount()-j-1));
			}
			aGroups.get(i).setChilds(theChildren);
		}
		mGroups.clear();
		mGroups = aGroups;
	}
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
		
		
		menu.add(0, ID_SETTING, 0, "設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_RESPONSE, 1, "意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_ABOUT_US, 2, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_GRADE, 3, "為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, ID_DOWNLOAD, 5, "下載").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		
//		menu.add("設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//		menu.add("意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//		menu.add("關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//		menu.add("為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
        menu.add(0, ID_SEARCH, 4,"Search")
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
	    case ID_SETTING: // setting
	    		Intent intent = new Intent(NovelIntroduceActivity.this, SettingActivity.class);
	    		startActivity(intent); 
	        break;
	    case ID_RESPONSE: // response
    			Toast.makeText(NovelIntroduceActivity.this, "RESPONESE", Toast.LENGTH_SHORT).show();
    		break;
	    case ID_ABOUT_US: // response
			Toast.makeText(NovelIntroduceActivity.this, "ABOUT_US", Toast.LENGTH_SHORT).show();
			break;
	    case ID_GRADE: // response
			Toast.makeText(NovelIntroduceActivity.this, "GRADE", Toast.LENGTH_SHORT).show();
			break;
	    case ID_DOWNLOAD: // response
		    	Intent intent_to_download = new Intent(NovelIntroduceActivity.this, DownloadActivity.class);
	            Bundle bundle = new Bundle();
	 			bundle.putInt("NovelId", novelId); 
	 			bundle.putString("NovelName", novelName);
	 			intent_to_download.putExtras(bundle);
	 			startActivity(intent_to_download);
			break;
	    case ID_SEARCH: // response
			Toast.makeText(NovelIntroduceActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
			break;	
	    }
	    return true;
	}
	
	 private class DownloadArticlesTask extends AsyncTask {

	        @Override
	        protected Object doInBackground(Object... params) {
	            
	        	articleList = NovelAPI.getNovelArticles(novelId, true, NovelIntroduceActivity.this);
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Object result) {
	          
	            super.onPostExecute(result);
	            novelLayoutProgress.setVisibility(View.GONE);
	            if(articleList!= null && articleList.size()!= 0){
	            	
	            	// use HashMap || TreeMap to make a parent key
	            	for(int i=0; i<articleList.size(); i++){
	            		if(myData.containsKey(articleList.get(i).getSubject())){
	            			myData.get(articleList.get(i).getSubject()).add(articleList.get(i));
	            		}else{
//	            			groupTitleList.add(articleList.get(i).getSubject());
	            			mGroups.add(new Group(articleList.get(i).getSubject()));
	            			myData.put(articleList.get(i).getSubject(), new ArrayList<Article>());
	            			myData.get(articleList.get(i).getSubject()).add(articleList.get(i));
	            		}
	            	}
	            	 
	            	for(int i= 0; i< mGroups.size(); i++){
	            		ArrayList<Article> articles = myData.get(mGroups.get(i).getTitle());
	            		for(int j=0; j< articles.size(); j++){
	            			mGroups.get(i).addChildrenItem(new ChildArticle(articles.get(j).getId(), articles.get(j).getNovelId(), "", articles.get(j).getTitle(), articles.get(j).getSubject(), articles.get(j).isDownload()));
	            		}
	            	}
	            	
	            	ExpandListAdapter mAdapter = new ExpandListAdapter( NovelIntroduceActivity.this, mGroups, novelName);
	            	novelListView.setAdapter(mAdapter);
	            	
	            	//FastScroller problem
//	            	FastScroller newFastScroller = new FastScroller(NovelIntroduceActivity.this, novelListView);
//	            	newFastScroller.setState(2);
//	            	newFastScroller.draw(new Canvas());
	            	
//	            	novelListView.setFastScrollEnabled(true);
//            	
//	            	novelListView.setOnScrollListener(new OnScrollListener() {  
//	                    
//	                    @Override  
//	                    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {  
//	                          
//	                          
//	                    }  
//	                      
//	                    @Override  
//	                    public void onScroll(AbsListView paramAbsListView, int firstVisibleItem,  
//	                            int visibleItemCount, int totalItemCount) {  
//	                    	
//	                    	System.out.println("***first:"+firstVisibleItem);
//	                    	System.out.println("***visible:"+visibleItemCount);
//	                    	System.out.println("***Total:"+totalItemCount);
//	                    	
//	                    }  
//	                });  
	              
	            	
	            	
//		            mListAdapter = new ListArticleAdapter(NovelIntroduceActivity.this, articleList);
//		            novelListView.setAdapter(mListAdapter);
	            }

	        }
	 }
	 
	 private class DownloadNovelTask extends AsyncTask {

	        @Override
	        protected Object doInBackground(Object... params) {
	        	theNovel = NovelAPI.getNovel(novelId, NovelIntroduceActivity.this);
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Object result) {
	            super.onPostExecute(result);
	            novelTextDescription.setText(theNovel.getDescription());
	            novelTextDescription.setMaxLines(3);
	            descriptionExpand = false;
	        }
	 }
    

}
