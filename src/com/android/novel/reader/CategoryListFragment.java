package com.android.novel.reader;


import java.util.ArrayList;

import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Category;
import com.taiwan.imageload.ListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;



public class CategoryListFragment extends ListFragment {

  private ArrayList<Category> categories = new ArrayList<Category>();
  private static Activity mActivity;
	
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
//    String[] values = new String[] { "經典武俠", "經典小說", "長篇",
//        "短篇" };
    
    categories = NovelAPI.getCategories();
    ListAdapter adapter = new ListAdapter(mActivity, categories);
   
    setListAdapter(adapter);
  }
  
  public static ListFragment newInstance(Activity myActivity) {
	  mActivity = myActivity;
	  CategoryListFragment fragment = new CategoryListFragment();
      return fragment;
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    // Do something with the data
	  	Intent intent = new Intent(mActivity, CategoryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("CategoryId", categories.get(position).getId()); 
		bundle.putString("CategoryName", categories.get(position).getCateName());
		intent.putExtras(bundle);
		mActivity.startActivity(intent);
  }

} 
