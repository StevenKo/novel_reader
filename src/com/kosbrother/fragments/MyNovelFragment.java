package com.kosbrother.fragments;


import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.novel.db.SQLiteNovel;
import com.novel.reader.BookmarkActivity;
import com.novel.reader.ClassicNovelsActivity;
import com.novel.reader.MyNovelActivity;
import com.novel.reader.R;
import com.novel.reader.SettingActivity;
import com.novel.reader.adapter.GridViewDownloadAdapter;
import com.novel.reader.adapter.GridViewIndexBookmarkAdapter;
import com.novel.reader.adapter.GridViewIndexNovelAdapter;
import com.novel.reader.api.NovelAPI;
import com.novel.reader.costum.view.ExpandableHeightGridView;
import com.novel.reader.entity.Bookmark;
import com.novel.reader.entity.Novel;

public final class MyNovelFragment extends Fragment {

    public static MyNovelFragment newInstance() {
        MyNovelFragment fragment = new MyNovelFragment();
        return fragment;
    }

    private View         myFragmentView;
    private RelativeLayout myBookmarks;
    private LinearLayout recentRead;
    private RelativeLayout myBookcase;
    private LinearLayout mySetting;
    private RelativeLayout classicKongFu;
    private RelativeLayout classicNovel;
    private ExpandableHeightGridView novelGridView;
    private ExpandableHeightGridView bookmarkGridView;
    private GridViewIndexNovelAdapter novelAdapter;
    private GridViewIndexBookmarkAdapter bookmarkAdapter;
	private ArrayList<Novel> novels;
	private ArrayList<Bookmark> bookmarks;
	private LinearLayout noNovelInBookcase;
	private LinearLayout noBookmarkInBookmarks;
	private LinearLayout progressLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.my_novel, container, false);
        findViews();
        return myFragmentView;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        progressLayout.setVisibility(View.VISIBLE);
        new DownloadChannelsTask().execute();
    }
    
    private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
        	getData();
            progressLayout.setVisibility(View.GONE);
            setViews();
        }
    }

    private void getData() {
    	SQLiteNovel db = new SQLiteNovel(this.getActivity());
    	novels = db.getLastCollectNovels(3);
    	novels.addAll(db.getLastDownloadNovels(3));
    	bookmarks = db.getLastBookmarks(3);
    	bookmarks.addAll(db.getLastRecentBookmarks(3));
    	novelAdapter = new GridViewIndexNovelAdapter(getActivity(),novels);
    	bookmarkAdapter = new GridViewIndexBookmarkAdapter(getActivity(),bookmarks);
    	if(novels.size() > 0){
    		noNovelInBookcase.setVisibility(View.GONE);
    		novelGridView.setVisibility(View.VISIBLE);
    	}else{
    		novelGridView.setVisibility(View.GONE);
    		noNovelInBookcase.setVisibility(View.VISIBLE);
    	}
    	
    	if(bookmarks.size() > 0){
    		noBookmarkInBookmarks.setVisibility(View.GONE);
    		bookmarkGridView.setVisibility(View.VISIBLE);
    	}else{
    		bookmarkGridView.setVisibility(View.GONE);
    		noBookmarkInBookmarks.setVisibility(View.VISIBLE);
    	}
	}

	@Override
    public void onStart() {
        super.onStart();
    }

    private void setViews() {

        myBookmarks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_RECNET", false);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), BookmarkActivity.class);
                startActivity(intent);
            }
        });
        
        noBookmarkInBookmarks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_RECNET", false);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), BookmarkActivity.class);
                startActivity(intent);
            }
        });
        
        recentRead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_RECNET", true);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), BookmarkActivity.class);
                startActivity(intent);
            }
        });

        myBookcase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyNovelActivity.class);
                startActivity(intent);
            }
        });
        
        noNovelInBookcase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyNovelActivity.class);
                startActivity(intent);
            }
        });

        mySetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        classicKongFu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassicNovelsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ClassTitle", "經典武俠");
                bundle.putInt("ClassicId", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        classicNovel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassicNovelsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ClassTitle", "經典小說");
                bundle.putInt("ClassicId", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        novelGridView.setExpanded(true);
        bookmarkGridView.setExpanded(true);
        novelGridView.setAdapter(novelAdapter);
        bookmarkGridView.setAdapter(bookmarkAdapter);
        
    }

    private void findViews() {
        myBookmarks = (RelativeLayout) myFragmentView.findViewById(R.id.my_bookmarks);
        recentRead = (LinearLayout) myFragmentView.findViewById(R.id.my_recent_read_bookmarks);
        myBookcase = (RelativeLayout) myFragmentView.findViewById(R.id.my_bookcase);
        mySetting = (LinearLayout) myFragmentView.findViewById(R.id.my_setting);
        classicKongFu = (RelativeLayout) myFragmentView.findViewById(R.id.classic_kongfu);
        classicNovel = (RelativeLayout) myFragmentView.findViewById(R.id.classic_novel);
        novelGridView = (ExpandableHeightGridView) myFragmentView.findViewById(R.id.my_bookcase_grid);
        bookmarkGridView = (ExpandableHeightGridView) myFragmentView.findViewById(R.id.my_bookmarks_grid);
        noNovelInBookcase = (LinearLayout) myFragmentView.findViewById(R.id.no_novel_in_bookcase);
        noBookmarkInBookmarks = (LinearLayout) myFragmentView.findViewById(R.id.no_bookmark_in_my_bookmarks);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
