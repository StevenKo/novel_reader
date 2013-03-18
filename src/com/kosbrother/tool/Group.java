package com.kosbrother.tool;

import java.util.ArrayList;

public class Group {
//    private String id;
    private String title;
    private ArrayList<ChildArticle> children;
    private boolean isChecked;
 
    public Group(String title) {
//        this.id = id;
        this.title = title;
        children = new ArrayList<ChildArticle>();
        this.isChecked = false;
    }
 
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
 
    public void toggle() {
        this.isChecked = !this.isChecked;
    }
 
    public boolean getChecked() {
        return this.isChecked;
    }
 
//    public String getId() {
//        return id;
//    }
 
    public String getTitle() {
        return title;
    }
 
    public void addChildrenItem(ChildArticle child) {
        children.add(child);
    }
 
    public int getChildrenCount() {
        return children.size();
    }
 
    public ChildArticle getChildItem(int index) {
        return children.get(index);
    }
    
    public void clearChilds() {
    	children.clear();
    }
    
    public void setChilds(ArrayList<ChildArticle> childs) {
    	children.clear();
    	for(int i= 0; i<childs.size(); i++){
    		children.add(childs.get(i));
    	}
    }
    
    public int getGroupCheckedCount() {
    	int checkCount = 0;
    	for(int i=0; i< children.size(); i++){
    		if(children.get(i).getChecked() && !children.get(i).isDownloaded){
    			checkCount = checkCount +1;
    		}
    	}   	
    	return checkCount;
    }
    
}
