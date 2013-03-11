package com.taiwan.imageload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.android.novel.reader.R;
import com.android.novel.reader.entity.Article;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	 public ArrayList<String> groupListArray;
//	 public String[] groupListArray;	 
	 public TreeMap<String, ArrayList<Article>> theData = new TreeMap<String, ArrayList<Article>>();
	 
	 private static LayoutInflater inflater=null;
	 private Activity activity;
	 
	 public ExpandListAdapter(Activity a, TreeMap<String, ArrayList<Article>> myData, ArrayList<String> groupTitles) {
		 
		 activity = a;
		 theData = myData;
		 
//		 Set<String> keyArray =  theData.keySet();
//		 String groupString = keyArray.toString();		 
//		 groupListArray = parseCsvLine(groupString);
		 
		 groupListArray = groupTitles;
		 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		 
		 View vi=convertView;
	     vi = inflater.inflate(R.layout.item_expandible_child, null);
	     TextView text=(TextView)vi.findViewById(R.id.expandlist_child);
//	     String aString = groupListArray[groupPosition];
//	  	 aString = aString.substring(aString.indexOf(" ")+1);
	     String aString = groupListArray.get(groupPosition);
	     ArrayList<Article> childArticles = theData.get(aString);
	     String childString = childArticles.get(childPosition).getTitle();
	     text.setText(childString);
//	        text.setText("test");
		   
	  return vi;
	 }

	 @Override
	 public int getChildrenCount(int groupPosition) {	 
      String aString = groupListArray.get(groupPosition);
	  int ii = theData.get(aString).size();
	  return ii;
//		 return 2;
	 }

	 @Override
	 public Object getGroup(int groupPosition) {
	  return groupListArray.get(groupPosition);
	 }

	 @Override
	 public int getGroupCount() {
	  return groupListArray.size();
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
		 View vi=convertView;
	     vi = inflater.inflate(R.layout.item_expandbile_parent, null);
	     TextView text=(TextView)vi.findViewById(R.id.expandlist_parent);
	     String groupString = groupListArray.get(groupPosition);
	     text.setText(groupString);	  
	     
	     int id = (!isExpanded) ? R.drawable.right_arrow
	                : R.drawable.up_arrow;
	     ImageView image = (ImageView) vi.findViewById(R.id.expandlist_parent_button);
	     image.setImageResource(id);
	       		   
	  return vi;
	 }
	 
	 @Override
	 public boolean hasStableIds() {
	  return false;
	 }

	 @Override
	 public boolean isChildSelectable(int groupPosition, int childPosition) {
	  return false;
	 }
	 
	 public static String[] parseCsvLine(String line) {
	        // Create a pattern to match breaks
	        Pattern p =
	            Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
	        // Split input with the pattern
	        String[] fields = p.split(line);
	        for (int i = 0; i < fields.length; i++) {
	            // Get rid of residual double quotes
	            fields[i] = fields[i].replace("\"", "");
	        }
	        return fields;
	 }


}