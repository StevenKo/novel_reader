package com.android.novel.reader;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Novel;
import com.taiwan.imageload.GridViewAdapter;
import com.taiwan.imageload.ListNothingAdapter;
import com.taiwan.imageload.LoadMoreGridView;

public class HotNovelsFragment extends Fragment {
    
	private ArrayList<Novel> novels = new ArrayList<Novel>();
	private ArrayList<Novel> moreNovels = new ArrayList<Novel>();
	private static int myPage = 0;
	private LoadMoreGridView  myGrid;
	private GridViewAdapter myGridViewAdapter;
	private Boolean checkLoad = true;
	private LinearLayout progressLayout;
	private LinearLayout loadmoreLayout;
	
    public static HotNovelsFragment newInstance() {     
   	 

//  	  myPage = page;
//  	  novels = theNovels;
 
    	HotNovelsFragment fragment = new HotNovelsFragment();
  	    
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

        	novels =  NovelAPI.getHotNovels(); 
//        	moreNovels = NovelAPI.getHotNovels(); 

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);
   
        	if(moreNovels!= null){
	        	for(int i=0; i<moreNovels.size();i++){
	        		novels.add(moreNovels.get(i));
	            }
	        	for(int i=0; i<moreNovels.size();i++){
	        		novels.add(moreNovels.get(i));
	            }
	        	for(int i=0; i<moreNovels.size();i++){
	        		novels.add(moreNovels.get(i));
	            }
	        	for(int i=0; i<moreNovels.size();i++){
	        		novels.add(moreNovels.get(i));
	            }
        	}
            
            
            if(novels !=null){
          	  try{
          		myGridViewAdapter = new GridViewAdapter(getActivity(), novels);
          		myGrid.setAdapter(myGridViewAdapter);
          	  }catch(Exception e){
          		 
          	  }
            }else{
          	  ListNothingAdapter nothingAdapter = new ListNothingAdapter(getActivity());
          	  myGrid.setAdapter(nothingAdapter);
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

        	moreNovels = NovelAPI.getThisMonthHotNovels();
        	if(moreNovels!= null){
	        	for(int i=0; i<moreNovels.size();i++){
	        		novels.add(moreNovels.get(i));
	            }
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
