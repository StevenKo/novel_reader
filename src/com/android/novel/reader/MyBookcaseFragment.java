package com.android.novel.reader;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Novel;
import com.taiwan.imageload.GridViewAdapter;
import com.taiwan.imageload.ListNothingAdapter;
import com.taiwan.imageload.LoadMoreGridView;

public class MyBookcaseFragment extends Fragment {
    
	private ArrayList<Novel> novels = new ArrayList<Novel>();
	private LoadMoreGridView  myGrid;
	private GridViewAdapter myGridViewAdapter;
	private LinearLayout progressLayout;
	private LinearLayout loadmoreLayout;
	private LinearLayout noDataLayout;
	
    public static MyBookcaseFragment newInstance() {     
   	 
    	MyBookcaseFragment fragment = new MyBookcaseFragment();
  	    
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
    	myGrid = (LoadMoreGridView) myFragmentView.findViewById(R.id.news_list);
    	myGrid.setOnLoadMoreListener(new LoadMoreGridView.OnLoadMoreListener() {
			public void onLoadMore() {
//				// Do the work to load more items at the end of list
//				
//				if(checkLoad){
//					myPage = myPage +1;
//					loadmoreLayout.setVisibility(View.VISIBLE);
//					new LoadMoreTask().execute();
//				}else{
//					myGrid.onLoadMoreComplete();
//				}
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

        	novels = NovelAPI.getCollectedNovels(getActivity());
//        	moreNovels = NovelAPI.getThisWeekHotNovels(); 

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);           
            
            if(novels !=null && novels.size()!=0){
          	  try{
          		myGridViewAdapter = new GridViewAdapter(getActivity(), novels);
          		myGrid.setAdapter(myGridViewAdapter);
          	  }catch(Exception e){
          		 
          	  }
            }else{
            	myGrid.setVisibility(View.GONE);
            	noDataLayout.setVisibility(View.VISIBLE);
            }

        }
    }
    
  
}