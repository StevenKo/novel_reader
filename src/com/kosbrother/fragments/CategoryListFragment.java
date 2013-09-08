package com.kosbrother.fragments;


import java.util.ArrayList;

import com.novel.reader.CategoryActivity;
import com.novel.reader.adapter.ListAdapter;
import com.novel.reader.api.NovelAPI;
import com.novel.reader.entity.Category;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;



public class CategoryListFragment extends ListFragment {

  private ArrayList<Category> categories = new ArrayList<Category>();
//  private static Activity mActivity;
	
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
//    String[] values = new String[] { "經典武俠", "經典小說", "長篇",
//        "短篇" };
    
    categories = NovelAPI.getCategories();
    ListAdapter adapter = new ListAdapter(getActivity(), categories);
   
    setListAdapter(adapter);
  }
  
  public static ListFragment newInstance(Activity myActivity) {
//	  mActivity = myActivity;
	  CategoryListFragment fragment = new CategoryListFragment();
      return fragment;
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    // Do something with the data
	  	Intent intent = new Intent(getActivity(), CategoryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("CategoryId", categories.get(position).getId()); 
		bundle.putString("CategoryName", categories.get(position).getCateName());
		intent.putExtras(bundle);
		getActivity().startActivity(intent);
  }

} 
