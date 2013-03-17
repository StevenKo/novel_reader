package com.taiwan.imageload;

import java.util.ArrayList;
import java.util.TreeMap;
import com.android.novel.reader.ArticleActivity;
import com.android.novel.reader.R;
import com.android.novel.reader.entity.Article;
import com.android.novel.reader.entity.Novel;
import com.kosbrother.tool.ChildArticle;
import com.kosbrother.tool.Group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandListAdapter extends BaseExpandableListAdapter {

 
	 
	 private static LayoutInflater inflater=null;
	 private Activity activity; 
	 public ArrayList<Group> theGroups;
	 private Novel theNovel;
	 
	 public ExpandListAdapter(Activity a, ArrayList<Group> mGroups, Novel mNovel) {
		 
		 activity = a;
		 theGroups = mGroups;
		 
		 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		 theNovel = mNovel;
	 }

	 @Override
	 public Object getChild(int groupPosition, int childPosition) {
	  return null;
	 }

	 @Override
	 public long getChildId(int groupPosition, int childPosition) {
	  return 0;
	 }

	 @Override
	 public View getChildView(int groupPosition, final int childPosition,
	   boolean isLastChild, View convertView, ViewGroup parent) {
		 
		 final ChildArticle child = theGroups.get(groupPosition).getChildItem(childPosition);
		 
		 View vi=convertView;
	     vi = inflater.inflate(R.layout.item_expandible_child, null);
	     TextView text=(TextView)vi.findViewById(R.id.expandlist_child);
	     String childString = child.getTitle();
	     text.setText(childString);
	     
	     vi.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	            Intent intent = new Intent(activity, ArticleActivity.class);
	            Bundle bundle = new Bundle();
	 			bundle.putInt("ArticleId", child.getId()); 
	 			bundle.putString("ArticleTitle", child.getTitle());
	 			bundle.putString("NovelName", theNovel.getName());
	 			bundle.putString("NovelPic", theNovel.getPic());
	 			bundle.putInt("NovelId", theNovel.getId());
	 			intent.putExtras(bundle);
	 			activity.startActivity(intent);
	        }
	     });
	     
		   
	  return vi;
	 }

	 @Override
	 public int getChildrenCount(int groupPosition) {	 
		 return theGroups.get(groupPosition).getChildrenCount();
	 }

	 @Override
	 public Object getGroup(int groupPosition) {
		 return theGroups.get(groupPosition);
	 }

	 @Override
	 public int getGroupCount() {
		 return theGroups.size();
	 }

	 @Override
	 public void onGroupCollapsed(int groupPosition) {
	  super.onGroupCollapsed(groupPosition);
	 }

	 @Override
	 public void onGroupExpanded(int groupPosition) {
	  super.onGroupExpanded(groupPosition);
	 }

	 @Override
	 public long getGroupId(int groupPosition) {
		 return groupPosition;
	 }

	 @Override
	 public View getGroupView(int groupPosition, boolean isExpanded,
	   View convertView, ViewGroup parent) {
		 
		 Group group = theGroups.get(groupPosition);
		 
		 View vi=convertView;
	     vi = inflater.inflate(R.layout.item_expandbile_parent, null);
	     TextView text=(TextView)vi.findViewById(R.id.expandlist_parent);
	     String groupString = group.getTitle();
	     text.setText(groupString);	  
	     
	     int id = (!isExpanded) ? R.drawable.right_arrow
	                : R.drawable.up_arrow;
	     ImageView image = (ImageView) vi.findViewById(R.id.expandlist_parent_button);
	     image.setImageResource(id);
	       		   
	  return vi;
	 }
	 
	 @Override
	 public boolean hasStableIds() {
	  return true;
	 }

	 @Override
	 public boolean isChildSelectable(int groupPosition, int childPosition) {
	  return true;
	 }
	 
	 

}