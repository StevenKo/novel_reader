package com.kosbrother.fragments;

import java.util.ArrayList;

import com.android.novel.reader.R;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Novel;

import com.taiwan.imageload.GridViewAdapter;
import com.taiwan.imageload.ListNothingAdapter;
import com.taiwan.imageload.LoadMoreGridView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public final class CategoryNewNovelsFragment extends Fragment {
    
	private ArrayList<Novel> novels = new ArrayList<Novel>();
	private ArrayList<Novel> moreNovels = new ArrayList<Novel>();
	private static int myPage = 1;
	private LoadMoreGridView  myGrid;
	private GridViewAdapter myGridViewAdapter;
	private Boolean checkLoad = true;
	private LinearLayout progressLayout;
	private LinearLayout loadmoreLayout;
	private LinearLayout noDataLayout;
	private LinearLayout layoutReload;
	private static int id;
	private Button buttonReload;
	
    public static CategoryNewNovelsFragment newInstance(int categoryId) {     
    	 

//	  myPage = page;
//	  novels = theNovels;
      id = categoryId;
 
  	  CategoryNewNovelsFragment fragment = new CategoryNewNovelsFragment();
  	    
      return fragment;
        
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        new DownloadChannelsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	
    	View myFragmentView = inflater.inflate(R.layout.loadmore_grid, container, false);
    	progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
    	loadmoreLayout = (LinearLayout) myFragmentView.findViewById(R.id.load_more_grid);
    	noDataLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_no_data);
    	layoutReload = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
    	buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
    	myGrid = (LoadMoreGridView) myFragmentView.findViewById(R.id.news_list);
    	myGrid.setOnLoadMoreListener(new LoadMoreGridView.OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				
				if(checkLoad){
					myPage = myPage +1;
					loadmoreLayout.setVisibility(View.VISIBLE);
					new LoadMoreTask().execute();
				}else{
					myGrid.onLoadMoreComplete();
				}
			}
		});
    	
    	buttonReload.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				progressLayout.setVisibility(View.VISIBLE);
				new DownloadChannelsTask().execute();
			}
		});
    	
    	if (myGridViewAdapter != null){
    		progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);
      		myGrid.setAdapter(myGridViewAdapter);
    	}else{
    		new DownloadChannelsTask().execute();
    	}
    	
        return myFragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       
    }
    
    private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub

        	novels = NovelAPI.getCategoryNovels(id, myPage); 
//        	moreNovels = NovelAPI.getThisWeekHotNovels(); 

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);
                   
            if(novels !=null && novels.size()!= 0){
          	  try{
          		layoutReload.setVisibility(View.GONE);
          		myGridViewAdapter = new GridViewAdapter(getActivity(), novels);
          		myGrid.setAdapter(myGridViewAdapter);
          	  }catch(Exception e){
          		 
          	  }
            }else{
            	layoutReload.setVisibility(View.VISIBLE);
//            	noDataLayout.setVisibility(View.VISIBLE);
//          	  ListNothingAdapter nothingAdapter = new ListNothingAdapter(getActivity());
//          	  myGrid.setAdapter(nothingAdapter);
            }

        }
    }
    
    
    private class LoadMoreTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub

        	moreNovels = NovelAPI.getCategoryNovels(id, myPage); 
        	if(moreNovels!= null){
        		for(int i=0; i<moreNovels.size();i++){
	        		novels.add(moreNovels.get(i));
	            }
        	}
        	
        	
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            
            loadmoreLayout.setVisibility(View.GONE);
            
            if(moreNovels!= null){
            	myGridViewAdapter.notifyDataSetChanged();	                
            }else{
                checkLoad= false;
                Toast.makeText(getActivity(), "no more data", Toast.LENGTH_SHORT).show();            	
            }       
            myGrid.onLoadMoreComplete();
          	
          	
        }
    }
    
}
